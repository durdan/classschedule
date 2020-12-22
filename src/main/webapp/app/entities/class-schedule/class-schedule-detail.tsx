import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './class-schedule.reducer';
import { IClassSchedule } from 'app/shared/model/class-schedule.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IClassScheduleDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ClassScheduleDetail = (props: IClassScheduleDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { classScheduleEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="classscheduleApp.classSchedule.detail.title">ClassSchedule</Translate> [<b>{classScheduleEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">
              <Translate contentKey="classscheduleApp.classSchedule.name">Name</Translate>
            </span>
          </dt>
          <dd>{classScheduleEntity.name}</dd>
          <dt>
            <span id="created">
              <Translate contentKey="classscheduleApp.classSchedule.created">Created</Translate>
            </span>
          </dt>
          <dd>
            {classScheduleEntity.created ? <TextFormat value={classScheduleEntity.created} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updated">
              <Translate contentKey="classscheduleApp.classSchedule.updated">Updated</Translate>
            </span>
          </dt>
          <dd>
            {classScheduleEntity.updated ? <TextFormat value={classScheduleEntity.updated} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="classscheduleApp.classSchedule.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{classScheduleEntity.createdBy}</dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="classscheduleApp.classSchedule.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{classScheduleEntity.updatedBy}</dd>
          <dt>
            <span id="confirmedByStudent">
              <Translate contentKey="classscheduleApp.classSchedule.confirmedByStudent">Confirmed By Student</Translate>
            </span>
          </dt>
          <dd>{classScheduleEntity.confirmedByStudent}</dd>
          <dt>
            <span id="confirmedByTeacher">
              <Translate contentKey="classscheduleApp.classSchedule.confirmedByTeacher">Confirmed By Teacher</Translate>
            </span>
          </dt>
          <dd>{classScheduleEntity.confirmedByTeacher}</dd>
          <dt>
            <span id="comment">
              <Translate contentKey="classscheduleApp.classSchedule.comment">Comment</Translate>
            </span>
          </dt>
          <dd>{classScheduleEntity.comment}</dd>
          <dt>
            <span id="connected">
              <Translate contentKey="classscheduleApp.classSchedule.connected">Connected</Translate>
            </span>
          </dt>
          <dd>{classScheduleEntity.connected ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="classscheduleApp.classSchedule.student">Student</Translate>
          </dt>
          <dd>{classScheduleEntity.student ? classScheduleEntity.student.id : ''}</dd>
          <dt>
            <Translate contentKey="classscheduleApp.classSchedule.teacher">Teacher</Translate>
          </dt>
          <dd>{classScheduleEntity.teacher ? classScheduleEntity.teacher.id : ''}</dd>
          <dt>
            <Translate contentKey="classscheduleApp.classSchedule.parent">Parent</Translate>
          </dt>
          <dd>{classScheduleEntity.parent ? classScheduleEntity.parent.id : ''}</dd>
          <dt>
            <Translate contentKey="classscheduleApp.classSchedule.course">Course</Translate>
          </dt>
          <dd>{classScheduleEntity.course ? classScheduleEntity.course.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/class-schedule" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/class-schedule/${classScheduleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ classSchedule }: IRootState) => ({
  classScheduleEntity: classSchedule.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ClassScheduleDetail);
