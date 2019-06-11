import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({c2cProduct}) => ({
  c2cProduct,
}))
export default class C2cProductInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('c2cProduct/find',{id: match.params.id});
    }
  }

  render() {
    const { c2cProduct:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="币种编码">{findData.data.coinCode}</Description>
     <Description term="最小交易金额">{findData.data.minNumber}</Description>
     <Description term="最大交易金额">{findData.data.maxNumber}</Description>
     <Description term="购买单价">{findData.data.buyPrice}</Description>
     <Description term="卖出单价">{findData.data.sellPrice}</Description>
     <Description term="是否有效">{findData.data.status}</Description>
     <Description term="排序">{findData.data.sort}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
