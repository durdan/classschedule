import { IUser } from 'app/shared/model/user.model';

export interface IUserExtra {
  id?: number;
  userType?: string;
  user?: IUser;
}

export const defaultValue: Readonly<IUserExtra> = {};
