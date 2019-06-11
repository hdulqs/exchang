import React from 'react';
import {connect} from 'dva';
import {Card} from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';

const {Description} = DescriptionList;
@connect(({depositWallet}) => ({
  depositWallet,
}))
export default class DepositWalletInfo extends BaseAction {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('depositWallet/find', {id: match.params.id});
    }
  }

  render() {
    const {depositWallet: {findData}} = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large">
            <Description term="钱包类型">{findData.data.walletType}</Description>
            <Description term="币代码">{findData.data.coinCode}</Description>
            <Description term="提币地址">{findData.data.walletAddress}</Description>
            <Description term="币数量">{findData.data.walletAmount}</Description>
            <Description term="旷工费">{findData.data.fee}</Description>
            <Description term="ETH旷工费">{findData.data.gasPrice}</Description>
            <Description term="ETH旷工Limit">{findData.data.gasLimit}</Description>
            <Description term="解锁密码">{findData.data.lockPassword}</Description>
            <Description term="memo标签">{findData.data.memo}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
