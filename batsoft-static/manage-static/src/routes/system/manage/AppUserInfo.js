import React from 'react';
import {connect} from 'dva';
import {Card} from 'antd';
import PageHeaderLayout from '../../../layouts/PageHeaderLayout';
import DescriptionList from '../../../components/DescriptionList';
import BaseAction from '../../../components/BatSoft';

const {Description} = DescriptionList;
@connect(({appUser}) => ({
  appUser,
}))
export default class AppUserInfo extends BaseAction {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('appUser/find', {id: match.params.id});
    }
  }

  render() {
    const {appUser: {findData}} = this.props;

    const roles = [];
    if (findData.data.roles != undefined) {
      findData.data.roles.forEach(item => {
        roles.push(item.name);
      })
    }
    return (
      <PageHeaderLayout>
        <Card bordered={false}>
          <DescriptionList size="large">
            <Description term="工号">{findData.data.officeId}</Description>
            <Description term="所属角色">{roles.join(",")}</Description>
            <Description term="用户名">{findData.data.userName}</Description>
            <Description term="真实姓名">{findData.data.realName}</Description>
            <Description term="手机号">{findData.data.mobile}</Description>
            <Description term="固定电话">{findData.data.phone}</Description>
            <Description term="邮箱">{findData.data.email}</Description>
            <Description term="是否超级管理员">{findData.data.superUser}</Description>
            <Description term="是否禁用">{findData.data.available}</Description>
            <Description term="备注">{findData.data.remark}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
