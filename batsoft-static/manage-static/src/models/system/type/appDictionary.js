import {
  queryAppDictionary,
  removeAppDictionary,
  queryAppDictionaryTree,
  addAppDictionary,
  findAppDictionary,
  findByKey,
  queryAppDictionaryTreeList
} from '../../../services/system/type/AppDictionaryApi';


export default {
  namespace: 'appDictionary',

  state: {
    list: {
      rows: [],
      pagination: {},
    },
    data: {},
    dicTreeData: {},
    findData: {
      data: {}
    },
  },

  effects: {
    * fetch({payload}, {call, put}) {
      const response = yield call(queryAppDictionary, payload);
      yield put({
        type: 'list',
        payload: response,
      });
    },
    * findDic({payload}, {call, put}) {
      const response = yield call(findAppDictionary, payload);
      yield put({
        type: 'findById',
        payload: response,
      });
    },
    * dicTree({payload}, {call, put}) {
      const response = yield call(queryAppDictionaryTree, payload);
      yield put({
        type: 'get',
        payload: response,
      });
    },

    * findByKey({payload}, {call, put}) {


      const response = yield call(findByKey, payload);
      yield put({
        type: 'get',
        payload: response,
      });
    },

    * dicTreeData({payload, callback}, {call, put}) {
      const response = yield call(queryAppDictionaryTreeList, payload);
      yield put({
        type: 'get',
        payload: response,
      });
      if (callback) callback(response);
    },

    * add({payload, callback}, {call, put}) {
      const response = yield call(addAppDictionary, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * remove({payload, callback}, {call, put}) {
      const response = yield call(removeAppDictionary, payload);
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
    findById(state, action) {
      return {
        ...state,
        findData: action.payload,
      };
    },
    get(state, action) {
      return {
        ...state,
        dicTreeData: action.payload,
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
