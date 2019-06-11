import React from 'react';
import PropTypes from 'prop-types';
import { Layout, Icon, message } from 'antd';
import DocumentTitle from 'react-document-title';
import { connect } from 'dva';
import { Route, Redirect, Switch, routerRedux } from 'dva/router';
import { ContainerQuery } from 'react-container-query';
import classNames from 'classnames';
import { enquireScreen } from 'enquire-js';
import GlobalHeader from '../components/GlobalHeader';
import GlobalFooter from '../components/GlobalFooter';
import SiderMenu from '../components/SiderMenu';
import NotFound from '../routes/Exception/404';
import { getRoutes } from '../utils/utils';
import Authorized from '../utils/Authorized';
import { getMenuDate } from '../services/api';
import { formatter } from '../common/menu';
import logo from '../assets/logo.svg';

const { Content, Header, Footer } = Layout;
const { AuthorizedRoute } = Authorized;

/**
 * 根据菜单取得重定向地址.
 */
const redirectData = [];
const getRedirect = (item) => {
  if (item && item.children) {
    if (item.children[0] && item.children[0].path) {
      redirectData.push({
        from: `/${item.path}`,
        to: `/${item.children[0].path}`,
      });
      item.children.forEach((children) => {
        getRedirect(children);
      });
    }
  }
};



const query = {
  'screen-xs': {
    maxWidth: 575,
  },
  'screen-sm': {
    minWidth: 576,
    maxWidth: 767,
  },
  'screen-md': {
    minWidth: 768,
    maxWidth: 991,
  },
  'screen-lg': {
    minWidth: 992,
    maxWidth: 1199,
  },
  'screen-xl': {
    minWidth: 1200,
  },
};

let isMobile;
enquireScreen((b) => {
  isMobile = b;
});

class BasicLayout extends React.PureComponent {
  static childContextTypes = {
    location: PropTypes.object,
    breadcrumbNameMap: PropTypes.object,
  }
  state = {
    isMobile,
    menuData: 1,
  };
  getChildContext() {
    const { location, routerData } = this.props;
    return {
      location,
      breadcrumbNameMap: routerData,
    };
  }

  componentDidMount() {
    getMenuDate().then((response =>
        this.setState({
          menuData: formatter(response),
        })
    ));
    enquireScreen((mobile) => {
      this.setState({
        isMobile: mobile,
      });
    });
    this.props.dispatch({
      type: 'appUser/fetchCurrent',
    });
    this.props.dispatch({
      type: 'global/find',
    });
  }
  getPageTitle() {
    const { routerData, location,globInfo } = this.props;
    const { pathname } = location;
    let title = globInfo.webSiteTitle;
    if (routerData[pathname] && routerData[pathname].name) {
      title = `${routerData[pathname].name} - `+globInfo.webSiteTitle;
    }
    return title;
  }
  getBashRedirect = () => {
    // According to the url parameter to redirect
    // 这里是重定向的,重定向到 url 的 redirect 参数所示地址
    const urlParams = new URL(window.location.href);

    const redirect = urlParams.searchParams.get('redirect');
    // Remove the parameters in the url
    if (redirect) {
      urlParams.searchParams.delete('redirect');
      window.history.replaceState(null, 'redirect', urlParams.href);
    } else {
      return '/member/user/list';
    }
    return redirect;
  }
  handleMenuCollapse = (collapsed) => {
    this.props.dispatch({
      type: 'global/changeLayoutCollapsed',
      payload: collapsed,
    });
  }
  handleNoticeClear = (type) => {
    message.success(`清空了${type}`);
    this.props.dispatch({
      type: 'global/clearNotices',
      payload: type,
    });
  }
  handleMenuClick = ({ key }) => {
    if (key === 'triggerError') {
      this.props.dispatch(routerRedux.push('/exception/trigger'));
      return;
    }
    if (key === 'logout') {
      this.props.dispatch({
        type: 'login/logout',
      });
    }
  }
  handleNoticeVisibleChange = (visible) => {
    if (visible) {
      this.props.dispatch({
        type: 'global/fetchNotices',
      });
    }
  }
  render() {
    const {
      currentUser, collapsed, fetchingNotices, notices,globInfo ,routerData, match, location,
    } = this.props;

    const bashRedirect = this.getBashRedirect();
    const layout = (
      <Layout>
        <SiderMenu
          logo={globInfo.fileHost+globInfo.manageLogo}
          title={globInfo.webSiteTitle}
          // 不带Authorized参数的情况下如果没有权限,会强制跳到403界面
          // If you do not have the Authorized parameter
          // you will be forced to jump to the 403 interface without permission
          Authorized={Authorized}
          menuData={this.state.menuData}
          collapsed={collapsed}
          location={location}
          isMobile={this.state.isMobile}
          onCollapse={this.handleMenuCollapse}
        />
        <Layout>
          <Header style={{ padding: 0 }}>
            <GlobalHeader
              logo={logo}
              currentUser={currentUser}
              fetchingNotices={fetchingNotices}
              notices={notices}
              collapsed={collapsed}
              isMobile={this.state.isMobile}
              onNoticeClear={this.handleNoticeClear}
              onCollapse={this.handleMenuCollapse}
              onMenuClick={this.handleMenuClick}
              onNoticeVisibleChange={this.handleNoticeVisibleChange}
            />
          </Header>
          <Content style={{ margin: '24px 24px 0', height: '100%' }}>
            <Switch>
              {
                redirectData.map(item =>
                  <Redirect key={item.from} exact from={item.from} to={item.to} />
                )
              }
              {
                getRoutes(match.path, routerData).map(item =>
                  (
                    <AuthorizedRoute
                      key={item.key}
                      path={item.path}
                      component={item.component}
                      exact={item.exact}
                      authority={item.authority}
                      redirectPath="/exception/403"
                    />
                  )
                )
              }
              <Redirect exact from="/" to={bashRedirect} />
              <Route render={NotFound} />
            </Switch>
          </Content>
          <Footer style={{ padding: 0 }}>
            <GlobalFooter
              /*links={[{
                key: ' BatSoft Pro 首页',
                title: 'BatSoft Pro 首页',
                href: '/',
                blankTarget: true,
              }, {
                key: 'github',
                title: <Icon type="github" />,
                href: '/',
                blankTarget: true,
              }, {
                key: 'BatSoft Pro',
                title: 'BatSoft Pro',
                href: '/',
                blankTarget: true,
              }]}*/
              copyright={
                <div>
                  Copyright <Icon type="copyright" /> {globInfo===undefined?"":globInfo.copyright}
                </div>
              }
            />
          </Footer>
        </Layout>
      </Layout>
    );

    return (
      <DocumentTitle title={this.getPageTitle()}>
        {
          this.state.menuData !== 1 ? (
            <ContainerQuery query={query}>
              {params => <div className={classNames(params)}>{layout}</div>}
            </ContainerQuery>
          ) : null
        }
      </DocumentTitle>
    );
  }
}

export default connect(({ appUser, global, loading }) => ({
  currentUser: appUser.currentUser.data,
  collapsed: global.collapsed,
  fetchingNotices: loading.effects['global/fetchNotices'],
  notices: global.notices,
  globInfo:global.globInfo,
}))(BasicLayout);
