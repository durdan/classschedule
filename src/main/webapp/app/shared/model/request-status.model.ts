export interface IRequestStatus {
  id?: number;
  statusCode?: number;
  status?: string;
}

export const defaultValue: Readonly<IRequestStatus> = {};
