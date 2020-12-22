import { Moment } from 'moment';
import { IUser } from 'app/shared/model/user.model';

export interface IParent {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  phone?: string;
  createdBy?: string;
  created?: string;
  updated?: string;
  user?: IUser;
}

export const defaultValue: Readonly<IParent> = {};
