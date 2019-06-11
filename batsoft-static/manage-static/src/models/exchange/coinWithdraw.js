import {queryCoinWithdraw, removeCoinWithdraw, addCoinWithdraw,findCoinWithdraw,passStatus,refuseStatus} from '../../services/exchange/CoinWithdrawApi';


export default {
  namespace: 'coinWithdraw',

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
      payload = {
        ...payload,
        sortOrder: 'desc',
        sortName: 'updateTime'
      }
      const response = yield call(queryCoinWithdraw, payload);
      yield put({
        type: 'list',
        payload: response,
      });
    },
    * find({payload}, {call, put}) {
      const response = yield call(findCoinWithdraw, payload);
      yield put({
        type: 'get',
        payload: response,
      });
    },
    * add({payload, callback}, {call, put}) {
      const response = yield call(addCoinWithdraw, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * remove({payload, callback}, {call, put}) {
      const response = yield call(removeCoinWithdraw, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * refuseStatus({payload, callback}, {call, put}) {
      const response = yield call(refuseStatus, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * passStatus({payload, callback}, {call, put}) {
      const response = yield call(passStatus, payload);
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
