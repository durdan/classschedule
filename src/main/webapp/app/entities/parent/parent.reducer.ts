import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IParent, defaultValue } from 'app/shared/model/parent.model';

export const ACTION_TYPES = {
  FETCH_PARENT_LIST: 'parent/FETCH_PARENT_LIST',
  FETCH_PARENT: 'parent/FETCH_PARENT',
  CREATE_PARENT: 'parent/CREATE_PARENT',
  UPDATE_PARENT: 'parent/UPDATE_PARENT',
  DELETE_PARENT: 'parent/DELETE_PARENT',
  RESET: 'parent/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IParent>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ParentState = Readonly<typeof initialState>;

// Reducer

export default (state: ParentState = initialState, action): ParentState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PARENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PARENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PARENT):
    case REQUEST(ACTION_TYPES.UPDATE_PARENT):
    case REQUEST(ACTION_TYPES.DELETE_PARENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_PARENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PARENT):
    case FAILURE(ACTION_TYPES.CREATE_PARENT):
    case FAILURE(ACTION_TYPES.UPDATE_PARENT):
    case FAILURE(ACTION_TYPES.DELETE_PARENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PARENT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_PARENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PARENT):
    case SUCCESS(ACTION_TYPES.UPDATE_PARENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PARENT):
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

const apiUrl = 'api/parents';

// Actions

export const getEntities: ICrudGetAllAction<IParent> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PARENT_LIST,
    payload: axios.get<IParent>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IParent> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PARENT,
    payload: axios.get<IParent>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IParent> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PARENT,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IParent> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PARENT,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IParent> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PARENT,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
