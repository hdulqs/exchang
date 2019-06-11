import React from 'react';
import {connect} from 'dva';
import {Card} from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';

const {Description} = DescriptionList;
@connect(({user}) => ({
  user,
}))
export default class UserInfo extends BaseAction {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('user/find', {id: match.params.id});
    }
  }

  render() {
    const {user: {findData}} = this.props;
    return (
      <PageHeaderLayout>
        <Card bordered={false}>
          <DescriptionList size="large">
            <Description term="用户昵称">{findData.data.userNick}</Description>
            <Description term="用户名">{findData.data.userName}</Description>
            <Description term="登录密码">{findData.data.password}</Description>
            <Description term="密码盐">{findData.data.salt}</Description>
            <Description term="真实姓名">{findData.data.realName}</Description>
            <Description term="手机区域号">{findData.data.areaCode}</Description>
            <Description term="手机号">{findData.data.userMobile}</Description>
            <Description term="邮箱">{findData.data.userEmail}</Description>
            <Description term="状态">{findData.data.status}</Description>
            <Description term="证件类型">{findData.data.userCardType}</Description>
            <Description term="证件号">{findData.data.userCardNumber}</Description>
            <Description term="生日">{findData.data.userBirthday}</Description>
            <Description term="头像">{findData.data.userAvatar}</Description>
            <Description term="qq">{findData.data.userQq}</Description>
            <Description term="登录次数">{findData.data.loginNum}</Description>
            <Description term="当前登录时间">{findData.data.loginTime}</Description>
            <Description term="上次登录时间">{findData.data.oldLoginTime}</Description>
            <Description term="当前登录ip">{findData.data.loginIp}</Description>
            <Description term="上次登录ip">{findData.data.oldLoginIp}</Description>
            <Description term="证件证明照片">{findData.data.userCardFront}</Description>
            <Description term="证件反面照片">{findData.data.userCardBack}</Description>
            <Description term="是否实名">{findData.data.realState}</Description>
            <Description term="国家">{findData.data.country}</Description>
            <Description term="性别">{findData.data.sex}</Description>
            <Description term="交易密码">{findData.data.tradePassword}</Description>
            <Description term="google 验证码">{findData.data.googleCode}</Description>
            <Description term="手持证件照">{findData.data.userCardAll}</Description>
            <Description term="开启google认证">{findData.data.openGoogleAuth}</Description>
            <Description term="被信任人数">{findData.data.trustNum}</Description>
            <Description term="交易次数">{findData.data.exchangeNum}</Description>
            <Description term="备注或简介">{findData.data.remark}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
