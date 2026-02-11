import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import StravaAccountResolve from './route/strava-account-routing-resolve.service';

const stravaAccountRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/strava-account.component').then(m => m.StravaAccountComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/strava-account-detail.component').then(m => m.StravaAccountDetailComponent),
    resolve: {
      stravaAccount: StravaAccountResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/strava-account-update.component').then(m => m.StravaAccountUpdateComponent),
    resolve: {
      stravaAccount: StravaAccountResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/strava-account-update.component').then(m => m.StravaAccountUpdateComponent),
    resolve: {
      stravaAccount: StravaAccountResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default stravaAccountRoute;
