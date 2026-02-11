import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IStravaAccount, NewStravaAccount } from '../strava-account.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStravaAccount for edit and NewStravaAccountFormGroupInput for create.
 */
type StravaAccountFormGroupInput = IStravaAccount | PartialWithRequiredKeyOf<NewStravaAccount>;

type StravaAccountFormDefaults = Pick<NewStravaAccount, 'id'>;

type StravaAccountFormGroupContent = {
  id: FormControl<IStravaAccount['id'] | NewStravaAccount['id']>;
  stravaUserId: FormControl<IStravaAccount['stravaUserId']>;
  accessToken: FormControl<IStravaAccount['accessToken']>;
};

export type StravaAccountFormGroup = FormGroup<StravaAccountFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StravaAccountFormService {
  createStravaAccountFormGroup(stravaAccount: StravaAccountFormGroupInput = { id: null }): StravaAccountFormGroup {
    const stravaAccountRawValue = {
      ...this.getFormDefaults(),
      ...stravaAccount,
    };
    return new FormGroup<StravaAccountFormGroupContent>({
      id: new FormControl(
        { value: stravaAccountRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      stravaUserId: new FormControl(stravaAccountRawValue.stravaUserId, {
        validators: [Validators.required],
      }),
      accessToken: new FormControl(stravaAccountRawValue.accessToken, {
        validators: [Validators.required],
      }),
    });
  }

  getStravaAccount(form: StravaAccountFormGroup): IStravaAccount | NewStravaAccount {
    return form.getRawValue() as IStravaAccount | NewStravaAccount;
  }

  resetForm(form: StravaAccountFormGroup, stravaAccount: StravaAccountFormGroupInput): void {
    const stravaAccountRawValue = { ...this.getFormDefaults(), ...stravaAccount };
    form.reset(
      {
        ...stravaAccountRawValue,
        id: { value: stravaAccountRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): StravaAccountFormDefaults {
    return {
      id: null,
    };
  }
}
