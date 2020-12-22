import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './notification.reducer';
import { INotification } from 'app/shared/model/notification.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface INotificationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const NotificationDetail = (props: INotificationDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { notificationEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="classscheduleApp.notification.detail.title">Notification</Translate> [<b>{notificationEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="type">
              <Translate contentKey="classscheduleApp.notification.type">Type</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.type}</dd>
          <dt>
            <span id="channel">
              <Translate contentKey="classscheduleApp.notification.channel">Channel</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.channel}</dd>
          <dt>
            <span id="recipient">
              <Translate contentKey="classscheduleApp.notification.recipient">Recipient</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.recipient}</dd>
          <dt>
            <span id="sendDate">
              <Translate contentKey="classscheduleApp.notification.sendDate">Send Date</Translate>
            </span>
          </dt>
          <dd>
            {notificationEntity.sendDate ? <TextFormat value={notificationEntity.sendDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="classscheduleApp.notification.user">User</Translate>
          </dt>
          <dd>{notificationEntity.user ? notificationEntity.user.email : ''}</dd>
        </dl>
        <Button tag={Link} to="/notification" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/notification/${notificationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ notification }: IRootState) => ({
  notificationEntity: notification.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(NotificationDetail);
