import { Moment } from 'moment';
import { IStudent } from 'app/shared/model/student.model';
import { ITeacher } from 'app/shared/model/teacher.model';
import { IParent } from 'app/shared/model/parent.model';
import { ICourse } from 'app/shared/model/course.model';

export interface IClassSchedule {
  id?: number;
  name?: string;
  created?: string;
  schedule?: string;
  updated?: string;
  createdBy?: string;
  updatedBy?: string;
  confirmedByStudent?: string;
  confirmedByTeacher?: string;
  comment?: string;
  payment?: boolean;
  connected?: boolean;
  reoccurring?: boolean;
  reoccurringType?: string;
  student?: IStudent;
  teacher?: ITeacher;
  parent?: IParent;
  course?: ICourse;
}

export const defaultValue: Readonly<IClassSchedule> = {
  payment: false,
  connected: false,
  reoccurring: false,
};
