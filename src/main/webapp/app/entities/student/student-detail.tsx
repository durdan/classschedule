import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './student.reducer';
import { IStudent } from 'app/shared/model/student.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IStudentDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const StudentDetail = (props: IStudentDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { studentEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="classscheduleApp.student.detail.title">Student</Translate> [<b>{studentEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="schoolYear">
              <Translate contentKey="classscheduleApp.student.schoolYear">School Year</Translate>
            </span>
          </dt>
          <dd>{studentEntity.schoolYear}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="classscheduleApp.student.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{studentEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="classscheduleApp.student.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{studentEntity.lastName}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="classscheduleApp.student.email">Email</Translate>
            </span>
          </dt>
          <dd>{studentEntity.email}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="classscheduleApp.student.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{studentEntity.phone}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="classscheduleApp.student.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{studentEntity.createdBy}</dd>
          <dt>
            <span id="created">
              <Translate contentKey="classscheduleApp.student.created">Created</Translate>
            </span>
          </dt>
          <dd>{studentEntity.created ? <TextFormat value={studentEntity.created} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updated">
              <Translate contentKey="classscheduleApp.student.updated">Updated</Translate>
            </span>
          </dt>
          <dd>{studentEntity.updated ? <TextFormat value={studentEntity.updated} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="classscheduleApp.student.user">User</Translate>
          </dt>
          <dd>{studentEntity.user ? studentEntity.user.email : ''}</dd>
          <dt>
            <Translate contentKey="classscheduleApp.student.parent">Parent</Translate>
          </dt>
          <dd>{studentEntity.parent ? studentEntity.parent.firstName : ''}</dd>
          <dt>
            <Translate contentKey="classscheduleApp.student.teacher">Teacher</Translate>
          </dt>
          <dd>{studentEntity.teacher ? studentEntity.teacher.firstName : ''}</dd>
        </dl>
        <Button tag={Link} to="/student" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/student/${studentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ student }: IRootState) => ({
  studentEntity: student.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(StudentDetail);
