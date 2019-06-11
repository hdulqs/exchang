import {queryCustomerAccount, removeCustomerAccount, addCustomerAccount,findCustomerAccount,depostSave,queryUserList,checkAccount} from '../../services/exchange/CustomerAccountApi';


export default {
  namespace: 'customerAccount',

  state: {
    accounts:[],
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
      const response = yield call(queryCustomerAccount, payload);
      yield put({
        type: 'list',
        payload: response,
      });
    },
    * userList({payload}, {call, put}) {
      const response = yield call(queryUserList, payload);
      yield put({
        type: 'list',
        payload: response,
      });
    },
    * checkAccount({payload}, {call, put}) {
      const response = yield call(checkAccount, payload);
      yield put({
        type: 'findAccount',
        payload: response,
      });
    },
    * find({payload}, {call, put}) {
      const response = yield call(findCustomerAccount, payload);
      yield put({
        type: 'get',
        payload: response,
      });
    },
    * add({payload, callback}, {call, put}) {
      const response = yield call(addCustomerAccount, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },

    * depostSave({payload, callback}, {call, put}) {
      const response = yield call(depostSave, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },

    * remove({payload, callback}, {call, put}) {
      const response = yield call(removeCustomerAccount, payload);
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
    findAccount(state, action) {
      return {
        ...state,
        accounts: action.payload,
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
