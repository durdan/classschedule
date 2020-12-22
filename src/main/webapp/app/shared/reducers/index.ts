import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import student, {
  StudentState
} from 'app/entities/student/student.reducer';
// prettier-ignore
import parent, {
  ParentState
} from 'app/entities/parent/parent.reducer';
// prettier-ignore
import teacher, {
  TeacherState
} from 'app/entities/teacher/teacher.reducer';
// prettier-ignore
import course, {
  CourseState
} from 'app/entities/course/course.reducer';
// prettier-ignore
import invite, {
  InviteState
} from 'app/entities/invite/invite.reducer';
// prettier-ignore
import inviteStatus, {
  InviteStatusState
} from 'app/entities/invite-status/invite-status.reducer';
// prettier-ignore
import classSchedule, {
  ClassScheduleState
} from 'app/entities/class-schedule/class-schedule.reducer';
// prettier-ignore
import notification, {
  NotificationState
} from 'app/entities/notification/notification.reducer';
// prettier-ignore
import userExtra, {
  UserExtraState
} from 'app/entities/user-extra/user-extra.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly locale: LocaleState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly student: StudentState;
  readonly parent: ParentState;
  readonly teacher: TeacherState;
  readonly course: CourseState;
  readonly invite: InviteState;
  readonly inviteStatus: InviteStatusState;
  readonly classSchedule: ClassScheduleState;
  readonly notification: NotificationState;
  readonly userExtra: UserExtraState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  student,
  parent,
  teacher,
  course,
  invite,
  inviteStatus,
  classSchedule,
  notification,
  userExtra,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
});

export default rootReducer;
