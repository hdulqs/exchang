import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({customerAccountOrder}) => ({
  customerAccountOrder,
}))
export default class CustomerAccountOrderInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('customerAccountOrder/find',{id: match.params.id});
    }
  }

  render() {
    const { customerAccountOrder:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="货币账户id">{findData.data.accountId}</Description>
     <Description term="客户id">{findData.data.customerId}</Description>
     <Description term="账户账号">{findData.data.accountNumber}</Description>
     <Description term="币代码">{findData.data.coinCode}</Description>
     <Description term="方向">{findData.data.direction}</Description>
     <Description term="操作类型">{findData.data.operationType}</Description>
     <Description term="订单金额">{findData.data.opeationMoney}</Description>
     <Description term="交易时间">{findData.data.tradeTime}</Description>
     <Description term="订单状态">{findData.data.opeationState}</Description>
     <Description term="订单说明">{findData.data.remark}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
