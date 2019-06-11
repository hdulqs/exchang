import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({coinApiUser}) => ({
  coinApiUser,
}))
export default class CoinApiUserInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('coinApiUser/find',{id: match.params.id});
    }
  }

  render() {
    const { coinApiUser:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="用户名">{findData.data.userName}</Description>
     <Description term="用户密码">{findData.data.userPassword}</Description>
     <Description term="用户秘钥">{findData.data.userKey}</Description>
     <Description term="用户ip">{findData.data.userIp}</Description>
     <Description term="联系方式">{findData.data.userPhone}</Description>
     <Description term="通知地址">{findData.data.notifyUrl}</Description>
     <Description term="状态">{findData.data.status}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
