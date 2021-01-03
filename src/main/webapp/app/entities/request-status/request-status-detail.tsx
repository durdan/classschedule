import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './request-status.reducer';
import { IRequestStatus } from 'app/shared/model/request-status.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRequestStatusDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RequestStatusDetail = (props: IRequestStatusDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { requestStatusEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="classscheduleApp.requestStatus.detail.title">RequestStatus</Translate> [<b>{requestStatusEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="statusCode">
              <Translate contentKey="classscheduleApp.requestStatus.statusCode">Status Code</Translate>
            </span>
          </dt>
          <dd>{requestStatusEntity.statusCode}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="classscheduleApp.requestStatus.status">Status</Translate>
            </span>
          </dt>
          <dd>{requestStatusEntity.status}</dd>
        </dl>
        <Button tag={Link} to="/request-status" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/request-status/${requestStatusEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ requestStatus }: IRootState) => ({
  requestStatusEntity: requestStatus.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RequestStatusDetail);
