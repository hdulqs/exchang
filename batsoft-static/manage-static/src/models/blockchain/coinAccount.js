import {queryCoinAccount, removeCoinAccount, addCoinAccount,findCoinAccount,createAddress} from '../../services/blockchain/CoinAccountApi';


export default {
  namespace: 'coinAccount',

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
      const response = yield call(queryCoinAccount, payload);
      yield put({
        type: 'list',
        payload: response,
      });
    },
    * find({payload}, {call, put}) {
      const response = yield call(findCoinAccount, payload);
      yield put({
        type: 'get',
        payload: response,
      });
    },
    * add({payload, callback}, {call, put}) {
      const response = yield call(addCoinAccount, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },

    * createAddress({payload, callback}, {call, put}) {
      const response = yield call(createAddress, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },

    * remove({payload, callback}, {call, put}) {
      const response = yield call(removeCoinAccount, payload);
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
