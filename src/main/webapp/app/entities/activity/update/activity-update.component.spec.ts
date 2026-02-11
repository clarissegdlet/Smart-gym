import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IStravaAccount } from 'app/entities/strava-account/strava-account.model';
import { StravaAccountService } from 'app/entities/strava-account/service/strava-account.service';
import { ActivityService } from '../service/activity.service';
import { IActivity } from '../activity.model';
import { ActivityFormService } from './activity-form.service';

import { ActivityUpdateComponent } from './activity-update.component';

describe('Activity Management Update Component', () => {
  let comp: ActivityUpdateComponent;
  let fixture: ComponentFixture<ActivityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let activityFormService: ActivityFormService;
  let activityService: ActivityService;
  let stravaAccountService: StravaAccountService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ActivityUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ActivityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ActivityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    activityFormService = TestBed.inject(ActivityFormService);
    activityService = TestBed.inject(ActivityService);
    stravaAccountService = TestBed.inject(StravaAccountService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call StravaAccount query and add missing value', () => {
      const activity: IActivity = { id: 30644 };
      const stravaAccount: IStravaAccount = { id: 8856 };
      activity.stravaAccount = stravaAccount;

      const stravaAccountCollection: IStravaAccount[] = [{ id: 8856 }];
      jest.spyOn(stravaAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: stravaAccountCollection })));
      const additionalStravaAccounts = [stravaAccount];
      const expectedCollection: IStravaAccount[] = [...additionalStravaAccounts, ...stravaAccountCollection];
      jest.spyOn(stravaAccountService, 'addStravaAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ activity });
      comp.ngOnInit();

      expect(stravaAccountService.query).toHaveBeenCalled();
      expect(stravaAccountService.addStravaAccountToCollectionIfMissing).toHaveBeenCalledWith(
        stravaAccountCollection,
        ...additionalStravaAccounts.map(expect.objectContaining),
      );
      expect(comp.stravaAccountsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const activity: IActivity = { id: 30644 };
      const stravaAccount: IStravaAccount = { id: 8856 };
      activity.stravaAccount = stravaAccount;

      activatedRoute.data = of({ activity });
      comp.ngOnInit();

      expect(comp.stravaAccountsSharedCollection).toContainEqual(stravaAccount);
      expect(comp.activity).toEqual(activity);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IActivity>>();
      const activity = { id: 3219 };
      jest.spyOn(activityFormService, 'getActivity').mockReturnValue(activity);
      jest.spyOn(activityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ activity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: activity }));
      saveSubject.complete();

      // THEN
      expect(activityFormService.getActivity).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(activityService.update).toHaveBeenCalledWith(expect.objectContaining(activity));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IActivity>>();
      const activity = { id: 3219 };
      jest.spyOn(activityFormService, 'getActivity').mockReturnValue({ id: null });
      jest.spyOn(activityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ activity: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: activity }));
      saveSubject.complete();

      // THEN
      expect(activityFormService.getActivity).toHaveBeenCalled();
      expect(activityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IActivity>>();
      const activity = { id: 3219 };
      jest.spyOn(activityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ activity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(activityService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareStravaAccount', () => {
      it('should forward to stravaAccountService', () => {
        const entity = { id: 8856 };
        const entity2 = { id: 6513 };
        jest.spyOn(stravaAccountService, 'compareStravaAccount');
        comp.compareStravaAccount(entity, entity2);
        expect(stravaAccountService.compareStravaAccount).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
