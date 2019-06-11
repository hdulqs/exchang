import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({c2cOrder}) => ({
  c2cOrder,
}))
export default class C2cOrderInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('c2cOrder/find',{id: match.params.id});
    }
  }

  render() {
    const { c2cOrder:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="账户ID">{findData.data.accountId}</Description>
     <Description term="用户id">{findData.data.userId}</Description>
     <Description term="用户名">{findData.data.userName}</Description>
     <Description term="币种">{findData.data.coinCode}</Description>
     <Description term="方向">{findData.data.direction}</Description>
     <Description term="操作类型">{findData.data.operationType}</Description>
     <Description term="订单金额">{findData.data.opeationMoney}</Description>
     <Description term="交易时间">{findData.data.tradeTime}</Description>
     <Description term="订单状态">{findData.data.opeationState}</Description>
     <Description term="资金转入方ID">{findData.data.transactionId}</Description>
     <Description term="银行名称">{findData.data.traBankname}</Description>
     <Description term="tra_bankcard">{findData.data.traBankcard}</Description>
     <Description term="交易流水号">{findData.data.traNumber}</Description>
     <Description term="订单说明">{findData.data.remark}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
