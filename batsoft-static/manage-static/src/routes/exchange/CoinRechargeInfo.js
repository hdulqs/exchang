import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({coinRecharge}) => ({
  coinRecharge,
}))
export default class CoinRechargeInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('coinRecharge/find',{id: match.params.id});
    }
  }

  render() {
    const { coinRecharge:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="转入地址">{findData.data.inAddress}</Description>
     <Description term="接受地址">{findData.data.toAddress}</Description>
     <Description term="币种代码">{findData.data.coinCode}</Description>
     <Description term="转入数量">{findData.data.coinCount}</Description>
     <Description term="矿工费">{findData.data.fee}</Description>
     <Description term="状态">{findData.data.status}</Description>
     <Description term="交易单号">{findData.data.txOrder}</Description>
     <Description term="用户id">{findData.data.userId}</Description>
     <Description term="用户名">{findData.data.userName}</Description>
     <Description term="用户手机号">{findData.data.userMobile}</Description>
     <Description term="用户邮箱">{findData.data.userEmail}</Description>
     <Description term="备注">{findData.data.remark}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
