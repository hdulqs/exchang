import {queryAppConfigType, removeAppConfigType, addAppConfigType,findAppConfigType,queryFindTypes} from '../../../services/system/config/AppConfigTypeApi';


export default {
  namespace: 'appConfigType',

  state: {
    list: {
      rows: [],
      pagination: {},
    },
    data: {},
    findData:{
      data:{}
    },
  },

  effects: {
    * fetch({payload}, {call, put}) {
      const response = yield call(queryAppConfigType, payload);
      yield put({
        type: 'list',
        payload: response,
      });
    },
    * find({payload}, {call, put}) {
      const response = yield call(findAppConfigType, payload);
      yield put({
        type: 'get',
        payload: response,
      });
    },
    * findTypes({payload,callback}, {call, put}) {
      const response = yield call(queryFindTypes, payload);
      yield put({
        type: 'get',
        payload: response,
      });
      if (callback) callback(response);
    },

    * add({payload, callback}, {call, put}) {
      const response = yield call(addAppConfigType, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * remove({payload, callback}, {call, put}) {
      const response = yield call(removeAppConfigType, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
  },

  reducers: {
    list(state, action) {
      return {
        ...state,
        list: action.payload,
      };
    },
    get(state, action) {
      return {
        ...state,
        findData: action.payload,
      };
    },
    save(state, action) {
      return {
        ...state,
        data: action.payload,
      };
    },
  },
};
