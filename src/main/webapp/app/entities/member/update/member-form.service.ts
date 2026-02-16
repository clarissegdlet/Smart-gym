import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMember, NewMember } from '../member.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMember for edit and NewMemberFormGroupInput for create.
 */
type MemberFormGroupInput = IMember | PartialWithRequiredKeyOf<NewMember>;

type MemberFormDefaults = Pick<NewMember, 'id'>;

type MemberFormGroupContent = {
  id: FormControl<IMember['id'] | NewMember['id']>;
  firstName: FormControl<IMember['firstName']>;
  lastName: FormControl<IMember['lastName']>;
  email: FormControl<IMember['email']>;
  phoneNumber: FormControl<IMember['phoneNumber']>;
  stravaAccount: FormControl<IMember['stravaAccount']>;
};

export type MemberFormGroup = FormGroup<MemberFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MemberFormService {
  createMemberFormGroup(member: MemberFormGroupInput = { id: null }): MemberFormGroup {
    const memberRawValue = {
      ...this.getFormDefaults(),
      ...member,
    };
    return new FormGroup<MemberFormGroupContent>({
      id: new FormControl(
        { value: memberRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      firstName: new FormControl(memberRawValue.firstName, {
        validators: [Validators.required],
      }),
      lastName: new FormControl(memberRawValue.lastName, {
        validators: [Validators.required],
      }),
      email: new FormControl(memberRawValue.email, {
        validators: [Validators.required],
      }),
      phoneNumber: new FormControl(memberRawValue.phoneNumber),
      stravaAccount: new FormControl(memberRawValue.stravaAccount),
    });
  }

  getMember(form: MemberFormGroup): IMember | NewMember {
    return form.getRawValue() as IMember | NewMember;
  }

  resetForm(form: MemberFormGroup, member: MemberFormGroupInput): void {
    const memberRawValue = { ...this.getFormDefaults(), ...member };
    form.reset(
      {
        ...memberRawValue,
        id: { value: memberRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MemberFormDefaults {
    return {
      id: null,
    };
  }
}
