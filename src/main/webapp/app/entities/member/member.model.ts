import { IStravaAccount } from 'app/entities/strava-account/strava-account.model';

export interface IMember {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  stravaAccount?: Pick<IStravaAccount, 'id'> | null;
}

export type NewMember = Omit<IMember, 'id'> & { id: null };
