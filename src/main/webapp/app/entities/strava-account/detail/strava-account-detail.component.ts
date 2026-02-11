import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IStravaAccount } from '../strava-account.model';

@Component({
  selector: 'jhi-strava-account-detail',
  templateUrl: './strava-account-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class StravaAccountDetailComponent {
  stravaAccount = input<IStravaAccount | null>(null);

  previousState(): void {
    window.history.back();
  }
}
