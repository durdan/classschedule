import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './request-status.reducer';
import { IRequestStatus } from 'app/shared/model/request-status.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRequestStatusUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RequestStatusUpdate = (props: IRequestStatusUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { requestStatusEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/request-status' + props.location.search);
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
        ...requestStatusEntity,
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
          <h2 id="classscheduleApp.requestStatus.home.createOrEditLabel">
            <Translate contentKey="classscheduleApp.requestStatus.home.createOrEditLabel">Create or edit a RequestStatus</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : requestStatusEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="request-status-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="request-status-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="statusCodeLabel" for="request-status-statusCode">
                  <Translate contentKey="classscheduleApp.requestStatus.statusCode">Status Code</Translate>
                </Label>
                <AvField id="request-status-statusCode" type="string" className="form-control" name="statusCode" />
              </AvGroup>
              <AvGroup>
                <Label id="statusLabel" for="request-status-status">
                  <Translate contentKey="classscheduleApp.requestStatus.status">Status</Translate>
                </Label>
                <AvField id="request-status-status" type="text" name="status" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/request-status" replace color="info">
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
  requestStatusEntity: storeState.requestStatus.entity,
  loading: storeState.requestStatus.loading,
  updating: storeState.requestStatus.updating,
  updateSuccess: storeState.requestStatus.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RequestStatusUpdate);
