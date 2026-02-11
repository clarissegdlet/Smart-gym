import dayjs from 'dayjs/esm';

import { IActivity, NewActivity } from './activity.model';

export const sampleWithRequiredData: IActivity = {
  id: 20972,
  type: 'gens',
  distance: 20722.42,
  duration: 1909.72,
  activityDate: dayjs('2026-02-11T01:34'),
};

export const sampleWithPartialData: IActivity = {
  id: 9423,
  type: 'dehors assez',
  distance: 12771.58,
  duration: 24535.84,
  activityDate: dayjs('2026-02-10T20:50'),
};

export const sampleWithFullData: IActivity = {
  id: 16264,
  type: 'selon partout',
  distance: 28902.11,
  duration: 19749.52,
  activityDate: dayjs('2026-02-11T01:46'),
};

export const sampleWithNewData: NewActivity = {
  type: 'ailleurs entre-temps grandement',
  distance: 18311.88,
  duration: 4551.78,
  activityDate: dayjs('2026-02-10T16:38'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
