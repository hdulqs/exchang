import {queryAppCache, removeAppCache, addAppCache,findAppCache,refreshCache} from '../../../services/system/cache/AppCacheApi';


export default {
  namespace: 'appCache',

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
      const response = yield call(queryAppCache, payload);
      yield put({
        type: 'list',
        payload: response,
      });
    },
    * find({payload}, {call, put}) {
      const response = yield call(findAppCache, payload);
      yield put({
        type: 'get',
        payload: response,
      });
    },
    * refreshCache({payload,callback}, {call, put}) {
      const response = yield call(refreshCache, payload);
      yield put({
        type: 'get',
        payload: response,
      });
      if (callback) callback(response);
    },
    * add({payload, callback}, {call, put}) {
      const response = yield call(addAppCache, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * remove({payload, callback}, {call, put}) {
      const response = yield call(removeAppCache, payload);
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
