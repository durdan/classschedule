import { Moment } from 'moment';

export interface ICourse {
  id?: number;
  name?: string;
  created?: string;
  updated?: string;
}

export const defaultValue: Readonly<ICourse> = {};
