import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IStudent } from 'app/shared/model/student.model';
import { getEntities as getStudents } from 'app/entities/student/student.reducer';
import { ITeacher } from 'app/shared/model/teacher.model';
import { getEntities as getTeachers } from 'app/entities/teacher/teacher.reducer';
import { IParent } from 'app/shared/model/parent.model';
import { getEntities as getParents } from 'app/entities/parent/parent.reducer';
import { ICourse } from 'app/shared/model/course.model';
import { getEntities as getCourses } from 'app/entities/course/course.reducer';
import { getEntity, updateEntity, createEntity, reset } from './class-schedule.reducer';
import { IClassSchedule } from 'app/shared/model/class-schedule.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IClassScheduleUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ClassScheduleUpdate = (props: IClassScheduleUpdateProps) => {
  const [studentId, setStudentId] = useState('0');
  const [teacherId, setTeacherId] = useState('0');
  const [parentId, setParentId] = useState('0');
  const [courseId, setCourseId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { classScheduleEntity, students, teachers, parents, courses, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/class-schedule' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getStudents();
    props.getTeachers();
    props.getParents();
    props.getCourses();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.created = convertDateTimeToServer(values.created);
    values.schedule = convertDateTimeToServer(values.schedule);
    values.updated = convertDateTimeToServer(values.updated);

    if (errors.length === 0) {
      const entity = {
        ...classScheduleEntity,
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
          <h2 id="classscheduleApp.classSchedule.home.createOrEditLabel">
            <Translate contentKey="classscheduleApp.classSchedule.home.createOrEditLabel">Create or edit a ClassSchedule</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : classScheduleEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="class-schedule-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="class-schedule-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="class-schedule-name">
                  <Translate contentKey="classscheduleApp.classSchedule.name">Name</Translate>
                </Label>
                <AvField id="class-schedule-name" type="text" name="name" />
              </AvGroup>
              <AvGroup>
                <Label id="createdLabel" for="class-schedule-created">
                  <Translate contentKey="classscheduleApp.classSchedule.created">Created</Translate>
                </Label>
                <AvInput
                  id="class-schedule-created"
                  type="datetime-local"
                  className="form-control"
                  name="created"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.classScheduleEntity.created)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="scheduleLabel" for="class-schedule-schedule">
                  <Translate contentKey="classscheduleApp.classSchedule.schedule">Schedule</Translate>
                </Label>
                <AvInput
                  id="class-schedule-schedule"
                  type="datetime-local"
                  className="form-control"
                  name="schedule"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.classScheduleEntity.schedule)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedLabel" for="class-schedule-updated">
                  <Translate contentKey="classscheduleApp.classSchedule.updated">Updated</Translate>
                </Label>
                <AvInput
                  id="class-schedule-updated"
                  type="datetime-local"
                  className="form-control"
                  name="updated"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.classScheduleEntity.updated)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="createdByLabel" for="class-schedule-createdBy">
                  <Translate contentKey="classscheduleApp.classSchedule.createdBy">Created By</Translate>
                </Label>
                <AvField id="class-schedule-createdBy" type="text" name="createdBy" />
              </AvGroup>
              <AvGroup>
                <Label id="updatedByLabel" for="class-schedule-updatedBy">
                  <Translate contentKey="classscheduleApp.classSchedule.updatedBy">Updated By</Translate>
                </Label>
                <AvField id="class-schedule-updatedBy" type="text" name="updatedBy" />
              </AvGroup>
              <AvGroup>
                <Label id="confirmedByStudentLabel" for="class-schedule-confirmedByStudent">
                  <Translate contentKey="classscheduleApp.classSchedule.confirmedByStudent">Confirmed By Student</Translate>
                </Label>
                <AvField id="class-schedule-confirmedByStudent" type="text" name="confirmedByStudent" />
              </AvGroup>
              <AvGroup>
                <Label id="confirmedByTeacherLabel" for="class-schedule-confirmedByTeacher">
                  <Translate contentKey="classscheduleApp.classSchedule.confirmedByTeacher">Confirmed By Teacher</Translate>
                </Label>
                <AvField id="class-schedule-confirmedByTeacher" type="text" name="confirmedByTeacher" />
              </AvGroup>
              <AvGroup>
                <Label id="commentLabel" for="class-schedule-comment">
                  <Translate contentKey="classscheduleApp.classSchedule.comment">Comment</Translate>
                </Label>
                <AvField id="class-schedule-comment" type="text" name="comment" />
              </AvGroup>
              <AvGroup check>
                <Label id="paymentLabel">
                  <AvInput id="class-schedule-payment" type="checkbox" className="form-check-input" name="payment" />
                  <Translate contentKey="classscheduleApp.classSchedule.payment">Payment</Translate>
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="confirmedLabel">
                  <AvInput id="class-schedule-confirmed" type="checkbox" className="form-check-input" name="confirmed" />
                  <Translate contentKey="classscheduleApp.classSchedule.confirmed">Confirmed</Translate>
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="rescheduledLabel">
                  <AvInput id="class-schedule-rescheduled" type="checkbox" className="form-check-input" name="rescheduled" />
                  <Translate contentKey="classscheduleApp.classSchedule.rescheduled">Rescheduled</Translate>
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="connectedLabel">
                  <AvInput id="class-schedule-connected" type="checkbox" className="form-check-input" name="connected" />
                  <Translate contentKey="classscheduleApp.classSchedule.connected">Connected</Translate>
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="reoccurringLabel">
                  <AvInput id="class-schedule-reoccurring" type="checkbox" className="form-check-input" name="reoccurring" />
                  <Translate contentKey="classscheduleApp.classSchedule.reoccurring">Reoccurring</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="reoccurringTypeLabel" for="class-schedule-reoccurringType">
                  <Translate contentKey="classscheduleApp.classSchedule.reoccurringType">Reoccurring Type</Translate>
                </Label>
                <AvField id="class-schedule-reoccurringType" type="text" name="reoccurringType" />
              </AvGroup>
              <AvGroup>
                <Label for="class-schedule-student">
                  <Translate contentKey="classscheduleApp.classSchedule.student">Student</Translate>
                </Label>
                <AvInput id="class-schedule-student" type="select" className="form-control" name="student.id">
                  <option value="" key="0" />
                  {students
                    ? students.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="class-schedule-teacher">
                  <Translate contentKey="classscheduleApp.classSchedule.teacher">Teacher</Translate>
                </Label>
                <AvInput id="class-schedule-teacher" type="select" className="form-control" name="teacher.id">
                  <option value="" key="0" />
                  {teachers
                    ? teachers.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="class-schedule-parent">
                  <Translate contentKey="classscheduleApp.classSchedule.parent">Parent</Translate>
                </Label>
                <AvInput id="class-schedule-parent" type="select" className="form-control" name="parent.id">
                  <option value="" key="0" />
                  {parents
                    ? parents.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="class-schedule-course">
                  <Translate contentKey="classscheduleApp.classSchedule.course">Course</Translate>
                </Label>
                <AvInput id="class-schedule-course" type="select" className="form-control" name="course.id">
                  <option value="" key="0" />
                  {courses
                    ? courses.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.name}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/class-schedule" replace color="info">
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
  students: storeState.student.entities,
  teachers: storeState.teacher.entities,
  parents: storeState.parent.entities,
  courses: storeState.course.entities,
  classScheduleEntity: storeState.classSchedule.entity,
  loading: storeState.classSchedule.loading,
  updating: storeState.classSchedule.updating,
  updateSuccess: storeState.classSchedule.updateSuccess,
});

const mapDispatchToProps = {
  getStudents,
  getTeachers,
  getParents,
  getCourses,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ClassScheduleUpdate);
