import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({customerAccountRecord}) => ({
  customerAccountRecord,
}))
export default class CustomerAccountRecordInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('customerAccountRecord/find',{id: match.params.id});
    }
  }

  render() {
    const { customerAccountRecord:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="账户id">{findData.data.accountId}</Description>
     <Description term="客户id">{findData.data.customerId}</Description>
     <Description term="账户账号">{findData.data.accountNumber}</Description>
     <Description term="货币代码">{findData.data.coinCode}</Description>
     <Description term="订单id">{findData.data.orderId}</Description>
     <Description term="流水金额">{findData.data.money}</Description>
     <Description term="订单说明">{findData.data.remark}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
