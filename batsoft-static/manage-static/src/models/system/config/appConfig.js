import {queryAppConfig, removeAppConfig, addAppConfig,findAppConfig,findAppConfigByTypeKey,updateConfig} from '../../../services/system/config/AppConfigApi';


export default {
  namespace: 'appConfig',

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
      const response = yield call(queryAppConfig, payload);
      yield put({
        type: 'list',
        payload: response,
      });
    },
    * find({payload}, {call, put}) {
      const response = yield call(findAppConfig, payload);
      yield put({
        type: 'get',
        payload: response,
      });
    },
    * findAppConfigByTypeKey({payload}, {call, put}) {
      const response = yield call(findAppConfigByTypeKey, payload);
      yield put({
        type: 'get',
        payload: response,
      });
    },
    * add({payload, callback}, {call, put}) {
      const response = yield call(addAppConfig, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * update({payload, callback}, {call, put}) {
      const response = yield call(updateConfig, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * remove({payload, callback}, {call, put}) {
      const response = yield call(removeAppConfig, payload);
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
