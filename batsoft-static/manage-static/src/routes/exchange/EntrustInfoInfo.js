import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({entrustInfo}) => ({
  entrustInfo,
}))
export default class EntrustInfoInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('entrustInfo/find',{id: match.params.id});
    }
  }

  render() {
    const { entrustInfo:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="成交货币">{findData.data.coinCode}</Description>
     <Description term="委托单号">{findData.data.entrustOrder}</Description>
     <Description term="成交价格">{findData.data.entrustPrice}</Description>
     <Description term="成交数量">{findData.data.entrustAmout}</Description>
     <Description term="成交时间">{findData.data.entrustTime}</Description>
     <Description term="卖方账户">{findData.data.sellAccountId}</Description>
     <Description term="买方账户">{findData.data.buyAccountId}</Description>
     <Description term="卖方客户">{findData.data.sellCustomerId}</Description>
     <Description term="买方客户">{findData.data.buyCustomerId}</Description>
     <Description term="备注">{findData.data.remark}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
