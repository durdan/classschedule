import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './parent.reducer';
import { IParent } from 'app/shared/model/parent.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IParentDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ParentDetail = (props: IParentDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { parentEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="classscheduleApp.parent.detail.title">Parent</Translate> [<b>{parentEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="firstName">
              <Translate contentKey="classscheduleApp.parent.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{parentEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="classscheduleApp.parent.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{parentEntity.lastName}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="classscheduleApp.parent.email">Email</Translate>
            </span>
          </dt>
          <dd>{parentEntity.email}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="classscheduleApp.parent.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{parentEntity.phone}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="classscheduleApp.parent.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{parentEntity.createdBy}</dd>
          <dt>
            <span id="created">
              <Translate contentKey="classscheduleApp.parent.created">Created</Translate>
            </span>
          </dt>
          <dd>{parentEntity.created ? <TextFormat value={parentEntity.created} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updated">
              <Translate contentKey="classscheduleApp.parent.updated">Updated</Translate>
            </span>
          </dt>
          <dd>{parentEntity.updated ? <TextFormat value={parentEntity.updated} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="classscheduleApp.parent.user">User</Translate>
          </dt>
          <dd>{parentEntity.user ? parentEntity.user.email : ''}</dd>
        </dl>
        <Button tag={Link} to="/parent" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/parent/${parentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ parent }: IRootState) => ({
  parentEntity: parent.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ParentDetail);
