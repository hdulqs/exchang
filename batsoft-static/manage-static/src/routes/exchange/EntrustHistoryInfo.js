import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({entrustHistory}) => ({
  entrustHistory,
}))
export default class EntrustHistoryInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('entrustHistory/find',{id: match.params.id});
    }
  }

  render() {
    const { entrustHistory:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="账户id">{findData.data.accountId}</Description>
     <Description term="客户id">{findData.data.customerId}</Description>
     <Description term="币代码">{findData.data.coinCode}</Description>
     <Description term="委托价格">{findData.data.entrustPrice}</Description>
     <Description term="委托数量">{findData.data.entrustAmout}</Description>
     <Description term="委托时间">{findData.data.entrustTime}</Description>
     <Description term="委托状态">{findData.data.entrustState}</Description>
     <Description term="已成交">{findData.data.tradedCoins}</Description>
     <Description term="成交总额">{findData.data.totalMoney}</Description>
     <Description term="成交均价">{findData.data.tradedAvg}</Description>
     <Description term="委托类别">{findData.data.category}</Description>
     <Description term="来源">{findData.data.orderFrom}</Description>
     <Description term="订单说明">{findData.data.remark}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
