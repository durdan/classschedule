import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import InviteStatus from './invite-status';
import InviteStatusDetail from './invite-status-detail';
import InviteStatusUpdate from './invite-status-update';
import InviteStatusDeleteDialog from './invite-status-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={InviteStatusUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={InviteStatusUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={InviteStatusDetail} />
      <ErrorBoundaryRoute path={match.url} component={InviteStatus} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={InviteStatusDeleteDialog} />
  </>
);

export default Routes;
