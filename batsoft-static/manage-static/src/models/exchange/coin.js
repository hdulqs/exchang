import {queryCoin, removeCoin, addCoin,findCoin,updateStatus,closeStatus,setPriceCoin,allowRecharge,allowDeposit,findPriceCoins,setTrade} from '../../services/exchange/CoinApi';


export default {
  namespace: 'coin',

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
      const response = yield call(queryCoin, payload);
      yield put({
        type: 'list',
        payload: response,
      });
    },
    * find({payload}, {call, put}) {
      const response = yield call(findCoin, payload);
      yield put({
        type: 'get',
        payload: response,
      });
    },
    * findPriceCoins({payload}, {call, put}) {
      const response = yield call(findPriceCoins, payload);
      yield put({
        type: 'get',
        payload: response,
      });
    },
    * add({payload, callback}, {call, put}) {
      const response = yield call(addCoin, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * remove({payload, callback}, {call, put}) {
      const response = yield call(removeCoin, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * updateStatus({payload, callback}, {call, put}) {
      const response = yield call(updateStatus, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * closeStatus({payload, callback}, {call, put}) {
      const response = yield call(closeStatus, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * setPriceCoin({payload, callback}, {call, put}) {
      const response = yield call(setPriceCoin, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * setTrade({payload, callback}, {call, put}) {
      const response = yield call(setTrade, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * allowRecharge({payload, callback}, {call, put}) {
      const response = yield call(allowRecharge, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },

    * allowDeposit({payload, callback}, {call, put}) {
      const response = yield call(allowDeposit, payload);
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
