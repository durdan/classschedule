import { Moment } from 'moment';
import { IInviteStatus } from 'app/shared/model/invite-status.model';

export interface IInvite {
  id?: number;
  requestedUserId?: string;
  invitedUserId?: string;
  createdBy?: string;
  created?: string;
  updated?: string;
  invitestatus?: IInviteStatus;
}

export const defaultValue: Readonly<IInvite> = {};
