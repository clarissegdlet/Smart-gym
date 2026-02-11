import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { StravaAccountService } from '../service/strava-account.service';
import { IStravaAccount } from '../strava-account.model';
import { StravaAccountFormService } from './strava-account-form.service';

import { StravaAccountUpdateComponent } from './strava-account-update.component';

describe('StravaAccount Management Update Component', () => {
  let comp: StravaAccountUpdateComponent;
  let fixture: ComponentFixture<StravaAccountUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let stravaAccountFormService: StravaAccountFormService;
  let stravaAccountService: StravaAccountService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [StravaAccountUpdateComponent],
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
      .overrideTemplate(StravaAccountUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StravaAccountUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    stravaAccountFormService = TestBed.inject(StravaAccountFormService);
    stravaAccountService = TestBed.inject(StravaAccountService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const stravaAccount: IStravaAccount = { id: 6513 };

      activatedRoute.data = of({ stravaAccount });
      comp.ngOnInit();

      expect(comp.stravaAccount).toEqual(stravaAccount);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStravaAccount>>();
      const stravaAccount = { id: 8856 };
      jest.spyOn(stravaAccountFormService, 'getStravaAccount').mockReturnValue(stravaAccount);
      jest.spyOn(stravaAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stravaAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stravaAccount }));
      saveSubject.complete();

      // THEN
      expect(stravaAccountFormService.getStravaAccount).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(stravaAccountService.update).toHaveBeenCalledWith(expect.objectContaining(stravaAccount));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStravaAccount>>();
      const stravaAccount = { id: 8856 };
      jest.spyOn(stravaAccountFormService, 'getStravaAccount').mockReturnValue({ id: null });
      jest.spyOn(stravaAccountService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stravaAccount: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stravaAccount }));
      saveSubject.complete();

      // THEN
      expect(stravaAccountFormService.getStravaAccount).toHaveBeenCalled();
      expect(stravaAccountService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStravaAccount>>();
      const stravaAccount = { id: 8856 };
      jest.spyOn(stravaAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stravaAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(stravaAccountService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
