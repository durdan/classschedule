import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './parent.reducer';
import { IParent } from 'app/shared/model/parent.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IParentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ParentUpdate = (props: IParentUpdateProps) => {
  const [userId, setUserId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { parentEntity, users, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/parent' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

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
        ...parentEntity,
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
          <h2 id="classscheduleApp.parent.home.createOrEditLabel">
            <Translate contentKey="classscheduleApp.parent.home.createOrEditLabel">Create or edit a Parent</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : parentEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="parent-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="parent-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="firstNameLabel" for="parent-firstName">
                  <Translate contentKey="classscheduleApp.parent.firstName">First Name</Translate>
                </Label>
                <AvField id="parent-firstName" type="text" name="firstName" />
              </AvGroup>
              <AvGroup>
                <Label id="lastNameLabel" for="parent-lastName">
                  <Translate contentKey="classscheduleApp.parent.lastName">Last Name</Translate>
                </Label>
                <AvField id="parent-lastName" type="text" name="lastName" />
              </AvGroup>
              <AvGroup>
                <Label id="emailLabel" for="parent-email">
                  <Translate contentKey="classscheduleApp.parent.email">Email</Translate>
                </Label>
                <AvField id="parent-email" type="text" name="email" />
              </AvGroup>
              <AvGroup>
                <Label id="phoneLabel" for="parent-phone">
                  <Translate contentKey="classscheduleApp.parent.phone">Phone</Translate>
                </Label>
                <AvField id="parent-phone" type="text" name="phone" />
              </AvGroup>
              <AvGroup>
                <Label id="createdByLabel" for="parent-createdBy">
                  <Translate contentKey="classscheduleApp.parent.createdBy">Created By</Translate>
                </Label>
                <AvField id="parent-createdBy" type="text" name="createdBy" />
              </AvGroup>
              <AvGroup>
                <Label id="createdLabel" for="parent-created">
                  <Translate contentKey="classscheduleApp.parent.created">Created</Translate>
                </Label>
                <AvInput
                  id="parent-created"
                  type="datetime-local"
                  className="form-control"
                  name="created"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.parentEntity.created)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedLabel" for="parent-updated">
                  <Translate contentKey="classscheduleApp.parent.updated">Updated</Translate>
                </Label>
                <AvInput
                  id="parent-updated"
                  type="datetime-local"
                  className="form-control"
                  name="updated"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.parentEntity.updated)}
                />
              </AvGroup>
              <AvGroup>
                <Label for="parent-user">
                  <Translate contentKey="classscheduleApp.parent.user">User</Translate>
                </Label>
                <AvInput id="parent-user" type="select" className="form-control" name="user.id">
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
              <Button tag={Link} id="cancel-save" to="/parent" replace color="info">
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
  users: storeState.userManagement.users,
  parentEntity: storeState.parent.entity,
  loading: storeState.parent.loading,
  updating: storeState.parent.updating,
  updateSuccess: storeState.parent.updateSuccess,
});

const mapDispatchToProps = {
  getUsers,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ParentUpdate);
