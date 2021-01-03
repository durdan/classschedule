import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IRequestStatus } from 'app/shared/model/request-status.model';
import { getEntities as getRequestStatuses } from 'app/entities/request-status/request-status.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './request-task.reducer';
import { IRequestTask } from 'app/shared/model/request-task.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRequestTaskUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RequestTaskUpdate = (props: IRequestTaskUpdateProps) => {
  const [requestStatusId, setRequestStatusId] = useState('0');
  const [userId, setUserId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { requestTaskEntity, requestStatuses, users, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/request-task' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getRequestStatuses();
    props.getUsers();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.created = convertDateTimeToServer(values.created);
    values.updated = convertDateTimeToServer(values.updated);

    if (errors.length === 0) {
      const entity = {
        ...requestTaskEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="classscheduleApp.requestTask.home.createOrEditLabel">
            <Translate contentKey="classscheduleApp.requestTask.home.createOrEditLabel">Create or edit a RequestTask</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : requestTaskEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="request-task-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="request-task-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="requestedUserIdLabel" for="request-task-requestedUserId">
                  <Translate contentKey="classscheduleApp.requestTask.requestedUserId">Requested User Id</Translate>
                </Label>
                <AvField id="request-task-requestedUserId" type="text" name="requestedUserId" />
              </AvGroup>
              <AvGroup>
                <Label id="requestCodeLabel" for="request-task-requestCode">
                  <Translate contentKey="classscheduleApp.requestTask.requestCode">Request Code</Translate>
                </Label>
                <AvField id="request-task-requestCode" type="text" name="requestCode" />
              </AvGroup>
              <AvGroup>
                <Label id="requestTypeLabel" for="request-task-requestType">
                  <Translate contentKey="classscheduleApp.requestTask.requestType">Request Type</Translate>
                </Label>
                <AvField id="request-task-requestType" type="text" name="requestType" />
              </AvGroup>
              <AvGroup>
                <Label id="requiredActionFromUserIdLabel" for="request-task-requiredActionFromUserId">
                  <Translate contentKey="classscheduleApp.requestTask.requiredActionFromUserId">Required Action From User Id</Translate>
                </Label>
                <AvField id="request-task-requiredActionFromUserId" type="text" name="requiredActionFromUserId" />
              </AvGroup>
              <AvGroup>
                <Label id="createdByLabel" for="request-task-createdBy">
                  <Translate contentKey="classscheduleApp.requestTask.createdBy">Created By</Translate>
                </Label>
                <AvField id="request-task-createdBy" type="text" name="createdBy" />
              </AvGroup>
              <AvGroup>
                <Label id="createdLabel" for="request-task-created">
                  <Translate contentKey="classscheduleApp.requestTask.created">Created</Translate>
                </Label>
                <AvInput
                  id="request-task-created"
                  type="datetime-local"
                  className="form-control"
                  name="created"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.requestTaskEntity.created)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedLabel" for="request-task-updated">
                  <Translate contentKey="classscheduleApp.requestTask.updated">Updated</Translate>
                </Label>
                <AvInput
                  id="request-task-updated"
                  type="datetime-local"
                  className="form-control"
                  name="updated"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.requestTaskEntity.updated)}
                />
              </AvGroup>
              <AvGroup>
                <Label for="request-task-requestStatus">
                  <Translate contentKey="classscheduleApp.requestTask.requestStatus">Request Status</Translate>
                </Label>
                <AvInput id="request-task-requestStatus" type="select" className="form-control" name="requestStatus.id">
                  <option value="" key="0" />
                  {requestStatuses
                    ? requestStatuses.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.status}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="request-task-user">
                  <Translate contentKey="classscheduleApp.requestTask.user">User</Translate>
                </Label>
                <AvInput id="request-task-user" type="select" className="form-control" name="user.id">
                  <option value="" key="0" />
                  {users
                    ? users.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.email}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/request-task" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  requestStatuses: storeState.requestStatus.entities,
  users: storeState.userManagement.users,
  requestTaskEntity: storeState.requestTask.entity,
  loading: storeState.requestTask.loading,
  updating: storeState.requestTask.updating,
  updateSuccess: storeState.requestTask.updateSuccess,
});

const mapDispatchToProps = {
  getRequestStatuses,
  getUsers,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RequestTaskUpdate);
