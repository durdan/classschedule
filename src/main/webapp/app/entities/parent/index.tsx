import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Parent from './parent';
import ParentDetail from './parent-detail';
import ParentUpdate from './parent-update';
import ParentDeleteDialog from './parent-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ParentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ParentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ParentDetail} />
      <ErrorBoundaryRoute path={match.url} component={Parent} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ParentDeleteDialog} />
  </>
);

export default Routes;
