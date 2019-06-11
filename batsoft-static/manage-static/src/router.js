import React from 'react';
import { routerRedux, Route, Switch } from 'dva/router';
import { LocaleProvider, Spin } from 'antd';
import zhCN from 'antd/lib/locale-provider/zh_CN';
import dynamic from 'dva/dynamic';
import { getRouterData } from './common/router';
import {  getAuthority } from './utils/authority';
import Authorized from './utils/Authorized';
import styles from './index.less';
import {getGlobData} from "./utils/globData";

const { ConnectedRouter } = routerRedux;
const { AuthorizedRoute } = Authorized;
dynamic.setDefaultLoadingComponent(() => {
  return <Spin size="large" className={styles.globalSpin} />;
});

function RouterConfig({ history, app }) {

  const globData={};
  globData.role=["admin"];

  if(getGlobData("system-globData")!="undefined"){
    let data=JSON.parse(getGlobData("system-globData"));
    if(data!=null){
      globData.role=data.role;
    }
  }
  //校验权限 如果已经有权限直接跳转到 首页
  const  author=getAuthority();
  if(author!=undefined&&app._history.location.pathname=="/login"){
    //app._history.push("/");
  }
  const routerData = getRouterData(app);
  const UserLayout = routerData['/user'].component;
  const LoginLayout = routerData['/login'].component;
  const RegisterLayout = routerData['/register'].component;
  const BasicLayout = routerData['/'].component;
  return (
    <LocaleProvider locale={zhCN}>
      <ConnectedRouter history={history}>
        <Switch>
          <Route
            path="/user"
            component={UserLayout}
          />
          <Route
            path="/login"
            component={LoginLayout}
          />
          <Route
            path="/register"
            component={RegisterLayout}
          />
          <AuthorizedRoute
            path="/"
            render={props => <BasicLayout {...props} />}
            authority={globData.role}
            redirectPath="/login"
          />
        </Switch>
      </ConnectedRouter>
    </LocaleProvider>
  );
}

export default RouterConfig;
