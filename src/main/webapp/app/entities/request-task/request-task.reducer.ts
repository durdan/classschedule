import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRequestTask, defaultValue } from 'app/shared/model/request-task.model';

export const ACTION_TYPES = {
  FETCH_REQUESTTASK_LIST: 'requestTask/FETCH_REQUESTTASK_LIST',
  FETCH_REQUESTTASK: 'requestTask/FETCH_REQUESTTASK',
  CREATE_REQUESTTASK: 'requestTask/CREATE_REQUESTTASK',
  UPDATE_REQUESTTASK: 'requestTask/UPDATE_REQUESTTASK',
  DELETE_REQUESTTASK: 'requestTask/DELETE_REQUESTTASK',
  RESET: 'requestTask/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRequestTask>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type RequestTaskState = Readonly<typeof initialState>;

// Reducer

export default (state: RequestTaskState = initialState, action): RequestTaskState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_REQUESTTASK_LIST):
    case REQUEST(ACTION_TYPES.FETCH_REQUESTTASK):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_REQUESTTASK):
    case REQUEST(ACTION_TYPES.UPDATE_REQUESTTASK):
    case REQUEST(ACTION_TYPES.DELETE_REQUESTTASK):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_REQUESTTASK_LIST):
    case FAILURE(ACTION_TYPES.FETCH_REQUESTTASK):
    case FAILURE(ACTION_TYPES.CREATE_REQUESTTASK):
    case FAILURE(ACTION_TYPES.UPDATE_REQUESTTASK):
    case FAILURE(ACTION_TYPES.DELETE_REQUESTTASK):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_REQUESTTASK_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_REQUESTTASK):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_REQUESTTASK):
    case SUCCESS(ACTION_TYPES.UPDATE_REQUESTTASK):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_REQUESTTASK):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/request-tasks';

// Actions

export const getEntities: ICrudGetAllAction<IRequestTask> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_REQUESTTASK_LIST,
    payload: axios.get<IRequestTask>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IRequestTask> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_REQUESTTASK,
    payload: axios.get<IRequestTask>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IRequestTask> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_REQUESTTASK,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRequestTask> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_REQUESTTASK,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRequestTask> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_REQUESTTASK,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
