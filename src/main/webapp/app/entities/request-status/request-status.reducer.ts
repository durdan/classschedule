import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRequestStatus, defaultValue } from 'app/shared/model/request-status.model';

export const ACTION_TYPES = {
  FETCH_REQUESTSTATUS_LIST: 'requestStatus/FETCH_REQUESTSTATUS_LIST',
  FETCH_REQUESTSTATUS: 'requestStatus/FETCH_REQUESTSTATUS',
  CREATE_REQUESTSTATUS: 'requestStatus/CREATE_REQUESTSTATUS',
  UPDATE_REQUESTSTATUS: 'requestStatus/UPDATE_REQUESTSTATUS',
  DELETE_REQUESTSTATUS: 'requestStatus/DELETE_REQUESTSTATUS',
  RESET: 'requestStatus/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRequestStatus>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type RequestStatusState = Readonly<typeof initialState>;

// Reducer

export default (state: RequestStatusState = initialState, action): RequestStatusState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_REQUESTSTATUS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_REQUESTSTATUS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_REQUESTSTATUS):
    case REQUEST(ACTION_TYPES.UPDATE_REQUESTSTATUS):
    case REQUEST(ACTION_TYPES.DELETE_REQUESTSTATUS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_REQUESTSTATUS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_REQUESTSTATUS):
    case FAILURE(ACTION_TYPES.CREATE_REQUESTSTATUS):
    case FAILURE(ACTION_TYPES.UPDATE_REQUESTSTATUS):
    case FAILURE(ACTION_TYPES.DELETE_REQUESTSTATUS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_REQUESTSTATUS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_REQUESTSTATUS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_REQUESTSTATUS):
    case SUCCESS(ACTION_TYPES.UPDATE_REQUESTSTATUS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_REQUESTSTATUS):
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

const apiUrl = 'api/request-statuses';

// Actions

export const getEntities: ICrudGetAllAction<IRequestStatus> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_REQUESTSTATUS_LIST,
    payload: axios.get<IRequestStatus>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IRequestStatus> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_REQUESTSTATUS,
    payload: axios.get<IRequestStatus>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IRequestStatus> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_REQUESTSTATUS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRequestStatus> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_REQUESTSTATUS,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRequestStatus> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_REQUESTSTATUS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
