import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './invite.reducer';
import { IInvite } from 'app/shared/model/invite.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IInviteDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const InviteDetail = (props: IInviteDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { inviteEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="classscheduleApp.invite.detail.title">Invite</Translate> [<b>{inviteEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="requestedUserId">
              <Translate contentKey="classscheduleApp.invite.requestedUserId">Requested User Id</Translate>
            </span>
          </dt>
          <dd>{inviteEntity.requestedUserId}</dd>
          <dt>
            <span id="invitedUserId">
              <Translate contentKey="classscheduleApp.invite.invitedUserId">Invited User Id</Translate>
            </span>
          </dt>
          <dd>{inviteEntity.invitedUserId}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="classscheduleApp.invite.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{inviteEntity.createdBy}</dd>
          <dt>
            <span id="created">
              <Translate contentKey="classscheduleApp.invite.created">Created</Translate>
            </span>
          </dt>
          <dd>{inviteEntity.created ? <TextFormat value={inviteEntity.created} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updated">
              <Translate contentKey="classscheduleApp.invite.updated">Updated</Translate>
            </span>
          </dt>
          <dd>{inviteEntity.updated ? <TextFormat value={inviteEntity.updated} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="classscheduleApp.invite.invitestatus">Invitestatus</Translate>
          </dt>
          <dd>{inviteEntity.invitestatus ? inviteEntity.invitestatus.status : ''}</dd>
        </dl>
        <Button tag={Link} to="/invite" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/invite/${inviteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ invite }: IRootState) => ({
  inviteEntity: invite.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(InviteDetail);
