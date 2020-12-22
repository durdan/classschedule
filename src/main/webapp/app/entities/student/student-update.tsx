import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IParent } from 'app/shared/model/parent.model';
import { getEntities as getParents } from 'app/entities/parent/parent.reducer';
import { ITeacher } from 'app/shared/model/teacher.model';
import { getEntities as getTeachers } from 'app/entities/teacher/teacher.reducer';
import { getEntity, updateEntity, createEntity, reset } from './student.reducer';
import { IStudent } from 'app/shared/model/student.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IStudentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const StudentUpdate = (props: IStudentUpdateProps) => {
  const [userId, setUserId] = useState('0');
  const [parentId, setParentId] = useState('0');
  const [teacherId, setTeacherId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { studentEntity, users, parents, teachers, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/student' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getUsers();
    props.getParents();
    props.getTeachers();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.created = convertDateTimeToServer(values.created);
    values.updated = convertDateTimeToServer(values.updated);

    if (errors.length === 0) {
      const entity = {
        ...studentEntity,
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
          <h2 id="classscheduleApp.student.home.createOrEditLabel">
            <Translate contentKey="classscheduleApp.student.home.createOrEditLabel">Create or edit a Student</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : studentEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="student-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="student-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="schoolYearLabel" for="student-schoolYear">
                  <Translate contentKey="classscheduleApp.student.schoolYear">School Year</Translate>
                </Label>
                <AvField id="student-schoolYear" type="text" name="schoolYear" />
              </AvGroup>
              <AvGroup>
                <Label id="firstNameLabel" for="student-firstName">
                  <Translate contentKey="classscheduleApp.student.firstName">First Name</Translate>
                </Label>
                <AvField id="student-firstName" type="text" name="firstName" />
              </AvGroup>
              <AvGroup>
                <Label id="lastNameLabel" for="student-lastName">
                  <Translate contentKey="classscheduleApp.student.lastName">Last Name</Translate>
                </Label>
                <AvField id="student-lastName" type="text" name="lastName" />
              </AvGroup>
              <AvGroup>
                <Label id="emailLabel" for="student-email">
                  <Translate contentKey="classscheduleApp.student.email">Email</Translate>
                </Label>
                <AvField id="student-email" type="text" name="email" />
              </AvGroup>
              <AvGroup>
                <Label id="phoneLabel" for="student-phone">
                  <Translate contentKey="classscheduleApp.student.phone">Phone</Translate>
                </Label>
                <AvField id="student-phone" type="text" name="phone" />
              </AvGroup>
              <AvGroup>
                <Label id="createdByLabel" for="student-createdBy">
                  <Translate contentKey="classscheduleApp.student.createdBy">Created By</Translate>
                </Label>
                <AvField id="student-createdBy" type="text" name="createdBy" />
              </AvGroup>
              <AvGroup>
                <Label id="createdLabel" for="student-created">
                  <Translate contentKey="classscheduleApp.student.created">Created</Translate>
                </Label>
                <AvInput
                  id="student-created"
                  type="datetime-local"
                  className="form-control"
                  name="created"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.studentEntity.created)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedLabel" for="student-updated">
                  <Translate contentKey="classscheduleApp.student.updated">Updated</Translate>
                </Label>
                <AvInput
                  id="student-updated"
                  type="datetime-local"
                  className="form-control"
                  name="updated"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.studentEntity.updated)}
                />
              </AvGroup>
              <AvGroup>
                <Label for="student-user">
                  <Translate contentKey="classscheduleApp.student.user">User</Translate>
                </Label>
                <AvInput id="student-user" type="select" className="form-control" name="user.id">
                  <option value="" key="0" />
                  {users
                    ? users.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.email}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="student-parent">
                  <Translate contentKey="classscheduleApp.student.parent">Parent</Translate>
                </Label>
                <AvInput id="student-parent" type="select" className="form-control" name="parent.id">
                  <option value="" key="0" />
                  {parents
                    ? parents.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.firstName}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="student-teacher">
                  <Translate contentKey="classscheduleApp.student.teacher">Teacher</Translate>
                </Label>
                <AvInput id="student-teacher" type="select" className="form-control" name="teacher.id">
                  <option value="" key="0" />
                  {teachers
                    ? teachers.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.firstName}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/student" replace color="info">
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
  users: storeState.userManagement.users,
  parents: storeState.parent.entities,
  teachers: storeState.teacher.entities,
  studentEntity: storeState.student.entity,
  loading: storeState.student.loading,
  updating: storeState.student.updating,
  updateSuccess: storeState.student.updateSuccess,
});

const mapDispatchToProps = {
  getUsers,
  getParents,
  getTeachers,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(StudentUpdate);
