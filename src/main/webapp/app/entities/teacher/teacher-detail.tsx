import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './teacher.reducer';
import { ITeacher } from 'app/shared/model/teacher.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITeacherDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TeacherDetail = (props: ITeacherDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { teacherEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="classscheduleApp.teacher.detail.title">Teacher</Translate> [<b>{teacherEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="profileContent">
              <Translate contentKey="classscheduleApp.teacher.profileContent">Profile Content</Translate>
            </span>
          </dt>
          <dd>{teacherEntity.profileContent}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="classscheduleApp.teacher.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{teacherEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="classscheduleApp.teacher.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{teacherEntity.lastName}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="classscheduleApp.teacher.email">Email</Translate>
            </span>
          </dt>
          <dd>{teacherEntity.email}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="classscheduleApp.teacher.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{teacherEntity.phone}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="classscheduleApp.teacher.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{teacherEntity.createdBy}</dd>
          <dt>
            <span id="created">
              <Translate contentKey="classscheduleApp.teacher.created">Created</Translate>
            </span>
          </dt>
          <dd>{teacherEntity.created ? <TextFormat value={teacherEntity.created} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updated">
              <Translate contentKey="classscheduleApp.teacher.updated">Updated</Translate>
            </span>
          </dt>
          <dd>{teacherEntity.updated ? <TextFormat value={teacherEntity.updated} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="classscheduleApp.teacher.user">User</Translate>
          </dt>
          <dd>{teacherEntity.user ? teacherEntity.user.email : ''}</dd>
          <dt>
            <Translate contentKey="classscheduleApp.teacher.parent">Parent</Translate>
          </dt>
          <dd>{teacherEntity.parent ? teacherEntity.parent.firstName : ''}</dd>
          <dt>
            <Translate contentKey="classscheduleApp.teacher.student">Student</Translate>
          </dt>
          <dd>{teacherEntity.student ? teacherEntity.student.firstName : ''}</dd>
        </dl>
        <Button tag={Link} to="/teacher" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/teacher/${teacherEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ teacher }: IRootState) => ({
  teacherEntity: teacher.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TeacherDetail);
