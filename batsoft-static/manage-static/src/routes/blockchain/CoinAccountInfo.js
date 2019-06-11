import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({coinAccount}) => ({
  coinAccount,
}))
export default class CoinAccountInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('coinAccount/find',{id: match.params.id});
    }
  }

  render() {
    const { coinAccount:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="钱包">{findData.data.walletId}</Description>
     <Description term="钱包code">{findData.data.walletCode}</Description>
     <Description term="币代码">{findData.data.coinCode}</Description>
     <Description term="币地址">{findData.data.coinAddress}</Description>
     <Description term="解锁密码">{findData.data.addressPassword}</Description>
     <Description term="memo">{findData.data.memo}</Description>
     <Description term="币数量">{findData.data.balance}</Description>
     <Description term="状态">{findData.data.status}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
