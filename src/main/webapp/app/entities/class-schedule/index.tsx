import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ClassSchedule from './class-schedule';
import ClassScheduleDetail from './class-schedule-detail';
import ClassScheduleUpdate from './class-schedule-update';
import ClassScheduleDeleteDialog from './class-schedule-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ClassScheduleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ClassScheduleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ClassScheduleDetail} />
      <ErrorBoundaryRoute path={match.url} component={ClassSchedule} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ClassScheduleDeleteDialog} />
  </>
);

export default Routes;
