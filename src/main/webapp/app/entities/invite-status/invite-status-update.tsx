import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './invite-status.reducer';
import { IInviteStatus } from 'app/shared/model/invite-status.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IInviteStatusUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const InviteStatusUpdate = (props: IInviteStatusUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { inviteStatusEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/invite-status' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...inviteStatusEntity,
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
          <h2 id="classscheduleApp.inviteStatus.home.createOrEditLabel">
            <Translate contentKey="classscheduleApp.inviteStatus.home.createOrEditLabel">Create or edit a InviteStatus</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : inviteStatusEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="invite-status-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="invite-status-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="statusCodeLabel" for="invite-status-statusCode">
                  <Translate contentKey="classscheduleApp.inviteStatus.statusCode">Status Code</Translate>
                </Label>
                <AvField id="invite-status-statusCode" type="string" className="form-control" name="statusCode" />
              </AvGroup>
              <AvGroup>
                <Label id="statusLabel" for="invite-status-status">
                  <Translate contentKey="classscheduleApp.inviteStatus.status">Status</Translate>
                </Label>
                <AvField id="invite-status-status" type="text" name="status" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/invite-status" replace color="info">
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
  inviteStatusEntity: storeState.inviteStatus.entity,
  loading: storeState.inviteStatus.loading,
  updating: storeState.inviteStatus.updating,
  updateSuccess: storeState.inviteStatus.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(InviteStatusUpdate);
