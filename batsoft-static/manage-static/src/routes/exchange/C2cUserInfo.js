import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({c2cUser}) => ({
  c2cUser,
}))
export default class C2cUserInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('c2cUser/find',{id: match.params.id});
    }
  }

  render() {
    const { c2cUser:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="商户号">{findData.data.userName}</Description>
     <Description term="银行开户名">{findData.data.trueName}</Description>
     <Description term="手机号码">{findData.data.telephone}</Description>
     <Description term="商户证件号">{findData.data.userCard}</Description>
     <Description term="银行ID">{findData.data.bankId}</Description>
     <Description term="银行名称">{findData.data.bankName}</Description>
     <Description term="支行名称">{findData.data.branchName}</Description>
     <Description term="银行卡号">{findData.data.cardNumber}</Description>
     <Description term="币种名称">{findData.data.coinName}</Description>
     <Description term="币种代码">{findData.data.coinCode}</Description>
     <Description term="币种ID">{findData.data.coinId}</Description>
     <Description term="币种数量">{findData.data.coinNumber}</Description>
     <Description term="对人民币折算比例">{findData.data.coinProportion}</Description>
     <Description term="是否可用">{findData.data.status}</Description>
     <Description term="类型">{findData.data.type}</Description>
     <Description term="备注">{findData.data.remark}</Description>
     <Description term="排序">{findData.data.sort}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
