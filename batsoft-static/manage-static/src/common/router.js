import {createElement} from 'react';
import dynamic from 'dva/dynamic';
import pathToRegexp from 'path-to-regexp';
import {getMenuDate} from '../services/api';
import {formatter} from '../common/menu';
import {getAuthority} from '../utils/authority';

let routerDataCache;

const modelNotExisted = (app, model) => (
  // eslint-disable-next-line
  !app._models.some(({namespace}) => {
    return namespace === model.substring(model.lastIndexOf('/') + 1);
  })
);

// wrapper of dynamic
const dynamicWrapper = (app, models, component) => {
  // () => require('module')
  // transformed by babel-plugin-dynamic-import-node-sync
  if (component.toString().indexOf('.then(') < 0) {
    models.forEach((model) => {
      if (modelNotExisted(app, model)) {
        // eslint-disable-next-line
        app.model(require(`../models/${model}`).default);
      }
    });
    return (props) => {
      if (!routerDataCache) {
        routerDataCache = getRouterData(app);
      }
      return createElement(component().default, {
        ...props,
        routerData: routerDataCache,
      });
    };
  }
  // () => import('module')
  return dynamic({
    app,
    models: () => models.filter(
      model => modelNotExisted(app, model)).map(m => import(`../models/${m}.js`)
    ),
    // add routerData prop
    component: () => {
      if (!routerDataCache) {
        routerDataCache = getRouterData(app);
      }
      return component().then((raw) => {
        const Component = raw.default || raw;
        return props => createElement(Component, {
          ...props,
          routerData: routerDataCache,
        });
      });
    },
  });
};

function getFlatMenuData(menus) {
  let keys = {};
  if (menus !== undefined) {
    menus.forEach((item) => {
      if (item.children) {
        keys[item.path] = {...item};
        keys = {...keys, ...getFlatMenuData(item.children)};
      } else {
        keys[item.path] = {...item};
      }
    });
  }
  return keys;
}

