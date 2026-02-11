import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IStravaAccount } from 'app/entities/strava-account/strava-account.model';
import { StravaAccountService } from 'app/entities/strava-account/service/strava-account.service';
import { IActivity } from '../activity.model';
import { ActivityService } from '../service/activity.service';
import { ActivityFormGroup, ActivityFormService } from './activity-form.service';

@Component({
  selector: 'jhi-activity-update',
  templateUrl: './activity-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ActivityUpdateComponent implements OnInit {
  isSaving = false;
  activity: IActivity | null = null;

  stravaAccountsSharedCollection: IStravaAccount[] = [];

  protected activityService = inject(ActivityService);
  protected activityFormService = inject(ActivityFormService);
  protected stravaAccountService = inject(StravaAccountService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ActivityFormGroup = this.activityFormService.createActivityFormGroup();

  compareStravaAccount = (o1: IStravaAccount | null, o2: IStravaAccount | null): boolean =>
    this.stravaAccountService.compareStravaAccount(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ activity }) => {
      this.activity = activity;
      if (activity) {
        this.updateForm(activity);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const activity = this.activityFormService.getActivity(this.editForm);
    if (activity.id !== null) {
      this.subscribeToSaveResponse(this.activityService.update(activity));
    } else {
      this.subscribeToSaveResponse(this.activityService.create(activity));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IActivity>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(activity: IActivity): void {
    this.activity = activity;
    this.activityFormService.resetForm(this.editForm, activity);

    this.stravaAccountsSharedCollection = this.stravaAccountService.addStravaAccountToCollectionIfMissing<IStravaAccount>(
      this.stravaAccountsSharedCollection,
      activity.stravaAccount,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.stravaAccountService
      .query()
      .pipe(map((res: HttpResponse<IStravaAccount[]>) => res.body ?? []))
      .pipe(
        map((stravaAccounts: IStravaAccount[]) =>
          this.stravaAccountService.addStravaAccountToCollectionIfMissing<IStravaAccount>(stravaAccounts, this.activity?.stravaAccount),
        ),
      )
      .subscribe((stravaAccounts: IStravaAccount[]) => (this.stravaAccountsSharedCollection = stravaAccounts));
  }
}
