import { Moment } from 'moment';
import { IUser } from 'app/shared/model/user.model';
import { IParent } from 'app/shared/model/parent.model';
import { ITeacher } from 'app/shared/model/teacher.model';

export interface IStudent {
  id?: number;
  schoolYear?: string;
  firstName?: string;
  lastName?: string;
  email?: string;
  phone?: string;
  createdBy?: string;
  created?: string;
  updated?: string;
  user?: IUser;
  parent?: IParent;
  teacher?: ITeacher;
}

export const defaultValue: Readonly<IStudent> = {};
