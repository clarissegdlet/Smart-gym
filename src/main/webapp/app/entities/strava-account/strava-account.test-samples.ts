import { IStravaAccount, NewStravaAccount } from './strava-account.model';

export const sampleWithRequiredData: IStravaAccount = {
  id: 20792,
  stravaUserId: 'vlan hypocrite',
  accessToken: 'électorat terriblement demander',
};

export const sampleWithPartialData: IStravaAccount = {
  id: 12629,
  stravaUserId: 'sitôt de manière à ce que',
  accessToken: 'certes',
};

export const sampleWithFullData: IStravaAccount = {
  id: 28289,
  stravaUserId: "à l'exception de au-dessous de oups",
  accessToken: 'soucier',
};

export const sampleWithNewData: NewStravaAccount = {
  stravaUserId: 'moyennant puisque',
  accessToken: 'chut sans',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
