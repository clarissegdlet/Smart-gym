import dayjs from 'dayjs/esm';

import { IBooking, NewBooking } from './booking.model';

export const sampleWithRequiredData: IBooking = {
  id: 4955,
  bookingDate: dayjs('2026-02-10T15:13'),
  status: 'CONFIRMED',
};

export const sampleWithPartialData: IBooking = {
  id: 76,
  bookingDate: dayjs('2026-02-11T09:28'),
  status: 'CONFIRMED',
};

export const sampleWithFullData: IBooking = {
  id: 28482,
  bookingDate: dayjs('2026-02-11T00:51'),
  status: 'CANCELLED',
};

export const sampleWithNewData: NewBooking = {
  bookingDate: dayjs('2026-02-10T13:41'),
  status: 'CONFIRMED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
