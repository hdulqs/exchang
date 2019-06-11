import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({bankcard}) => ({
  bankcard,
}))
export default class BankcardInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('bankcard/find',{id: match.params.id});
    }
  }

  render() {
    const { bankcard:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="用户ID">{findData.data.userId}</Description>
     <Description term="用户名">{findData.data.userName}</Description>
     <Description term="银行ID">{findData.data.bankId}</Description>
     <Description term="银行卡号">{findData.data.bankCard}</Description>
     <Description term="银行名称">{findData.data.bankName}</Description>
     <Description term="支行名称">{findData.data.branchName}</Description>
     <Description term="开户省">{findData.data.province}</Description>
     <Description term="开户市">{findData.data.city}</Description>
     <Description term="是否有效">{findData.data.status}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
