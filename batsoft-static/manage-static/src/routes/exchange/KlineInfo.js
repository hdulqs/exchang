import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({kline}) => ({
  kline,
}))
export default class KlineInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('kline/find',{id: match.params.id});
    }
  }

  render() {
    const { kline:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="交易对">{findData.data.coinPair}</Description>
     <Description term="时区">{findData.data.klineTime}</Description>
     <Description term="K线数据">{findData.data.klineData}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
