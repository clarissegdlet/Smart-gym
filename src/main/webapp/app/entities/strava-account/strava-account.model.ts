export interface IStravaAccount {
  id: number;
  stravaUserId?: string | null;
  accessToken?: string | null;
}

export type NewStravaAccount = Omit<IStravaAccount, 'id'> & { id: null };
