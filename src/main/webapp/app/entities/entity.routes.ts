import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'member',
    data: { pageTitle: 'Members' },
    loadChildren: () => import('./member/member.routes'),
  },
  {
    path: 'class-session',
    data: { pageTitle: 'ClassSessions' },
    loadChildren: () => import('./class-session/class-session.routes'),
  },
  {
    path: 'booking',
    data: { pageTitle: 'Bookings' },
    loadChildren: () => import('./booking/booking.routes'),
  },
  {
    path: 'strava-account',
    data: { pageTitle: 'StravaAccounts' },
    loadChildren: () => import('./strava-account/strava-account.routes'),
  },
  {
    path: 'activity',
    data: { pageTitle: 'Activities' },
    loadChildren: () => import('./activity/activity.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
