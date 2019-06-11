import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({coinOrder}) => ({
  coinOrder,
}))
export default class CoinOrderInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('coinOrder/find',{id: match.params.id});
    }
  }

  render() {
    const { coinOrder:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="币代码">{findData.data.coinCode}</Description>
     <Description term="from">{findData.data.fromAddress}</Description>
     <Description term="to_address">{findData.data.toAddress}</Description>
     <Description term="交易金额">{findData.data.amount}</Description>
     <Description term="手续费">{findData.data.fee}</Description>
     <Description term="区块链流水号">{findData.data.txHash}</Description>
     <Description term="交易类型">{findData.data.txType}</Description>
     <Description term="block_number">{findData.data.blockNumber}</Description>
     <Description term="transaction_index">{findData.data.transactionIndex}</Description>
     <Description term="block_hash">{findData.data.blockHash}</Description>
     <Description term="gas">{findData.data.gas}</Description>
     <Description term="gas_price">{findData.data.gasPrice}</Description>
     <Description term="是否确认">{findData.data.consume}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
