import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IStravaAccount } from '../strava-account.model';
import { StravaAccountService } from '../service/strava-account.service';

@Component({
  templateUrl: './strava-account-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class StravaAccountDeleteDialogComponent {
  stravaAccount?: IStravaAccount;

  protected stravaAccountService = inject(StravaAccountService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.stravaAccountService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
