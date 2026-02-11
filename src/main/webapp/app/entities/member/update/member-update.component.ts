import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IStravaAccount } from 'app/entities/strava-account/strava-account.model';
import { StravaAccountService } from 'app/entities/strava-account/service/strava-account.service';
import { IMember } from '../member.model';
import { MemberService } from '../service/member.service';
import { MemberFormGroup, MemberFormService } from './member-form.service';

@Component({
  selector: 'jhi-member-update',
  templateUrl: './member-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MemberUpdateComponent implements OnInit {
  isSaving = false;
  member: IMember | null = null;

  stravaAccountsCollection: IStravaAccount[] = [];

  protected memberService = inject(MemberService);
  protected memberFormService = inject(MemberFormService);
  protected stravaAccountService = inject(StravaAccountService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MemberFormGroup = this.memberFormService.createMemberFormGroup();

  compareStravaAccount = (o1: IStravaAccount | null, o2: IStravaAccount | null): boolean =>
    this.stravaAccountService.compareStravaAccount(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ member }) => {
      this.member = member;
      if (member) {
        this.updateForm(member);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const member = this.memberFormService.getMember(this.editForm);
    if (member.id !== null) {
      this.subscribeToSaveResponse(this.memberService.update(member));
    } else {
      this.subscribeToSaveResponse(this.memberService.create(member));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMember>>): void {
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

  protected updateForm(member: IMember): void {
    this.member = member;
    this.memberFormService.resetForm(this.editForm, member);

    this.stravaAccountsCollection = this.stravaAccountService.addStravaAccountToCollectionIfMissing<IStravaAccount>(
      this.stravaAccountsCollection,
      member.stravaAccount,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.stravaAccountService
      .query({ filter: 'member-is-null' })
      .pipe(map((res: HttpResponse<IStravaAccount[]>) => res.body ?? []))
      .pipe(
        map((stravaAccounts: IStravaAccount[]) =>
          this.stravaAccountService.addStravaAccountToCollectionIfMissing<IStravaAccount>(stravaAccounts, this.member?.stravaAccount),
        ),
      )
      .subscribe((stravaAccounts: IStravaAccount[]) => (this.stravaAccountsCollection = stravaAccounts));
  }
}
