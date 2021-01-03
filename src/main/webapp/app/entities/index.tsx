import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Student from './student';
import Parent from './parent';
import Teacher from './teacher';
import Course from './course';
import Invite from './invite';
import InviteStatus from './invite-status';
import ClassSchedule from './class-schedule';
import Notification from './notification';
import UserExtra from './user-extra';
import RequestTask from './request-task';
import RequestStatus from './request-status';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}student`} component={Student} />
      <ErrorBoundaryRoute path={`${match.url}parent`} component={Parent} />
      <ErrorBoundaryRoute path={`${match.url}teacher`} component={Teacher} />
      <ErrorBoundaryRoute path={`${match.url}course`} component={Course} />
      <ErrorBoundaryRoute path={`${match.url}invite`} component={Invite} />
      <ErrorBoundaryRoute path={`${match.url}invite-status`} component={InviteStatus} />
      <ErrorBoundaryRoute path={`${match.url}class-schedule`} component={ClassSchedule} />
      <ErrorBoundaryRoute path={`${match.url}notification`} component={Notification} />
      <ErrorBoundaryRoute path={`${match.url}user-extra`} component={UserExtra} />
      <ErrorBoundaryRoute path={`${match.url}request-task`} component={RequestTask} />
      <ErrorBoundaryRoute path={`${match.url}request-status`} component={RequestStatus} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
