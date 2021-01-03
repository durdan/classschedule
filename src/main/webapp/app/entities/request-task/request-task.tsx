import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction, TextFormat, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './request-task.reducer';
import { IRequestTask } from 'app/shared/model/request-task.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';

export interface IRequestTaskProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const RequestTask = (props: IRequestTaskProps) => {
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

  const { requestTaskList, match, loading, totalItems } = props;
  return (
    <div>
      <h2 id="request-task-heading">
        <Translate contentKey="classscheduleApp.requestTask.home.title">Request Tasks</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="classscheduleApp.requestTask.home.createLabel">Create new Request Task</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {requestTaskList && requestTaskList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('requestedUserId')}>
                  <Translate contentKey="classscheduleApp.requestTask.requestedUserId">Requested User Id</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('requestCode')}>
                  <Translate contentKey="classscheduleApp.requestTask.requestCode">Request Code</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('requestType')}>
                  <Translate contentKey="classscheduleApp.requestTask.requestType">Request Type</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('requiredActionFromUserId')}>
                  <Translate contentKey="classscheduleApp.requestTask.requiredActionFromUserId">Required Action From User Id</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="classscheduleApp.requestTask.createdBy">Created By</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('created')}>
                  <Translate contentKey="classscheduleApp.requestTask.created">Created</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('updated')}>
                  <Translate contentKey="classscheduleApp.requestTask.updated">Updated</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="classscheduleApp.requestTask.requestStatus">Request Status</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="classscheduleApp.requestTask.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {requestTaskList.map((requestTask, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${requestTask.id}`} color="link" size="sm">
                      {requestTask.id}
                    </Button>
                  </td>
                  <td>{requestTask.requestedUserId}</td>
                  <td>{requestTask.requestCode}</td>
                  <td>{requestTask.requestType}</td>
                  <td>{requestTask.requiredActionFromUserId}</td>
                  <td>{requestTask.createdBy}</td>
                  <td>{requestTask.created ? <TextFormat type="date" value={requestTask.created} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{requestTask.updated ? <TextFormat type="date" value={requestTask.updated} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    {requestTask.requestStatus ? (
                      <Link to={`request-status/${requestTask.requestStatus.id}`}>{requestTask.requestStatus.status}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{requestTask.user ? requestTask.user.email : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${requestTask.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${requestTask.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                        to={`${match.url}/${requestTask.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
              <Translate contentKey="classscheduleApp.requestTask.home.notFound">No Request Tasks found</Translate>
            </div>
          )
        )}
      </div>
      {props.totalItems ? (
        <div className={requestTaskList && requestTaskList.length > 0 ? '' : 'd-none'}>
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

const mapStateToProps = ({ requestTask }: IRootState) => ({
  requestTaskList: requestTask.entities,
  loading: requestTask.loading,
  totalItems: requestTask.totalItems,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RequestTask);
