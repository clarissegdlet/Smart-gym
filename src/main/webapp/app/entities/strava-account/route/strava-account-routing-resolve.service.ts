import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStravaAccount } from '../strava-account.model';
import { StravaAccountService } from '../service/strava-account.service';

const stravaAccountResolve = (route: ActivatedRouteSnapshot): Observable<null | IStravaAccount> => {
  const id = route.params.id;
  if (id) {
    return inject(StravaAccountService)
      .find(id)
      .pipe(
        mergeMap((stravaAccount: HttpResponse<IStravaAccount>) => {
          if (stravaAccount.body) {
            return of(stravaAccount.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default stravaAccountResolve;
