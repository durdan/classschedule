import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RequestTask from './request-task';
import RequestTaskDetail from './request-task-detail';
import RequestTaskUpdate from './request-task-update';
import RequestTaskDeleteDialog from './request-task-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RequestTaskUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RequestTaskUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RequestTaskDetail} />
      <ErrorBoundaryRoute path={match.url} component={RequestTask} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RequestTaskDeleteDialog} />
  </>
);

export default Routes;
