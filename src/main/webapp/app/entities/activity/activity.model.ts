import dayjs from 'dayjs/esm';
import { IStravaAccount } from 'app/entities/strava-account/strava-account.model';

export interface IActivity {
  id: number;
  type?: string | null;
  distance?: number | null;
  duration?: number | null;
  activityDate?: dayjs.Dayjs | null;
  stravaAccount?: Pick<IStravaAccount, 'id'> | null;
}

export type NewActivity = Omit<IActivity, 'id'> & { id: null };
