import {queryCoinPair, removeCoinPair, addCoinPair,findCoinPair,recommendCoin,unRecommendCoin} from '../../services/exchange/CoinPairApi';


export default {
  namespace: 'coinPair',

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
      const response = yield call(queryCoinPair, payload);
      yield put({
        type: 'list',
        payload: response,
      });
    },
    * find({payload}, {call, put}) {
      const response = yield call(findCoinPair, payload);
      yield put({
        type: 'get',
        payload: response,
      });
    },
    * add({payload, callback}, {call, put}) {
      const response = yield call(addCoinPair, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * remove({payload, callback}, {call, put}) {
      const response = yield call(removeCoinPair, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * recommendCoin({payload, callback}, {call, put}) {
      const response = yield call(recommendCoin, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * unRecommendCoin({payload, callback}, {call, put}) {
      const response = yield call(unRecommendCoin, payload);
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
