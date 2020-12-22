import { Moment } from 'moment';
import { IUser } from 'app/shared/model/user.model';

export interface INotification {
  id?: number;
  type?: string;
  channel?: string;
  recipient?: string;
  sendDate?: string;
  user?: IUser;
}

export const defaultValue: Readonly<INotification> = {};
