import {queryAppUser, removeAppUser, addAppUser, findAppUser,findCurrent} from '../../../services/system/manage/AppUserApi';
import {reloadAuthorized} from "../../../utils/Authorized";
import {routerRedux} from "dva/router";
import {setAuthority} from "../../../utils/authority";



export default {
  namespace: 'appUser',

  state: {
    list: {
      rows: [],
      pagination: {},
    },
    currentUser: {},
    data: {},
    findData: {
      data: {}
    },
  },

  effects: {
    * fetch({payload}, {call, put}) {
      const response = yield call(queryAppUser, payload);
      yield put({
        type: 'list',
        payload: response,
      });
    },
    * find({payload}, {call, put}) {
      const response = yield call(findAppUser, payload);
      yield put({
        type: 'get',
        payload: response,
      });
    },
    * add({payload, callback}, {call, put}) {
      const response = yield call(addAppUser, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * remove({payload, callback}, {call, put}) {
      const response = yield call(removeAppUser, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * fetchCurrent(_, {call, put}) {
      const response = yield call(findCurrent);
      yield put({
        type: 'findCurrentUser',
        payload: response,
      });
    },
  },

  reducers: {
    findCurrentUser(state, action) {
      if(action.payload===undefined){
        setAuthority('');
        reloadAuthorized();
        routerRedux.push('/login');
        return false;
      }
      return {
        ...state,
        currentUser: action.payload,
      };
    },
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
