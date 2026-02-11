import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../strava-account.test-samples';

import { StravaAccountFormService } from './strava-account-form.service';

describe('StravaAccount Form Service', () => {
  let service: StravaAccountFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StravaAccountFormService);
  });

  describe('Service methods', () => {
    describe('createStravaAccountFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStravaAccountFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            stravaUserId: expect.any(Object),
            accessToken: expect.any(Object),
          }),
        );
      });

      it('passing IStravaAccount should create a new form with FormGroup', () => {
        const formGroup = service.createStravaAccountFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            stravaUserId: expect.any(Object),
            accessToken: expect.any(Object),
          }),
        );
      });
    });

    describe('getStravaAccount', () => {
      it('should return NewStravaAccount for default StravaAccount initial value', () => {
        const formGroup = service.createStravaAccountFormGroup(sampleWithNewData);

        const stravaAccount = service.getStravaAccount(formGroup) as any;

        expect(stravaAccount).toMatchObject(sampleWithNewData);
      });

      it('should return NewStravaAccount for empty StravaAccount initial value', () => {
        const formGroup = service.createStravaAccountFormGroup();

        const stravaAccount = service.getStravaAccount(formGroup) as any;

        expect(stravaAccount).toMatchObject({});
      });

      it('should return IStravaAccount', () => {
        const formGroup = service.createStravaAccountFormGroup(sampleWithRequiredData);

        const stravaAccount = service.getStravaAccount(formGroup) as any;

        expect(stravaAccount).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStravaAccount should not enable id FormControl', () => {
        const formGroup = service.createStravaAccountFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStravaAccount should disable id FormControl', () => {
        const formGroup = service.createStravaAccountFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
