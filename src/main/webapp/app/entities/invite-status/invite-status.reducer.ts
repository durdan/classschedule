import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IInviteStatus, defaultValue } from 'app/shared/model/invite-status.model';

export const ACTION_TYPES = {
  FETCH_INVITESTATUS_LIST: 'inviteStatus/FETCH_INVITESTATUS_LIST',
  FETCH_INVITESTATUS: 'inviteStatus/FETCH_INVITESTATUS',
  CREATE_INVITESTATUS: 'inviteStatus/CREATE_INVITESTATUS',
  UPDATE_INVITESTATUS: 'inviteStatus/UPDATE_INVITESTATUS',
  DELETE_INVITESTATUS: 'inviteStatus/DELETE_INVITESTATUS',
  RESET: 'inviteStatus/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IInviteStatus>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type InviteStatusState = Readonly<typeof initialState>;

// Reducer

export default (state: InviteStatusState = initialState, action): InviteStatusState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_INVITESTATUS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_INVITESTATUS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_INVITESTATUS):
    case REQUEST(ACTION_TYPES.UPDATE_INVITESTATUS):
    case REQUEST(ACTION_TYPES.DELETE_INVITESTATUS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_INVITESTATUS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_INVITESTATUS):
    case FAILURE(ACTION_TYPES.CREATE_INVITESTATUS):
    case FAILURE(ACTION_TYPES.UPDATE_INVITESTATUS):
    case FAILURE(ACTION_TYPES.DELETE_INVITESTATUS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_INVITESTATUS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_INVITESTATUS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_INVITESTATUS):
    case SUCCESS(ACTION_TYPES.UPDATE_INVITESTATUS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_INVITESTATUS):
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

const apiUrl = 'api/invite-statuses';

// Actions

export const getEntities: ICrudGetAllAction<IInviteStatus> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_INVITESTATUS_LIST,
    payload: axios.get<IInviteStatus>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IInviteStatus> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_INVITESTATUS,
    payload: axios.get<IInviteStatus>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IInviteStatus> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_INVITESTATUS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IInviteStatus> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_INVITESTATUS,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IInviteStatus> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_INVITESTATUS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
