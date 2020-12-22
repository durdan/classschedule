import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './invite-status.reducer';
import { IInviteStatus } from 'app/shared/model/invite-status.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IInviteStatusDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const InviteStatusDetail = (props: IInviteStatusDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { inviteStatusEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="classscheduleApp.inviteStatus.detail.title">InviteStatus</Translate> [<b>{inviteStatusEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="statusCode">
              <Translate contentKey="classscheduleApp.inviteStatus.statusCode">Status Code</Translate>
            </span>
          </dt>
          <dd>{inviteStatusEntity.statusCode}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="classscheduleApp.inviteStatus.status">Status</Translate>
            </span>
          </dt>
          <dd>{inviteStatusEntity.status}</dd>
        </dl>
        <Button tag={Link} to="/invite-status" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/invite-status/${inviteStatusEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ inviteStatus }: IRootState) => ({
  inviteStatusEntity: inviteStatus.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(InviteStatusDetail);
