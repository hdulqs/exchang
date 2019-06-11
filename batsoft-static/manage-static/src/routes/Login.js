import React, {Component} from 'react';
import {connect} from 'dva';
import {Link} from 'dva/router';
import {Checkbox, Alert, Icon} from 'antd';
import Login from '../components/Login/index';
import styles from './Login.less';
import DocumentTitle from 'react-document-title';
import logo from '../assets/logo.svg';
import GlobalFooter from '../components/GlobalFooter/index';
import {md5} from '../utils/utils';
const {Tab, UserName, Password, Mobile, Captcha, Submit} = Login;

const links = [{
  key: 'help',
  title: '帮助',
  href: '',
}, {
  key: 'privacy',
  title: '隐私',
  href: '',
}, {
  key: 'terms',
  title: '条款',
  href: '',
}];



@connect(({global,login, loading}) => ({
  login,
  global,
  submitting: loading.effects['login/login'],
}))
export default class LoginPage extends Component {

  state = {
    type: 'account',
    rememberMe: true,
    validCode: '',
  }

  componentDidMount() {
    this.props.dispatch({
      type: 'global/find',
    });
  }

  onTabChange = (type) => {
    this.setState({type});
  }

  handleSubmit = (err, values) => {
    const {type, rememberMe, validCode} = this.state;
    if (!err) {
      values.password = md5(values.password);
      this.props.dispatch({
        type: 'login/login',
        payload: {
          ...values,
          rememberMe,
          validCode,
          type,
        },
      });
    }
  }

  changeAutoLogin = (e) => {
    this.setState({
      rememberMe: e.target.checked,
    });
  }

  renderMessage = (content) => {
    return (
      <Alert style={{marginBottom: 24}} message={content} type="error" showIcon/>
    );
  }

  render() {
    const {global:{globInfo},login, submitting} = this.props;
    //  const copyright = <div>Copyright <Icon type="copyright"/> {globInfo.copyright}</div>;
    // const copyright = <div>Copyright <Icon type="copyright"/> {globInfo.copyright}</div>;
    const copyright = <div>Copyright <Icon type="copyright"/>© 2017-2018 BttMall, All Rights Reserved. BttMall Technology Co.,Limited</div>;
    const {type} = this.state;
    return (
      <DocumentTitle title="Login">
        <div className={styles.container}>
          <div className={styles.content}>
            <div className={styles.top}>
              <div className={styles.header}>
                <Link to="/">
                  {/* <img alt="logo" className={styles.logo} src={globInfo.fileHost+globInfo.manageLogo}/> */}
                  <img alt="logo" className={styles.logo} src='http://ltalk-website.oss-cn-hangzhou.aliyuncs.com/upload/attach/20181207/0776c847054640d8bf019009e56f0089.png'/>
                  {/* <span className={styles.title}>{globInfo.webSiteTitle}</span> */}
                  <span className={styles.title}>BttMall</span>
                </Link>
              </div>
             {/* <div className={styles.desc}>{globInfo.webSiteTitle} 全球最专业的开发平台</div>*/}
            </div>
            <div className={styles.main}>
              <Login
                defaultActiveKey={type}
                onTabChange={this.onTabChange}
                onSubmit={this.handleSubmit}
              >
                <Tab key="account" tab="账户密码登录">
                  {
                    !login.status &&
                    login.type === 'account' &&
                    !login.submitting &&
                    this.renderMessage('账户或密码错误！')
                  }
                  <UserName name="userName" placeholder="请输入账户"/>
                  <Password name="password" placeholder="请输入密码"/>
                  {/* <Captcha name="validCode" placeholder="验证码"/>*/}
                </Tab>
                {/* <Tab key="mobile" tab="手机号登录">
                  {
                    login.status === 'error' &&
                    login.type === 'mobile' &&
                    !login.submitting &&
                    this.renderMessage('验证码错误')
                  }
                  <Mobile name="mobile" />
                  <Captcha name="captcha" />
                </Tab>*/}
                <div>
                  <Checkbox checked={this.state.rememberMe} onChange={this.changeAutoLogin}>记住我</Checkbox>
                  <a style={{float: 'right'}} href="">忘记密码</a>
                </div>
                <Submit loading={submitting}>登录</Submit>
                {/*<div className={styles.other}>
                  其他登录方式
                  <Icon className={styles.icon} type="alipay-circle" />
                  <Icon className={styles.icon} type="taobao-circle" />
                  <Icon className={styles.icon} type="weibo-circle" />
                  <Link className={styles.register} to="/user/register">注册账户</Link>
                </div>*/}
              </Login>
            </div>
          </div>
          <GlobalFooter links={links} copyright={copyright}/>
        </div>
      </DocumentTitle>

    );
  }
}
