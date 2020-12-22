import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IClassSchedule, defaultValue } from 'app/shared/model/class-schedule.model';

export const ACTION_TYPES = {
  FETCH_CLASSSCHEDULE_LIST: 'classSchedule/FETCH_CLASSSCHEDULE_LIST',
  FETCH_CLASSSCHEDULE: 'classSchedule/FETCH_CLASSSCHEDULE',
  CREATE_CLASSSCHEDULE: 'classSchedule/CREATE_CLASSSCHEDULE',
  UPDATE_CLASSSCHEDULE: 'classSchedule/UPDATE_CLASSSCHEDULE',
  DELETE_CLASSSCHEDULE: 'classSchedule/DELETE_CLASSSCHEDULE',
  RESET: 'classSchedule/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IClassSchedule>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ClassScheduleState = Readonly<typeof initialState>;

// Reducer

export default (state: ClassScheduleState = initialState, action): ClassScheduleState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CLASSSCHEDULE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CLASSSCHEDULE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_CLASSSCHEDULE):
    case REQUEST(ACTION_TYPES.UPDATE_CLASSSCHEDULE):
    case REQUEST(ACTION_TYPES.DELETE_CLASSSCHEDULE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_CLASSSCHEDULE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CLASSSCHEDULE):
    case FAILURE(ACTION_TYPES.CREATE_CLASSSCHEDULE):
    case FAILURE(ACTION_TYPES.UPDATE_CLASSSCHEDULE):
    case FAILURE(ACTION_TYPES.DELETE_CLASSSCHEDULE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_CLASSSCHEDULE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_CLASSSCHEDULE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_CLASSSCHEDULE):
    case SUCCESS(ACTION_TYPES.UPDATE_CLASSSCHEDULE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_CLASSSCHEDULE):
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

const apiUrl = 'api/class-schedules';

// Actions

export const getEntities: ICrudGetAllAction<IClassSchedule> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CLASSSCHEDULE_LIST,
    payload: axios.get<IClassSchedule>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IClassSchedule> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CLASSSCHEDULE,
    payload: axios.get<IClassSchedule>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IClassSchedule> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CLASSSCHEDULE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IClassSchedule> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CLASSSCHEDULE,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IClassSchedule> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CLASSSCHEDULE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
