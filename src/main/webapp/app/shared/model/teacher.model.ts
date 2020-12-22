import { Moment } from 'moment';
import { IUser } from 'app/shared/model/user.model';
import { IParent } from 'app/shared/model/parent.model';
import { IStudent } from 'app/shared/model/student.model';

export interface ITeacher {
  id?: number;
  profileContent?: string;
  firstName?: string;
  lastName?: string;
  email?: string;
  phone?: string;
  createdBy?: string;
  created?: string;
  updated?: string;
  user?: IUser;
  parent?: IParent;
  student?: IStudent;
}

export const defaultValue: Readonly<ITeacher> = {};
