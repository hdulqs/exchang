import {queryAppRole, removeAppRole, addAppRole,findAppRole,findRoles,addPerm,findMenu} from '../../../services/system/manage/AppRoleApi';


export default {
  namespace: 'appRole',

  state: {
    list: {
      rows: [],
      pagination: {},
    },
    data: {},
    findData:{
      data:{}
    },
    menuData:[],
    roleData:[],
  },

  effects: {
    * fetch({payload}, {call, put}) {
      const response = yield call(queryAppRole, payload);
      yield put({
        type: 'list',
        payload: response,
      });
    },
    * find({payload}, {call, put}) {
      const response = yield call(findAppRole, payload);
      yield put({
        type: 'get',
        payload: response,
      });
    },
    * findRoles({payload}, {call, put}) {
      const response = yield call(findRoles, payload);
      yield put({
        type: 'findRoleData',
        payload: response,
      });
    },
    * findMenu({payload,callback}, {call, put}) {
      const response = yield call(findMenu, payload);
      yield put({
        type: 'findMenuData',
        payload: response,
      });
      if (callback) callback(response);
    },
    * addPerm({payload, callback}, {call, put}) {
      const response = yield call(addPerm, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * add({payload, callback}, {call, put}) {
      const response = yield call(addAppRole, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * remove({payload, callback}, {call, put}) {
      const response = yield call(removeAppRole, payload);
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
    findRoleData(state, action) {
      return {
        ...state,
        roleData: action.payload,
      };
    },
    findMenuData(state, action) {
      return {
        ...state,
        menuData: action.payload,
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
