import React from 'react';
import {connect} from 'dva';
import {Card} from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';

const {Description} = DescriptionList;
@connect(({ercCoin}) => ({
  ercCoin,
}))
export default class ErcCoinInfo extends BaseAction {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('ercCoin/find', {id: match.params.id});
    }
  }

  render() {
    const {ercCoin: {findData}} = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large">
            <Description term="区块链类型">{findData.data.block}</Description>
            <Description term="代币Code">{findData.data.coinCode}</Description>
            <Description term="合约地址">{findData.data.coinAddress}</Description>
            <Description term="代币精度">{findData.data.coinDecimals}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
