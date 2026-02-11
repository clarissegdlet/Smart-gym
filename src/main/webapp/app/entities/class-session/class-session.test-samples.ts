import dayjs from 'dayjs/esm';

import { IClassSession, NewClassSession } from './class-session.model';

export const sampleWithRequiredData: IClassSession = {
  id: 29713,
  title: 'sitôt',
  dateTime: dayjs('2026-02-11T04:26'),
  capacity: 3853,
  status: 'CANCELLED',
};

export const sampleWithPartialData: IClassSession = {
  id: 1675,
  title: 'équipe puisque',
  dateTime: dayjs('2026-02-10T12:31'),
  capacity: 21905,
  status: 'PLANNED',
};

export const sampleWithFullData: IClassSession = {
  id: 1615,
  title: 'biathlète jeune enfant',
  description: 'snob',
  dateTime: dayjs('2026-02-11T05:57'),
  capacity: 4373,
  status: 'CANCELLED',
};

export const sampleWithNewData: NewClassSession = {
  title: 'vlan bè',
  dateTime: dayjs('2026-02-10T19:05'),
  capacity: 3717,
  status: 'CANCELLED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
