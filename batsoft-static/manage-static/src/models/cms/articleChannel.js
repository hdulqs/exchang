import {queryArticleChannel, removeArticleChannel, addArticleChannel,findArticleChannel,findChannels} from '../../services/cms/ArticleChannelApi';


export default {
  namespace: 'articleChannel',

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
      const response = yield call(queryArticleChannel, payload);
      yield put({
        type: 'list',
        payload: response,
      });
    },
    * find({payload}, {call, put}) {
      const response = yield call(findArticleChannel, payload);
      yield put({
        type: 'get',
        payload: response,
      });
    },
    * findChannels({payload}, {call, put}) {
      const response = yield call(findChannels, payload);
      yield put({
        type: 'get',
        payload: response,
      });
    },

    * add({payload, callback}, {call, put}) {
      const response = yield call(addArticleChannel, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback(response);
    },
    * remove({payload, callback}, {call, put}) {
      const response = yield call(removeArticleChannel, payload);
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
