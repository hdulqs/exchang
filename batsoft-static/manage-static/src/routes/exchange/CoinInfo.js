import React from 'react';
import {connect} from 'dva';
import {Card} from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';

const {Description} = DescriptionList;
@connect(({coin}) => ({
  coin,
}))
export default class CoinInfo extends BaseAction {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('coin/find', {id: match.params.id});
    }
  }

  render() {
    const {coin: {findData}} = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large">
            <Description term="币种名称">{findData.data.coinName}</Description>
            <Description term="币种代码">{findData.data.coinCode}</Description>
            <Description term="发行方名称">{findData.data.issuerName}</Description>
            <Description term="发行总额">{findData.data.totleMoney}</Description>
            <Description term="发行总量">{findData.data.totleNumber}</Description>
            <Description term="发行单价">{findData.data.price}</Description>
            <Description term="logo">{findData.data.coinLogo}</Description>
            <Description term="是否定价币">{findData.data.priceCoin}</Description>
            <Description term="是否erc20代币">{findData.data.erc20Status}</Description>
            <Description term="结算位数">{findData.data.calculationLen}</Description>
            <Description term="允许充值">{findData.data.allowRecharge}</Description>
            <Description term="允许提现">{findData.data.allowWithdraw}</Description>

            <Description term="基础手续费">{findData.data.withdrawBaseFee}</Description>
            <Description term="手续费类型">{findData.data.withdrawFeeType==0?'固定手续费':'百分比'}</Description>
            <Description term="最小提币数量">{findData.data.withdrawMin}</Description>
            <Description term="单日最大提币数量">{findData.data.withdrawMax}</Description>
            <Description term="未实名单日最大提币数量">{findData.data.noAuthWithdrawMax}</Description>
            <Description term="单笔小于多少无需审核">{findData.data.withdrawAcMaxAmt}</Description>

            <Description term="充手续费%">{findData.data.chargeRate}</Description>
            <Description term="提手续费%">{findData.data.withdrawRate}</Description>
            <Description term="卖手续费%">{findData.data.sellRate}</Description>
            <Description term="买手续费%">{findData.data.buyRate}</Description>
            <Description term="排序">{findData.data.sort}</Description>
            <Description term="充值节点确认个数">{findData.data.coinConfirm}</Description>
            <Description term="最大成交量">{findData.data.maxNum}</Description>
            <Description term="最小成交量">{findData.data.minNum}</Description>
            <Description term="证明方式">{findData.data.prove}</Description>
            <Description term="区块最小值">{findData.data.minBlock}</Description>
            <Description term="区块最大值">{findData.data.maxBlock}</Description>
            <Description term="区块多少秒产生一个">{findData.data.prodSpeed}</Description>
            <Description term="钱包地址">{findData.data.tokenUrl}</Description>
            <Description term="源码地址">{findData.data.sourceUrl}</Description>
            <Description term="区块浏览器">{findData.data.blockBrow}</Description>
            <Description term="官网地址">{findData.data.officialWeb}</Description>
            <Description term="官网论坛">{findData.data.officialForum}</Description>
            <Description term="挖矿地址">{findData.data.miningUrl}</Description>
            <Description term="状态">{findData.data.status}</Description>
            <Description term="算法说明">{findData.data.algorithm}</Description>
            <Description term="产品介绍">{findData.data.productIntroduction}</Description>


          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
