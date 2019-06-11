import {routerRedux} from 'dva/router';
import {userLogin, logout} from '../services/LoginApi';
import {setAuthority,setPermissions} from '../utils/authority';
import {reloadAuthorized} from '../utils/Authorized';
import {getGlobData} from "../utils/globData";


export default {
  namespace: 'login',

  state: {
    status: undefined,
  },

  effects: {
    * login({payload}, {call, put}) {
      const response = yield call(userLogin, payload);
      yield put({
        type: 'changeLoginStatus',
        payload: response,
      });

      // Login successfully
      if (response.success) {

        reloadAuthorized();
        yield put(routerRedux.push('/'));
      }
    },
    * logout(_, {call, put, select}) {
      try {
        // get location pathname
        const urlParams = new URL(window.location.href);
        const pathname = yield select(state => state.routing.location.pathname);
        // add the parameters in the url
        urlParams.searchParams.set('redirect', pathname);
        window.history.replaceState(null, 'login', urlParams.href);
      } finally {
        const response = yield call(logout, {
          type: 'changeLoginStatus',
          payload: {
            status: false,
            currentAuthority: '',
          },
        });
        if (response.success) {

          setAuthority('');
          setPermissions('');
          reloadAuthorized();
          yield put(routerRedux.push('/login'));
        }
      }
    },
  },

  reducers: {
    changeLoginStatus(state, {payload}) {
      const globData=JSON.parse(getGlobData("system-globData"));
      globData.role=[payload.userName];
      setAuthority(payload.userName);
      setPermissions(payload.permissions);
      return {
        ...state,
        status: payload.success,
        type: payload.type,
      };
    },
  },
};
