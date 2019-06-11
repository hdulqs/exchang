import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({robotTrade}) => ({
  robotTrade,
}))
export default class RobotTradeInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('robotTrade/find',{id: match.params.id});
    }
  }

  render() {
    const { robotTrade:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="交易对">{findData.data.coinPair}</Description>
     <Description term="下单用户">{findData.data.userName}</Description>
     <Description term="最大下单数量">{findData.data.entrustNumMax}</Description>
     <Description term="最小下单数量">{findData.data.entrustNumMin}</Description>
     <Description term="下单时间最大">{findData.data.entrustTimeMax}</Description>
     <Description term="下单时间最小">{findData.data.entrustTimeMin}</Description>
     <Description term="最大下单价格">{findData.data.entrustPriceMax}</Description>
     <Description term="最小下单价格">{findData.data.entrustPriceMin}</Description>
     <Description term="步长">{findData.data.basePrice}</Description>
     <Description term="是否三方数据">{findData.data.fromThird}</Description>
     <Description term="api地址">{findData.data.thirdApi}</Description>
     <Description term="运行状态">{findData.data.status}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
