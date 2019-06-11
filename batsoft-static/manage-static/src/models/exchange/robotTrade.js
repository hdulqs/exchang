import {queryRobotTrade, removeRobotTrade,openRobotTrade,stopRobotTrade, addRobotTrade,findRobotTrade} from '../../services/exchange/RobotTradeApi';


export default {
  namespace: 'robotTrade',

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
      const response = yield call(queryRobotTrade, payload);
      yield put({
        type: 'list',
        payload: response,
      });
    },
    * find({payload}, {call, put}) {
      const response = yield call(findRobotTrade, payload);
      yield put({
        type: 'get',
        payload: response,
      });
    },
    * add({payload, callback}, {call, put}) {
      const response = yield call(addRobotTrade, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * remove({payload, callback}, {call, put}) {
      const response = yield call(removeRobotTrade, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },

    * open({payload, callback}, {call, put}) {
      const response = yield call(openRobotTrade, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },

    * stop({payload, callback}, {call, put}) {
      const response = yield call(stopRobotTrade, payload);
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
