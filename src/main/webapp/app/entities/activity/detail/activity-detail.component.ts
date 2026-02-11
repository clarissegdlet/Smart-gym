import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IActivity } from '../activity.model';

@Component({
  selector: 'jhi-activity-detail',
  templateUrl: './activity-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class ActivityDetailComponent {
  activity = input<IActivity | null>(null);

  previousState(): void {
    window.history.back();
  }
}
