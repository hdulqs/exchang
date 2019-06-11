import {
  queryAppMenuTree,
  removeAppMenu,
  addAppMenu,
  queryAppMenuTreeList,
  findAppMenu
} from '../../../services/system/navigation/AppNavigationApi';


export default {
  namespace: 'appNavigation',

  state: {
    list: {
      rows: [],
      pagination: {},
    },
    data: {},
    menuTreeData: {},
    findData: {
      data: {}
    },
  },

  effects: {

    * menuTreeData({payload}, {call, put}) {
      const response = yield call(queryAppMenuTreeList, payload);
      yield put({
        type: 'get',
        payload: response,
      });
    },
    * findMenu({payload}, {call, put}) {
      const response = yield call(findAppMenu, payload);
      yield put({
        type: 'findById',
        payload: response,
      });
    },
    * menuTree({payload}, {call, put}) {
      const response = yield call(queryAppMenuTree, payload);
      yield put({
        type: 'get',
        payload: response,
      });
    },
    * add({payload, callback}, {call, put}) {
      const response = yield call(addAppMenu, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * remove({payload, callback}, {call, put}) {
      const response = yield call(removeAppMenu, payload);
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
        menuTreeData: action.payload,
      };
    },
    findById(state, action) {
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
