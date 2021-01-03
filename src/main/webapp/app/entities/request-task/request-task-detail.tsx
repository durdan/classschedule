import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './request-task.reducer';
import { IRequestTask } from 'app/shared/model/request-task.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRequestTaskDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RequestTaskDetail = (props: IRequestTaskDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { requestTaskEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="classscheduleApp.requestTask.detail.title">RequestTask</Translate> [<b>{requestTaskEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="requestedUserId">
              <Translate contentKey="classscheduleApp.requestTask.requestedUserId">Requested User Id</Translate>
            </span>
          </dt>
          <dd>{requestTaskEntity.requestedUserId}</dd>
          <dt>
            <span id="requestCode">
              <Translate contentKey="classscheduleApp.requestTask.requestCode">Request Code</Translate>
            </span>
          </dt>
          <dd>{requestTaskEntity.requestCode}</dd>
          <dt>
            <span id="requestType">
              <Translate contentKey="classscheduleApp.requestTask.requestType">Request Type</Translate>
            </span>
          </dt>
          <dd>{requestTaskEntity.requestType}</dd>
          <dt>
            <span id="requiredActionFromUserId">
              <Translate contentKey="classscheduleApp.requestTask.requiredActionFromUserId">Required Action From User Id</Translate>
            </span>
          </dt>
          <dd>{requestTaskEntity.requiredActionFromUserId}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="classscheduleApp.requestTask.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{requestTaskEntity.createdBy}</dd>
          <dt>
            <span id="created">
              <Translate contentKey="classscheduleApp.requestTask.created">Created</Translate>
            </span>
          </dt>
          <dd>
            {requestTaskEntity.created ? <TextFormat value={requestTaskEntity.created} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updated">
              <Translate contentKey="classscheduleApp.requestTask.updated">Updated</Translate>
            </span>
          </dt>
          <dd>
            {requestTaskEntity.updated ? <TextFormat value={requestTaskEntity.updated} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="classscheduleApp.requestTask.requestStatus">Request Status</Translate>
          </dt>
          <dd>{requestTaskEntity.requestStatus ? requestTaskEntity.requestStatus.status : ''}</dd>
          <dt>
            <Translate contentKey="classscheduleApp.requestTask.user">User</Translate>
          </dt>
          <dd>{requestTaskEntity.user ? requestTaskEntity.user.email : ''}</dd>
        </dl>
        <Button tag={Link} to="/request-task" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/request-task/${requestTaskEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ requestTask }: IRootState) => ({
  requestTaskEntity: requestTask.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RequestTaskDetail);
