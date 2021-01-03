import { Moment } from 'moment';
import { IRequestStatus } from 'app/shared/model/request-status.model';
import { IUser } from 'app/shared/model/user.model';

export interface IRequestTask {
  id?: number;
  requestedUserId?: string;
  requestCode?: string;
  requestType?: string;
  requiredActionFromUserId?: string;
  createdBy?: string;
  created?: string;
  updated?: string;
  requestStatus?: IRequestStatus;
  user?: IUser;
}

export const defaultValue: Readonly<IRequestTask> = {};
