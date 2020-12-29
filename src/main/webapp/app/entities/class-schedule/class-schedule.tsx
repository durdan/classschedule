import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction, TextFormat, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './class-schedule.reducer';
import { IClassSchedule } from 'app/shared/model/class-schedule.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';

export interface IClassScheduleProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const ClassSchedule = (props: IClassScheduleProps) => {
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE), props.location.search)
  );

  const getAllEntities = () => {
    props.getEntities(paginationState.activePage - 1, paginationState.itemsPerPage, `${paginationState.sort},${paginationState.order}`);
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (props.location.search !== endURL) {
      props.history.push(`${props.location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(props.location.search);
    const page = params.get('page');
    const sort = params.get('sort');
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [props.location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === 'asc' ? 'desc' : 'asc',
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const { classScheduleList, match, loading, totalItems } = props;
  return (
    <div>
      <h2 id="class-schedule-heading">
        <Translate contentKey="classscheduleApp.classSchedule.home.title">Class Schedules</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="classscheduleApp.classSchedule.home.createLabel">Create new Class Schedule</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {classScheduleList && classScheduleList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="classscheduleApp.classSchedule.name">Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('created')}>
                  <Translate contentKey="classscheduleApp.classSchedule.created">Created</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('schedule')}>
                  <Translate contentKey="classscheduleApp.classSchedule.schedule">Schedule</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('updated')}>
                  <Translate contentKey="classscheduleApp.classSchedule.updated">Updated</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="classscheduleApp.classSchedule.createdBy">Created By</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('updatedBy')}>
                  <Translate contentKey="classscheduleApp.classSchedule.updatedBy">Updated By</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('confirmedByStudent')}>
                  <Translate contentKey="classscheduleApp.classSchedule.confirmedByStudent">Confirmed By Student</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('confirmedByTeacher')}>
                  <Translate contentKey="classscheduleApp.classSchedule.confirmedByTeacher">Confirmed By Teacher</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('comment')}>
                  <Translate contentKey="classscheduleApp.classSchedule.comment">Comment</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('connected')}>
                  <Translate contentKey="classscheduleApp.classSchedule.connected">Connected</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('reoccurring')}>
                  <Translate contentKey="classscheduleApp.classSchedule.reoccurring">Reoccurring</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="classscheduleApp.classSchedule.student">Student</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="classscheduleApp.classSchedule.teacher">Teacher</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="classscheduleApp.classSchedule.parent">Parent</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="classscheduleApp.classSchedule.course">Course</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {classScheduleList.map((classSchedule, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${classSchedule.id}`} color="link" size="sm">
                      {classSchedule.id}
                    </Button>
                  </td>
                  <td>{classSchedule.name}</td>
                  <td>
                    {classSchedule.created ? <TextFormat type="date" value={classSchedule.created} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {classSchedule.schedule ? <TextFormat type="date" value={classSchedule.schedule} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {classSchedule.updated ? <TextFormat type="date" value={classSchedule.updated} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{classSchedule.createdBy}</td>
                  <td>{classSchedule.updatedBy}</td>
                  <td>{classSchedule.confirmedByStudent}</td>
                  <td>{classSchedule.confirmedByTeacher}</td>
                  <td>{classSchedule.comment}</td>
                  <td>{classSchedule.connected ? 'true' : 'false'}</td>
                  <td>{classSchedule.reoccurring ? 'true' : 'false'}</td>
                  <td>{classSchedule.student ? <Link to={`student/${classSchedule.student.id}`}>{classSchedule.student.id}</Link> : ''}</td>
                  <td>{classSchedule.teacher ? <Link to={`teacher/${classSchedule.teacher.id}`}>{classSchedule.teacher.id}</Link> : ''}</td>
                  <td>{classSchedule.parent ? <Link to={`parent/${classSchedule.parent.id}`}>{classSchedule.parent.id}</Link> : ''}</td>
                  <td>{classSchedule.course ? <Link to={`course/${classSchedule.course.id}`}>{classSchedule.course.name}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${classSchedule.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${classSchedule.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${classSchedule.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="classscheduleApp.classSchedule.home.notFound">No Class Schedules found</Translate>
            </div>
          )
        )}
      </div>
      {props.totalItems ? (
        <div className={classScheduleList && classScheduleList.length > 0 ? '' : 'd-none'}>
          <Row className="justify-content-center">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </Row>
          <Row className="justify-content-center">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={props.totalItems}
            />
          </Row>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

const mapStateToProps = ({ classSchedule }: IRootState) => ({
  classScheduleList: classSchedule.entities,
  loading: classSchedule.loading,
  totalItems: classSchedule.totalItems,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ClassSchedule);
