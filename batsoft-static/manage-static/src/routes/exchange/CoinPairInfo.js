import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({coinPair}) => ({
  coinPair,
}))
export default class CoinPairInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('coinPair/find',{id: match.params.id});
    }
  }

  render() {
    const { coinPair:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="交易币代码">{findData.data.tradeCoinCode}</Description>
     <Description term="定价币代码">{findData.data.pricingCoinCode}</Description>
     <Description term="交易币logo">{findData.data.tradeCoinLogo}</Description>
     <Description term="状态">{findData.data.status}</Description>
    <Description term="是否推荐">{findData.data.recommend}</Description>
    <Description term="挂单最小位数">{findData.data.amtDecimal}</Description>
    <Description term="单价最小位数">{findData.data.priceDecimal}</Description>
    <Description term="交易总额最小位数">{findData.data.amountDecimal}</Description>
    <Description term="发行价">{findData.data.openPrice}</Description>
     <Description term="排序">{findData.data.sort}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
