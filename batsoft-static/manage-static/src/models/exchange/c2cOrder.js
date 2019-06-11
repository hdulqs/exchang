import {queryC2cOrder, removeC2cOrder, addC2cOrder,findC2cOrder,c2cOrderPass,c2cRefuseOrder} from '../../services/exchange/C2cOrderApi';


export default {
  namespace: 'c2cOrder',

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
      const response = yield call(queryC2cOrder, payload);
      yield put({
        type: 'list',
        payload: response,
      });
    },
    * find({payload}, {call, put}) {
      const response = yield call(findC2cOrder, payload);
      yield put({
        type: 'get',
        payload: response,
      });
    },
    * add({payload, callback}, {call, put}) {
      const response = yield call(addC2cOrder, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * c2cOrderPass({payload, callback}, {call, put}) {
      const response = yield call(c2cOrderPass, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * c2cRefuseOrder({payload, callback}, {call, put}) {
      const response = yield call(c2cRefuseOrder, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * remove({payload, callback}, {call, put}) {
      const response = yield call(removeC2cOrder, payload);
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