export const getRouterData = (app) => {
  const routerConfig = {
    '/': {
      component: dynamicWrapper(app, ['system/manage/appUser', 'login'], () => import('../layouts/BasicLayout')),
    },
    '/home/desktop': {
      component: dynamicWrapper(app, ['chart'], () => import('../routes/home/Desktop')),
    },
    '/exception/403': {
      component: dynamicWrapper(app, [], () => import('../routes/Exception/403')),
    },
    '/exception/404': {
      component: dynamicWrapper(app, [], () => import('../routes/Exception/404')),
    },
    '/exception/500': {
      component: dynamicWrapper(app, [], () => import('../routes/Exception/500')),
    },
    '/user': {
      component: dynamicWrapper(app, [], () => import('../layouts/UserLayout')),
    },
    '/user/register-result': {
      component: dynamicWrapper(app, [], () => import('../routes/RegisterResult')),
    },
    // '/user/:id': {
    //   component: dynamicWrapper(app, [], () => import('../routes/User/SomeComponent')),
    // },

    // login register
    '/login': {
      component: dynamicWrapper(app, ['login'], () => import('../routes/Login')),
    },
    '/register': {
      component: dynamicWrapper(app, ['register'], () => import('../routes/Register')),
    },
    // cms article app
    '/cms/article/list': {
      component: dynamicWrapper(app, ['cms/article'], () => import('../routes/cms/ArticleList')),
    },
    '/cms/article/add': {
      component: dynamicWrapper(app, ['cms/article','cms/articleChannel'], () => import('../routes/cms/ArticleForm')),
    },
    '/cms/article/edit/:id': {
      component: dynamicWrapper(app, ['cms/article'], () => import('../routes/cms/ArticleForm')),
    },
    '/cms/article/info/:id': {
      component: dynamicWrapper(app, ['cms/article'], () => import('../routes/cms/ArticleInfo')),
    },

    // cms single单页面
    '/cms/single/list': {
      component: dynamicWrapper(app, ['cms/single'], () => import('../routes/cms/SingleList')),
    },
    '/cms/single/add': {
      component: dynamicWrapper(app, ['cms/single','cms/articleChannel'], () => import('../routes/cms/SingleForm')),
    },
    '/cms/single/edit/:id': {
      component: dynamicWrapper(app, ['cms/single'], () => import('../routes/cms/SingleForm')),
    },
    '/cms/single/info/:id': {
      component: dynamicWrapper(app, ['cms/single'], () => import('../routes/cms/SingleInfo')),
    },

    // article Channel
    '/cms/articleChannel/list': {
      component: dynamicWrapper(app, ['cms/articleChannel'], () => import('../routes/cms/ArticleChannelList')),
    },
    '/cms/articleChannel/add': {
      component: dynamicWrapper(app, ['cms/articleChannel'], () => import('../routes/cms/ArticleChannelForm')),
    },
    '/cms/articleChannel/edit/:id': {
      component: dynamicWrapper(app, ['cms/articleChannel'], () => import('../routes/cms/ArticleChannelForm')),
    },
    '/cms/articleChannel/info/:id': {
      component: dynamicWrapper(app, ['cms/articleChannel'], () => import('../routes/cms/ArticleChannelInfo')),
    },

    // banner
    '/cms/banner/list': {
      component: dynamicWrapper(app, ['cms/banner'], () => import('../routes/cms/BannerList')),
    },
    '/cms/banner/add': {
      component: dynamicWrapper(app, ['cms/banner'], () => import('../routes/cms/BannerForm')),
    },
    '/cms/banner/edit/:id': {
      component: dynamicWrapper(app, ['cms/banner'], () => import('../routes/cms/BannerForm')),
    },
    '/cms/banner/info/:id': {
      component: dynamicWrapper(app, ['cms/banner'], () => import('../routes/cms/BannerInfo')),
    },

    // friend
    '/cms/friend/list': {
      component: dynamicWrapper(app, ['cms/friend'], () => import('../routes/cms/FriendList')),
    },
    '/cms/friend/add': {
      component: dynamicWrapper(app, ['cms/friend'], () => import('../routes/cms/FriendForm')),
    },
    '/cms/friend/edit/:id': {
      component: dynamicWrapper(app, ['cms/friend'], () => import('../routes/cms/FriendForm')),
    },
    '/cms/friend/info/:id': {
      component: dynamicWrapper(app, ['cms/friend'], () => import('../routes/cms/FriendInfo')),
    },

    // exchange app
    //币种管理
    '/exchange/coin/list': {
      component: dynamicWrapper(app, ['exchange/coin'], () => import('../routes/exchange/CoinList')),
    },
    '/exchange/coin/add': {
      component: dynamicWrapper(app, ['exchange/coin'], () => import('../routes/exchange/CoinForm')),
    },
    '/exchange/coin/edit/:id': {
      component: dynamicWrapper(app, ['exchange/coin'], () => import('../routes/exchange/CoinForm')),
    },
    '/exchange/coin/info/:id': {
      component: dynamicWrapper(app, ['exchange/coin'], () => import('../routes/exchange/CoinInfo')),
    },

    // 交易对
    '/exchange/coinPair/list': {
      component: dynamicWrapper(app, ['exchange/coinPair'], () => import('../routes/exchange/CoinPairList')),
    },
    '/exchange/coinPair/add': {
      component: dynamicWrapper(app, ['exchange/coinPair'], () => import('../routes/exchange/CoinPairForm')),
    },
    '/exchange/coinPair/edit/:id': {
      component: dynamicWrapper(app, ['exchange/coinPair'], () => import('../routes/exchange/CoinPairForm')),
    },
    '/exchange/coinPair/info/:id': {
      component: dynamicWrapper(app, ['exchange/coinPair'], () => import('../routes/exchange/CoinPairInfo')),
    },

    // kline
    //K线管理
    '/exchange/kline/list': {
      component: dynamicWrapper(app, ['exchange/kline'], () => import('../routes/exchange/KlineList')),
    },
    '/exchange/kline/add': {
      component: dynamicWrapper(app, ['exchange/kline'], () => import('../routes/exchange/KlineForm')),
    },
    '/exchange/kline/edit/:id': {
      component: dynamicWrapper(app, ['exchange/kline'], () => import('../routes/exchange/KlineForm')),
    },
    '/exchange/kline/info/:id': {
      component: dynamicWrapper(app, ['exchange/kline'], () => import('../routes/exchange/KlineInfo')),
    },


    // c2c交易订单
    '/exchange_c2c/c2cOrder/list': {
      component: dynamicWrapper(app, ['exchange/c2cOrder'], () => import('../routes/exchange/C2cOrderList')),
    },
    '/exchange_c2c/c2cOrder/add': {
      component: dynamicWrapper(app, ['exchange/c2cOrder'], () => import('../routes/exchange/C2cOrderForm')),
    },
    '/exchange_c2c/c2cOrder/edit/:id': {
      component: dynamicWrapper(app, ['exchange/c2cOrder'], () => import('../routes/exchange/C2cOrderForm')),
    },
    '/exchange_c2c/c2cOrder/info/:id': {
      component: dynamicWrapper(app, ['exchange/c2cOrder'], () => import('../routes/exchange/C2cOrderInfo')),
    },

    // c2c商户管理
    '/exchange_c2c/c2cUser/list': {
      component: dynamicWrapper(app, ['exchange/c2cUser'], () => import('../routes/exchange/C2cUserList')),
    },
    '/exchange_c2c/c2cUser/add': {
      component: dynamicWrapper(app, ['exchange/c2cUser'], () => import('../routes/exchange/C2cUserForm')),
    },
    '/exchange_c2c/c2cUser/edit/:id': {
      component: dynamicWrapper(app, ['exchange/c2cUser'], () => import('../routes/exchange/C2cUserForm')),
    },
    '/exchange_c2c/c2cUser/info/:id': {
      component: dynamicWrapper(app, ['exchange/c2cUser'], () => import('../routes/exchange/C2cUserInfo')),
    },
  // c2c代币管理
    '/exchange_c2c/c2cProduct/list': {
      component: dynamicWrapper(app, ['exchange/c2cProduct'], () => import('../routes/exchange/C2cProductList')),
    },
    '/exchange_c2c/c2cProduct/add': {
      component: dynamicWrapper(app, ['exchange/c2cProduct'], () => import('../routes/exchange/C2cProductForm')),
    },
    '/exchange_c2c/c2cProduct/edit/:id': {
      component: dynamicWrapper(app, ['exchange/c2cProduct'], () => import('../routes/exchange/C2cProductForm')),
    },
    '/exchange_c2c/c2cProduct/info/:id': {
      component: dynamicWrapper(app, ['exchange/c2cProduct'], () => import('../routes/exchange/C2cProductInfo')),
    },

    // 币账户
    '/exchange_finance/customerAccount/list': {
      component: dynamicWrapper(app, ['exchange/customerAccount'], () => import('../routes/exchange/CustomerAccountList')),
    },
    '/exchange_finance/customerAccount/add': {
      component: dynamicWrapper(app, ['exchange/customerAccount'], () => import('../routes/exchange/CustomerAccountForm')),
    },
    '/exchange_finance/customerAccount/edit/:id': {
      component: dynamicWrapper(app, ['exchange/customerAccount'], () => import('../routes/exchange/CustomerAccountForm')),
    },
    '/exchange_finance/customerAccount/info/:id': {
      component: dynamicWrapper(app, ['exchange/customerAccount'], () => import('../routes/exchange/CustomerAccountInfo')),
    },

    // 币账户核算
    '/exchange_finance/customerAccount/userList': {
      component: dynamicWrapper(app, ['exchange/customerAccount'], () => import('../routes/exchange/UserList')),
    },
    '/exchange_finance/customerAccount/check/:id': {
      component: dynamicWrapper(app, ['exchange/customerAccount'], () => import('../routes/exchange/CheckAccount')),
    },

    // 冻结明细
    '/exchange_finance/customerAccountFreeze/list': {
      component: dynamicWrapper(app, ['exchange/customerAccountFreeze'], () => import('../routes/exchange/CustomerAccountFreezeList')),
    },
    '/exchange_finance/customerAccountFreeze/add': {
      component: dynamicWrapper(app, ['exchange/customerAccountFreeze'], () => import('../routes/exchange/CustomerAccountFreezeForm')),
    },
    '/exchange_finance/customerAccountFreeze/edit/:id': {
      component: dynamicWrapper(app, ['exchange/customerAccountFreeze'], () => import('../routes/exchange/CustomerAccountFreezeForm')),
    },
    '/exchange_finance/customerAccountFreeze/info/:id': {
      component: dynamicWrapper(app, ['exchange/customerAccountFreeze'], () => import('../routes/exchange/CustomerAccountFreezeInfo')),
    },

    // 交易订单
    '/exchange_finance/customerAccountOrder/list': {
      component: dynamicWrapper(app, ['exchange/customerAccountOrder'], () => import('../routes/exchange/CustomerAccountOrderList')),
    },
    '/exchange_finance/customerAccountOrder/add': {
      component: dynamicWrapper(app, ['exchange/customerAccountOrder'], () => import('../routes/exchange/CustomerAccountOrderForm')),
    },
    '/exchange_finance/customerAccountOrder/edit/:id': {
      component: dynamicWrapper(app, ['exchange/customerAccountOrder'], () => import('../routes/exchange/CustomerAccountOrderForm')),
    },
    '/exchange_finance/customerAccountOrder/info/:id': {
      component: dynamicWrapper(app, ['exchange/customerAccountOrder'], () => import('../routes/exchange/CustomerAccountOrderInfo')),
    },

    //充币管理
    '/exchange_finance/coinRecharge/list': {
      component: dynamicWrapper(app, ['exchange/coinRecharge'], () => import('../routes/exchange/CoinRechargeList')),
    },
    '/exchange_finance/coinRecharge/add': {
      component: dynamicWrapper(app, ['exchange/coinRecharge'], () => import('../routes/exchange/CoinRechargeForm')),
    },
    '/exchange_finance/coinRecharge/edit/:id': {
      component: dynamicWrapper(app, ['exchange/coinRecharge'], () => import('../routes/exchange/CoinRechargeForm')),
    },
    '/exchange_finance/coinRecharge/info/:id': {
      component: dynamicWrapper(app, ['exchange/coinRecharge'], () => import('../routes/exchange/CoinRechargeInfo')),
    },

    //提币管理
    '/exchange_finance/coinWithdraw/list': {
      component: dynamicWrapper(app, ['exchange/coinWithdraw'], () => import('../routes/exchange/CoinWithdrawList')),
    },
    '/exchange_finance/coinWithdraw/add': {
      component: dynamicWrapper(app, ['exchange/coinWithdraw'], () => import('../routes/exchange/CoinWithdrawForm')),
    },
    '/exchange_finance/coinWithdraw/edit/:id': {
      component: dynamicWrapper(app, ['exchange/coinWithdraw'], () => import('../routes/exchange/CoinWithdrawForm')),
    },
    '/exchange_finance/coinWithdraw/info/:id': {
      component: dynamicWrapper(app, ['exchange/coinWithdraw'], () => import('../routes/exchange/CoinWithdrawInfo')),
    },

    //提币地址管理
    '/exchange_finance/withdrawAddress/list': {
      component: dynamicWrapper(app, ['exchange/withdrawAddress'], () => import('../routes/exchange/WithdrawAddressList')),
    },
    '/exchange_finance/withdrawAddress/add': {
      component: dynamicWrapper(app, ['exchange/withdrawAddress'], () => import('../routes/exchange/WithdrawAddressForm')),
    },
    '/exchange_finance/withdrawAddress/edit/:id': {
      component: dynamicWrapper(app, ['exchange/withdrawAddress'], () => import('../routes/exchange/WithdrawAddressForm')),
    },
    '/exchange_finance/withdrawAddress/info/:id': {
      component: dynamicWrapper(app, ['exchange/withdrawAddress'], () => import('../routes/exchange/WithdrawAddressInfo')),
    },


    // 成交明细
    '/exchange_finance/customerAccountRecord/list': {
      component: dynamicWrapper(app, ['exchange/customerAccountRecord'], () => import('../routes/exchange/CustomerAccountRecordList')),
    },
    '/exchange_finance/customerAccountRecord/add': {
      component: dynamicWrapper(app, ['exchange/customerAccountRecord'], () => import('../routes/exchange/CustomerAccountRecordForm')),
    },
    '/exchange_finance/customerAccountRecord/edit/:id': {
      component: dynamicWrapper(app, ['exchange/customerAccountRecord'], () => import('../routes/exchange/CustomerAccountRecordForm')),
    },
    '/exchange_finance/customerAccountRecord/info/:id': {
      component: dynamicWrapper(app, ['exchange/customerAccountRecord'], () => import('../routes/exchange/CustomerAccountRecordInfo')),
    },

    // 历史委托
    '/exchange_entrust/entrustHistory/list': {
      component: dynamicWrapper(app, ['exchange/entrustHistory'], () => import('../routes/exchange/EntrustHistoryList')),
    },
    '/exchange_entrust/entrustHistory/add': {
      component: dynamicWrapper(app, ['exchange/entrustHistory'], () => import('../routes/exchange/EntrustHistoryForm')),
    },
    '/exchange_entrust/entrustHistory/edit/:id': {
      component: dynamicWrapper(app, ['exchange/entrustHistory'], () => import('../routes/exchange/EntrustHistoryForm')),
    },
    '/exchange_entrust/entrustHistory/info/:id': {
      component: dynamicWrapper(app, ['exchange/entrustHistory'], () => import('../routes/exchange/EntrustHistoryInfo')),
    },


    // 当前委托
    '/exchange_entrust/entrustIng/list': {
      component: dynamicWrapper(app, ['exchange/entrustIng'], () => import('../routes/exchange/EntrustIngList')),
    },
    '/exchange_entrust/entrustIng/add': {
      component: dynamicWrapper(app, ['exchange/entrustIng'], () => import('../routes/exchange/EntrustIngForm')),
    },
    '/exchange_entrust/entrustIng/edit/:id': {
      component: dynamicWrapper(app, ['exchange/entrustIng'], () => import('../routes/exchange/EntrustIngForm')),
    },
    '/exchange_entrust/entrustIng/info/:id': {
      component: dynamicWrapper(app, ['exchange/entrustIng'], () => import('../routes/exchange/EntrustIngInfo')),
    },

    // 委托详情
    '/exchange_entrust/entrustInfo/list': {
      component: dynamicWrapper(app, ['exchange/entrustInfo'], () => import('../routes/exchange/EntrustInfoList')),
    },
    '/exchange_entrust/entrustInfo/add': {
      component: dynamicWrapper(app, ['exchange/entrustInfo'], () => import('../routes/exchange/EntrustInfoForm')),
    },
    '/exchange_entrust/entrustInfo/edit/:id': {
      component: dynamicWrapper(app, ['exchange/entrustInfo'], () => import('../routes/exchange/EntrustInfoForm')),
    },
    '/exchange_entrust/entrustInfo/info/:id': {
      component: dynamicWrapper(app, ['exchange/entrustInfo'], () => import('../routes/exchange/EntrustInfoInfo')),
    },
    //***********************************机器人系统***************************************

    // 委托详情
    '/exchange_robot/robotTrade/list': {
      component: dynamicWrapper(app, ['exchange/robotTrade'], () => import('../routes/exchange/RobotTradeList')),
    },
    '/exchange_robot/robotTrade/add': {
      component: dynamicWrapper(app, ['exchange/robotTrade'], () => import('../routes/exchange/RobotTradeForm')),
    },
    '/exchange_robot/robotTrade/edit/:id': {
      component: dynamicWrapper(app, ['exchange/robotTrade'], () => import('../routes/exchange/RobotTradeForm')),
    },
    '/exchange_robot/robotTrade/info/:id': {
      component: dynamicWrapper(app, ['exchange/robotTrade'], () => import('../routes/exchange/RobotTradeInfo')),
    },

    //***********************************权限系统***************************************************

    //前端导航管理
    '/security/navigation/appNavigation/list': {
      component: dynamicWrapper(app, ['system/navigation/appNavigation'], () => import('../routes/system/navigation/AppNavigationList')),
    },
    '/security/navigation/appNavigation/add/:pid': {
      component: dynamicWrapper(app, ['system/navigation/appNavigation'], () => import('../routes/system/navigation/AppNavigationForm')),
    },
    '/security/navigation/appNavigation/edit/:id': {
      component: dynamicWrapper(app, ['system/navigation/appNavigation'], () => import('../routes/system/navigation/AppNavigationForm')),
    },

    //后台权限菜单
    '/security/appmenu/appMenu/list': {
      component: dynamicWrapper(app, ['system/appmenu/appMenu'], () => import('../routes/system/appmenu/AppMenuList')),
    },
    '/security/appmenu/appMenu/add/:pid': {
      component: dynamicWrapper(app, ['system/appmenu/appMenu'], () => import('../routes/system/appmenu/AppMenuForm')),
    },
    '/security/appmenu/appMenu/edit/:id': {
      component: dynamicWrapper(app, ['system/appmenu/appMenu'], () => import('../routes/system/appmenu/AppMenuForm')),
    },
    // system/manage appuser
    '/security/manage/appUser/list': {
      component: dynamicWrapper(app, ['system/manage/appUser'], () => import('../routes/system/manage/AppUserList')),
    },
    '/security/manage/appUser/add': {
      component: dynamicWrapper(app, ['system/manage/appUser', 'system/manage/appRole'], () => import('../routes/system/manage/AppUserForm')),
    },
    '/security/manage/appUser/edit/:id': {
      component: dynamicWrapper(app, ['system/manage/appUser', 'system/manage/appRole'], () => import('../routes/system/manage/AppUserForm')),
    },
    '/security/manage/appUser/info/:id': {
      component: dynamicWrapper(app, ['system/manage/appUser'], () => import('../routes/system/manage/AppUserInfo')),
    },
    // system/manage app role
    '/security/manage/appRole/list': {
      component: dynamicWrapper(app, ['system/manage/appRole','system/appmenu/appMenu'], () => import('../routes/system/manage/AppRoleList')),
    },
    '/security/manage/appRole/add': {
      component: dynamicWrapper(app, ['system/manage/appRole'], () => import('../routes/system/manage/AppRoleForm')),
    },
    '/security/manage/appRole/edit/:id': {
      component: dynamicWrapper(app, ['system/manage/appRole'], () => import('../routes/system/manage/AppRoleForm')),
    },
    '/security/manage/appRole/info/:id': {
      component: dynamicWrapper(app, ['system/manage/appRole'], () => import('../routes/system/manage/AppRoleInfo')),
    },
    //***********************************系统管理***************************************************
    // 系统配置
    '/system/config/appConfigType/list': {
      component: dynamicWrapper(app, ['system/config/appConfigType'], () => import('../routes/system/config/AppConfigTypeList')),
    },
    '/system/config/appConfigType/add': {
      component: dynamicWrapper(app, ['system/config/appConfigType'], () => import('../routes/system/config/AppConfigTypeForm')),
    },
    '/system/config/appConfigType/edit/:id': {
      component: dynamicWrapper(app, ['system/config/appConfigType'], () => import('../routes/system/config/AppConfigTypeForm')),
    },
    '/system/config/appConfigType/info/:id': {
      component: dynamicWrapper(app, ['system/config/appConfigType'], () => import('../routes/system/config/AppConfigTypeInfo')),
    },

    '/system/config/appConfig/setting': {
      component: dynamicWrapper(app, ['system/config/appConfig', 'system/config/appConfigType'], () => import('../routes/system/config/AppConfigSetForm')),
    },
    '/system/config/appConfig/list': {
      component: dynamicWrapper(app, ['system/config/appConfig'], () => import('../routes/system/config/AppConfigList')),
    },
    '/system/config/appConfig/add': {
      component: dynamicWrapper(app, ['system/config/appConfig', 'system/config/appConfigType'], () => import('../routes/system/config/AppConfigForm')),
    },
    '/system/config/appConfig/edit/:id': {
      component: dynamicWrapper(app, ['system/config/appConfig', 'system/config/appConfigType'], () => import('../routes/system/config/AppConfigForm')),
    },
    '/system/config/appConfig/info/:id': {
      component: dynamicWrapper(app, ['system/config/appConfig'], () => import('../routes/system/config/AppConfigInfo')),
    },

    // 系统缓存管理 models 中不能直接用cache 可能和系统缓存冲突，所以这里叫 cache1
    '/system/cache/appCache/list': {
      component: dynamicWrapper(app, ['system/cache1/appCache'], () => import('../routes/system/cache/AppCacheList')),
    },
    '/system/cache/appCache/add': {
      component: dynamicWrapper(app, ['system/cache1/appCache'], () => import('../routes/system/cache/AppCacheForm')),
    },
    '/system/cache/appCache/edit/:id': {
      component: dynamicWrapper(app, ['system/cache1/appCache'], () => import('../routes/system/cache/AppCacheForm')),
    },
    '/system/cache/appCache/info/:id': {
      component: dynamicWrapper(app, ['system/cache1/appCache'], () => import('../routes/system/cache/AppCacheInfo')),
    },

    // 数据字典管理
    '/system/type/appDictionary/list': {
      component: dynamicWrapper(app, ['system/type/appDictionary'], () => import('../routes/system/type/AppDictionaryList')),
    },
    '/system/type/appDictionary/add/:pid': {
      component: dynamicWrapper(app, ['system/type/appDictionary'], () => import('../routes/system/type/AppDictionaryForm')),
    },
    '/system/type/appDictionary/edit/:id': {
      component: dynamicWrapper(app, ['system/type/appDictionary'], () => import('../routes/system/type/AppDictionaryForm')),
    },

    //***********************************会员管理***************************************************

    '/member/user/list': {
      component: dynamicWrapper(app, ['member/user'], () => import('../routes/member/UserList')),
    },
    '/member/user/add': {
      component: dynamicWrapper(app, ['member/user'], () => import('../routes/member/UserForm')),
    },
    '/member/user/edit/:id': {
      component: dynamicWrapper(app, ['member/user'], () => import('../routes/member/UserForm')),
    },
    '/member/user/info/:id': {
      component: dynamicWrapper(app, ['member/user'], () => import('../routes/member/UserInfo')),
    },

    //***********************************会员银行账号管理***************************************************

    '/member/bankcard/list': {
      component: dynamicWrapper(app, ['member/bankcard'], () => import('../routes/member/BankcardList')),
    },
    '/member/bankcard/add': {
      component: dynamicWrapper(app, ['member/bankcard'], () => import('../routes/member/BankcardForm')),
    },
    '/member/bankcard/edit/:id': {
      component: dynamicWrapper(app, ['member/bankcard'], () => import('../routes/member/BankcardForm')),
    },
    '/member/bankcard/info/:id': {
      component: dynamicWrapper(app, ['member/bankcard'], () => import('../routes/member/BankcardInfo')),
    },

    // 资金账户管理
    '/member/finance/list': {
      component: dynamicWrapper(app, ['member/finance'], () => import('../routes/member/FinanceList')),
    },
    '/member/finance/add': {
      component: dynamicWrapper(app, ['member/finance'], () => import('../routes/member/FinanceForm')),
    },
    '/member/finance/edit/:id': {
      component: dynamicWrapper(app, ['member/finance'], () => import('../routes/member/FinanceForm')),
    },
    '/member/finance/info/:id': {
      component: dynamicWrapper(app, ['member/finance'], () => import('../routes/member/FinanceInfo')),
    },

    //***********************************日志管理***************************************************

    '/log/api/list': {
      component: dynamicWrapper(app, ['log/api'], () => import('../routes/log/ApiList')),
    },
    '/log/api/list/add': {
      component: dynamicWrapper(app, ['log/api'], () => import('../routes/log/ApiForm')),
    },
    '/log/api/edit/:id': {
      component: dynamicWrapper(app, ['log/api'], () => import('../routes/log/ApiForm')),
    },
    '/log/api/info/:id': {
      component: dynamicWrapper(app, ['log/api'], () => import('../routes/log/ApiInfo')),
    },

    //***********************************前台用户登录日志***************************************************

    '/log/memberLogin/list': {
      component: dynamicWrapper(app, ['log/memberLogin'], () => import('../routes/log/MemberLoginList')),
    },
    //***********************************短信发送日志管理***************************************************

    '/log/messageSend/list': {
      component: dynamicWrapper(app, ['log/messageSend'], () => import('../routes/log/MessageSendList')),
    },
    '/log/messageSend/info/:id': {
      component: dynamicWrapper(app, ['log/messageSend'], () => import('../routes/log/MessageSendInfo')),
    },

    //************************************区块钱包API******************************************

     //钱包服务器管理
    '/blockchain/wallet/list': {
      component: dynamicWrapper(app, ['blockchain/wallet'], () => import('../routes/blockchain/WalletList')),
    },
    '/blockchain/wallet/add': {
      component: dynamicWrapper(app, ['blockchain/wallet'], () => import('../routes/blockchain/WalletForm')),
    },
    '/blockchain/wallet/edit/:id': {
      component: dynamicWrapper(app, ['blockchain/wallet'], () => import('../routes/blockchain/WalletForm')),
    },
    '/blockchain/wallet/info/:id': {
      component: dynamicWrapper(app, ['blockchain/wallet'], () => import('../routes/blockchain/WalletInfo')),
    },

    //钱包地址管理
    '/blockchain/coinAccount/list': {
      component: dynamicWrapper(app, ['blockchain/coinAccount'], () => import('../routes/blockchain/CoinAccountList')),
    },
    '/blockchain/coinAccount/add': {
      component: dynamicWrapper(app, ['blockchain/coinAccount'], () => import('../routes/blockchain/CoinAccountForm')),
    },
    '/blockchain/coinAccount/edit/:id': {
      component: dynamicWrapper(app, ['blockchain/coinAccount'], () => import('../routes/blockchain/CoinAccountForm')),
    },
    '/blockchain/coinAccount/info/:id': {
      component: dynamicWrapper(app, ['blockchain/coinAccount'], () => import('../routes/blockchain/CoinAccountInfo')),
    },

    //钱包API用户管理
    '/blockchain/coinApiUser/list': {
      component: dynamicWrapper(app, ['blockchain/coinApiUser'], () => import('../routes/blockchain/CoinApiUserList')),
    },
    '/blockchain/coinApiUser/add': {
      component: dynamicWrapper(app, ['blockchain/coinApiUser'], () => import('../routes/blockchain/CoinApiUserForm')),
    },
    '/blockchain/coinApiUser/edit/:id': {
      component: dynamicWrapper(app, ['blockchain/coinApiUser'], () => import('../routes/blockchain/CoinApiUserForm')),
    },
    '/blockchain/coinApiUser/info/:id': {
      component: dynamicWrapper(app, ['blockchain/coinApiUser'], () => import('../routes/blockchain/CoinApiUserInfo')),
    },

    //钱包资金明细管理
    '/blockchain/coinOrder/list': {
      component: dynamicWrapper(app, ['blockchain/coinOrder'], () => import('../routes/blockchain/CoinOrderList')),
    },
    '/blockchain/coinOrder/add': {
      component: dynamicWrapper(app, ['blockchain/coinOrder'], () => import('../routes/blockchain/CoinOrderForm')),
    },
    '/blockchain/coinOrder/edit/:id': {
      component: dynamicWrapper(app, ['blockchain/coinOrder'], () => import('../routes/blockchain/CoinOrderForm')),
    },
    '/blockchain/coinOrder/info/:id': {
      component: dynamicWrapper(app, ['blockchain/coinOrder'], () => import('../routes/blockchain/CoinOrderInfo')),
    },
    //钱包API 调用日志
    '/blockchain/coinApiLog/list': {
      component: dynamicWrapper(app, ['blockchain/coinApiLog'], () => import('../routes/blockchain/CoinApiLogList')),
    },
    '/blockchain/coinApiLog/add': {
      component: dynamicWrapper(app, ['blockchain/coinApiLog'], () => import('../routes/blockchain/CoinApiLogForm')),
    },
    '/blockchain/coinApiLog/edit/:id': {
      component: dynamicWrapper(app, ['blockchain/coinApiLog'], () => import('../routes/blockchain/CoinApiLogForm')),
    },
    '/blockchain/coinApiLog/info/:id': {
      component: dynamicWrapper(app, ['blockchain/coinApiLog'], () => import('../routes/blockchain/CoinApiLogInfo')),
    },
    //钱包API 回调通知日志
    '/blockchain/coinNotify/list': {
      component: dynamicWrapper(app, ['blockchain/coinNotify'], () => import('../routes/blockchain/CoinNotifyList')),
    },
    '/blockchain/coinNotify/add': {
      component: dynamicWrapper(app, ['blockchain/coinNotify'], () => import('../routes/blockchain/CoinNotifyForm')),
    },
    '/blockchain/coinNotify/edit/:id': {
      component: dynamicWrapper(app, ['blockchain/coinNotify'], () => import('../routes/blockchain/CoinNotifyForm')),
    },
    '/blockchain/coinNotify/info/:id': {
      component: dynamicWrapper(app, ['blockchain/coinNotify'], () => import('../routes/blockchain/CoinNotifyInfo')),
    },

    //代币管理
    '/blockchain/ercCoin/list': {
      component: dynamicWrapper(app, ['blockchain/ercCoin'], () => import('../routes/blockchain/ErcCoinList')),
    },
    '/blockchain/ercCoin/add': {
      component: dynamicWrapper(app, ['blockchain/ercCoin'], () => import('../routes/blockchain/ErcCoinForm')),
    },
    '/blockchain/ercCoin/edit/:id': {
      component: dynamicWrapper(app, ['blockchain/ercCoin'], () => import('../routes/blockchain/ErcCoinForm')),
    },
    '/blockchain/ercCoin/info/:id': {
      component: dynamicWrapper(app, ['blockchain/ercCoin'], () => import('../routes/blockchain/ErcCoinInfo')),
    },

    //提币钱包管理
    '/blockchain/depositWallet/list': {
      component: dynamicWrapper(app, ['blockchain/depositWallet'], () => import('../routes/blockchain/DepositWalletList')),
    },
    '/blockchain/depositWallet/add': {
      component: dynamicWrapper(app, ['blockchain/depositWallet'], () => import('../routes/blockchain/DepositWalletForm')),
    },
    '/blockchain/depositWallet/edit/:id': {
      component: dynamicWrapper(app, ['blockchain/depositWallet'], () => import('../routes/blockchain/DepositWalletForm')),
    },
    '/blockchain/depositWallet/info/:id': {
      component: dynamicWrapper(app, ['blockchain/depositWallet'], () => import('../routes/blockchain/DepositWalletInfo')),
    },
  };
  // Get name from ./menu.js or just set it in the router data.
  /*const menuData = getMenuDate().then((responseText) => {
    getFlatMenuData(formatter(responseText));
  });*/
  const menuData = [];
  // Route configuration data
  // eg. {name,authority ...routerConfig }
  const routerData = {};
  // The route matches the menu
  Object.keys(routerConfig).forEach((path) => {
    // Regular match item name
    // eg.  router /user/:id === /user/chen
    const pathRegexp = pathToRegexp(path);
    const menuKey = Object.keys(menuData).find(key => pathRegexp.test(`/${key}`));
    let menuItem = {};
    // If menuKey is not empty
    if (menuKey) {
      menuItem = menuData[menuKey];
    }
    let router = routerConfig[path];
    // If you need to configure complex parameter routing,
    // https://github.com/ant-design/ant-design-pro-site/blob/master/docs/router-and-nav.md#%E5%B8%A6%E5%8F%82%E6%95%B0%E7%9A%84%E8%B7%AF%E7%94%B1%E8%8F%9C%E5%8D%95
    // eg . /list/:type/user/info/:id
    router = {
      ...router,
      name: router.name || menuItem.name,
      authority: router.authority || menuItem.authority,
    };
    routerData[path] = router;
  });
  return routerData;
};
