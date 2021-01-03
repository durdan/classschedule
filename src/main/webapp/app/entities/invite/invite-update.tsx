import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IInviteStatus } from 'app/shared/model/invite-status.model';
import { getEntities as getInviteStatuses } from 'app/entities/invite-status/invite-status.reducer';
import { getEntity, updateEntity, createEntity, reset } from './invite.reducer';
import { IInvite } from 'app/shared/model/invite.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IInviteUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const InviteUpdate = (props: IInviteUpdateProps) => {
  const [invitestatusId, setInvitestatusId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { inviteEntity, inviteStatuses, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/invite' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getInviteStatuses();
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
        ...inviteEntity,
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
          <h2 id="classscheduleApp.invite.home.createOrEditLabel">
            <Translate contentKey="classscheduleApp.invite.home.createOrEditLabel">Create or edit a Invite</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : inviteEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="invite-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="invite-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="requestedUserIdLabel" for="invite-requestedUserId">
                  <Translate contentKey="classscheduleApp.invite.requestedUserId">Requested User Id</Translate>
                </Label>
                <AvField id="invite-requestedUserId" type="text" name="requestedUserId" />
              </AvGroup>
              <AvGroup>
                <Label id="inviteCodeLabel" for="invite-inviteCode">
                  <Translate contentKey="classscheduleApp.invite.inviteCode">Invite Code</Translate>
                </Label>
                <AvField id="invite-inviteCode" type="text" name="inviteCode" />
              </AvGroup>
              <AvGroup>
                <Label id="invitedUserIdLabel" for="invite-invitedUserId">
                  <Translate contentKey="classscheduleApp.invite.invitedUserId">Invited User Id</Translate>
                </Label>
                <AvField id="invite-invitedUserId" type="text" name="invitedUserId" />
              </AvGroup>
              <AvGroup>
                <Label id="createdByLabel" for="invite-createdBy">
                  <Translate contentKey="classscheduleApp.invite.createdBy">Created By</Translate>
                </Label>
                <AvField id="invite-createdBy" type="text" name="createdBy" />
              </AvGroup>
              <AvGroup>
                <Label id="createdLabel" for="invite-created">
                  <Translate contentKey="classscheduleApp.invite.created">Created</Translate>
                </Label>
                <AvInput
                  id="invite-created"
                  type="datetime-local"
                  className="form-control"
                  name="created"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.inviteEntity.created)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedLabel" for="invite-updated">
                  <Translate contentKey="classscheduleApp.invite.updated">Updated</Translate>
                </Label>
                <AvInput
                  id="invite-updated"
                  type="datetime-local"
                  className="form-control"
                  name="updated"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.inviteEntity.updated)}
                />
              </AvGroup>
              <AvGroup>
                <Label for="invite-invitestatus">
                  <Translate contentKey="classscheduleApp.invite.invitestatus">Invitestatus</Translate>
                </Label>
                <AvInput id="invite-invitestatus" type="select" className="form-control" name="invitestatus.id">
                  <option value="" key="0" />
                  {inviteStatuses
                    ? inviteStatuses.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.status}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/invite" replace color="info">
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
  inviteStatuses: storeState.inviteStatus.entities,
  inviteEntity: storeState.invite.entity,
  loading: storeState.invite.loading,
  updating: storeState.invite.updating,
  updateSuccess: storeState.invite.updateSuccess,
});

const mapDispatchToProps = {
  getInviteStatuses,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(InviteUpdate);
