import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Invite from './invite';
import InviteDetail from './invite-detail';
import InviteUpdate from './invite-update';
import InviteDeleteDialog from './invite-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={InviteUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={InviteUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={InviteDetail} />
      <ErrorBoundaryRoute path={match.url} component={Invite} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={InviteDeleteDialog} />
  </>
);

export default Routes;
