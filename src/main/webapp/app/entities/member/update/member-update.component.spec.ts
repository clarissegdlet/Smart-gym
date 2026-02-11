import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IStravaAccount } from 'app/entities/strava-account/strava-account.model';
import { StravaAccountService } from 'app/entities/strava-account/service/strava-account.service';
import { MemberService } from '../service/member.service';
import { IMember } from '../member.model';
import { MemberFormService } from './member-form.service';

import { MemberUpdateComponent } from './member-update.component';

describe('Member Management Update Component', () => {
  let comp: MemberUpdateComponent;
  let fixture: ComponentFixture<MemberUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let memberFormService: MemberFormService;
  let memberService: MemberService;
  let stravaAccountService: StravaAccountService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MemberUpdateComponent],
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
      .overrideTemplate(MemberUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MemberUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    memberFormService = TestBed.inject(MemberFormService);
    memberService = TestBed.inject(MemberService);
    stravaAccountService = TestBed.inject(StravaAccountService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call stravaAccount query and add missing value', () => {
      const member: IMember = { id: 30790 };
      const stravaAccount: IStravaAccount = { id: 8856 };
      member.stravaAccount = stravaAccount;

      const stravaAccountCollection: IStravaAccount[] = [{ id: 8856 }];
      jest.spyOn(stravaAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: stravaAccountCollection })));
      const expectedCollection: IStravaAccount[] = [stravaAccount, ...stravaAccountCollection];
      jest.spyOn(stravaAccountService, 'addStravaAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ member });
      comp.ngOnInit();

      expect(stravaAccountService.query).toHaveBeenCalled();
      expect(stravaAccountService.addStravaAccountToCollectionIfMissing).toHaveBeenCalledWith(stravaAccountCollection, stravaAccount);
      expect(comp.stravaAccountsCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const member: IMember = { id: 30790 };
      const stravaAccount: IStravaAccount = { id: 8856 };
      member.stravaAccount = stravaAccount;

      activatedRoute.data = of({ member });
      comp.ngOnInit();

      expect(comp.stravaAccountsCollection).toContainEqual(stravaAccount);
      expect(comp.member).toEqual(member);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMember>>();
      const member = { id: 17514 };
      jest.spyOn(memberFormService, 'getMember').mockReturnValue(member);
      jest.spyOn(memberService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ member });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: member }));
      saveSubject.complete();

      // THEN
      expect(memberFormService.getMember).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(memberService.update).toHaveBeenCalledWith(expect.objectContaining(member));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMember>>();
      const member = { id: 17514 };
      jest.spyOn(memberFormService, 'getMember').mockReturnValue({ id: null });
      jest.spyOn(memberService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ member: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: member }));
      saveSubject.complete();

      // THEN
      expect(memberFormService.getMember).toHaveBeenCalled();
      expect(memberService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMember>>();
      const member = { id: 17514 };
      jest.spyOn(memberService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ member });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(memberService.update).toHaveBeenCalled();
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
