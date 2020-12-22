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
import { IStudent } from 'app/shared/model/student.model';
import { getEntities as getStudents } from 'app/entities/student/student.reducer';
import { getEntity, updateEntity, createEntity, reset } from './teacher.reducer';
import { ITeacher } from 'app/shared/model/teacher.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ITeacherUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TeacherUpdate = (props: ITeacherUpdateProps) => {
  const [userId, setUserId] = useState('0');
  const [parentId, setParentId] = useState('0');
  const [studentId, setStudentId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { teacherEntity, users, parents, students, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/teacher' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getUsers();
    props.getParents();
    props.getStudents();
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
        ...teacherEntity,
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
          <h2 id="classscheduleApp.teacher.home.createOrEditLabel">
            <Translate contentKey="classscheduleApp.teacher.home.createOrEditLabel">Create or edit a Teacher</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : teacherEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="teacher-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="teacher-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="profileContentLabel" for="teacher-profileContent">
                  <Translate contentKey="classscheduleApp.teacher.profileContent">Profile Content</Translate>
                </Label>
                <AvField id="teacher-profileContent" type="text" name="profileContent" />
              </AvGroup>
              <AvGroup>
                <Label id="firstNameLabel" for="teacher-firstName">
                  <Translate contentKey="classscheduleApp.teacher.firstName">First Name</Translate>
                </Label>
                <AvField id="teacher-firstName" type="text" name="firstName" />
              </AvGroup>
              <AvGroup>
                <Label id="lastNameLabel" for="teacher-lastName">
                  <Translate contentKey="classscheduleApp.teacher.lastName">Last Name</Translate>
                </Label>
                <AvField id="teacher-lastName" type="text" name="lastName" />
              </AvGroup>
              <AvGroup>
                <Label id="emailLabel" for="teacher-email">
                  <Translate contentKey="classscheduleApp.teacher.email">Email</Translate>
                </Label>
                <AvField id="teacher-email" type="text" name="email" />
              </AvGroup>
              <AvGroup>
                <Label id="phoneLabel" for="teacher-phone">
                  <Translate contentKey="classscheduleApp.teacher.phone">Phone</Translate>
                </Label>
                <AvField id="teacher-phone" type="text" name="phone" />
              </AvGroup>
              <AvGroup>
                <Label id="createdByLabel" for="teacher-createdBy">
                  <Translate contentKey="classscheduleApp.teacher.createdBy">Created By</Translate>
                </Label>
                <AvField id="teacher-createdBy" type="text" name="createdBy" />
              </AvGroup>
              <AvGroup>
                <Label id="createdLabel" for="teacher-created">
                  <Translate contentKey="classscheduleApp.teacher.created">Created</Translate>
                </Label>
                <AvInput
                  id="teacher-created"
                  type="datetime-local"
                  className="form-control"
                  name="created"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.teacherEntity.created)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedLabel" for="teacher-updated">
                  <Translate contentKey="classscheduleApp.teacher.updated">Updated</Translate>
                </Label>
                <AvInput
                  id="teacher-updated"
                  type="datetime-local"
                  className="form-control"
                  name="updated"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.teacherEntity.updated)}
                />
              </AvGroup>
              <AvGroup>
                <Label for="teacher-user">
                  <Translate contentKey="classscheduleApp.teacher.user">User</Translate>
                </Label>
                <AvInput id="teacher-user" type="select" className="form-control" name="user.id">
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
                <Label for="teacher-parent">
                  <Translate contentKey="classscheduleApp.teacher.parent">Parent</Translate>
                </Label>
                <AvInput id="teacher-parent" type="select" className="form-control" name="parent.id">
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
                <Label for="teacher-student">
                  <Translate contentKey="classscheduleApp.teacher.student">Student</Translate>
                </Label>
                <AvInput id="teacher-student" type="select" className="form-control" name="student.id">
                  <option value="" key="0" />
                  {students
                    ? students.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.firstName}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/teacher" replace color="info">
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
  students: storeState.student.entities,
  teacherEntity: storeState.teacher.entity,
  loading: storeState.teacher.loading,
  updating: storeState.teacher.updating,
  updateSuccess: storeState.teacher.updateSuccess,
});

const mapDispatchToProps = {
  getUsers,
  getParents,
  getStudents,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TeacherUpdate);
