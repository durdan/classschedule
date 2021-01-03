import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RequestStatus from './request-status';
import RequestStatusDetail from './request-status-detail';
import RequestStatusUpdate from './request-status-update';
import RequestStatusDeleteDialog from './request-status-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RequestStatusUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RequestStatusUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RequestStatusDetail} />
      <ErrorBoundaryRoute path={match.url} component={RequestStatus} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RequestStatusDeleteDialog} />
  </>
);

export default Routes;
