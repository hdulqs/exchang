import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({wallet}) => ({
  wallet,
}))
export default class WalletInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('wallet/find',{id: match.params.id});
    }
  }

  render() {
    const { wallet:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="钱包类型">{findData.data.walletType}</Description>
     <Description term="钱包名称">{findData.data.walletName}</Description>
     <Description term="钱包代码">{findData.data.walletCode}</Description>
     <Description term="协议">{findData.data.rpcProtocol}</Description>
     <Description term="钱包ip">{findData.data.rpcIp}</Description>
     <Description term="钱包账号">{findData.data.rpcAccount}</Description>
     <Description term="钱包密码">{findData.data.rpcPassword}</Description>
     <Description term="钱包端口">{findData.data.rpcPort}</Description>
     <Description term="钱包描述">{findData.data.walletRemark}</Description>
     <Description term="状态">{findData.data.status}</Description>
     <Description term="区块高度">{findData.data.blockHigh}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
