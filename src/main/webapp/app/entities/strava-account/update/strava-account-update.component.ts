import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IStravaAccount } from '../strava-account.model';
import { StravaAccountService } from '../service/strava-account.service';
import { StravaAccountFormGroup, StravaAccountFormService } from './strava-account-form.service';

@Component({
  selector: 'jhi-strava-account-update',
  templateUrl: './strava-account-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StravaAccountUpdateComponent implements OnInit {
  isSaving = false;
  stravaAccount: IStravaAccount | null = null;

  protected stravaAccountService = inject(StravaAccountService);
  protected stravaAccountFormService = inject(StravaAccountFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StravaAccountFormGroup = this.stravaAccountFormService.createStravaAccountFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stravaAccount }) => {
      this.stravaAccount = stravaAccount;
      if (stravaAccount) {
        this.updateForm(stravaAccount);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const stravaAccount = this.stravaAccountFormService.getStravaAccount(this.editForm);
    if (stravaAccount.id !== null) {
      this.subscribeToSaveResponse(this.stravaAccountService.update(stravaAccount));
    } else {
      this.subscribeToSaveResponse(this.stravaAccountService.create(stravaAccount));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStravaAccount>>): void {
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

  protected updateForm(stravaAccount: IStravaAccount): void {
    this.stravaAccount = stravaAccount;
    this.stravaAccountFormService.resetForm(this.editForm, stravaAccount);
  }
}
