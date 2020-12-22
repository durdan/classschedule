import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IInvite, defaultValue } from 'app/shared/model/invite.model';

export const ACTION_TYPES = {
  FETCH_INVITE_LIST: 'invite/FETCH_INVITE_LIST',
  FETCH_INVITE: 'invite/FETCH_INVITE',
  CREATE_INVITE: 'invite/CREATE_INVITE',
  UPDATE_INVITE: 'invite/UPDATE_INVITE',
  DELETE_INVITE: 'invite/DELETE_INVITE',
  RESET: 'invite/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IInvite>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type InviteState = Readonly<typeof initialState>;

// Reducer

export default (state: InviteState = initialState, action): InviteState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_INVITE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_INVITE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_INVITE):
    case REQUEST(ACTION_TYPES.UPDATE_INVITE):
    case REQUEST(ACTION_TYPES.DELETE_INVITE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_INVITE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_INVITE):
    case FAILURE(ACTION_TYPES.CREATE_INVITE):
    case FAILURE(ACTION_TYPES.UPDATE_INVITE):
    case FAILURE(ACTION_TYPES.DELETE_INVITE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_INVITE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_INVITE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_INVITE):
    case SUCCESS(ACTION_TYPES.UPDATE_INVITE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_INVITE):
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

const apiUrl = 'api/invites';

// Actions

export const getEntities: ICrudGetAllAction<IInvite> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_INVITE_LIST,
    payload: axios.get<IInvite>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IInvite> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_INVITE,
    payload: axios.get<IInvite>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IInvite> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_INVITE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IInvite> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_INVITE,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IInvite> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_INVITE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
