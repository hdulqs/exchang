import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({customerAccount}) => ({
  customerAccount,
}))
export default class CustomerAccountInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('customerAccount/find',{id: match.params.id});
    }
  }

  render() {
    const { customerAccount:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="账户账号">{findData.data.accountNumber}</Description>
     <Description term="用户id">{findData.data.userId}</Description>
     <Description term="用户名">{findData.data.userName}</Description>
     <Description term="币种">{findData.data.coinCode}</Description>
     <Description term="币地址">{findData.data.coinAddress}</Description>
     <Description term="币地址标签">{findData.data.memo}</Description>
     <Description term="可用金额">{findData.data.available}</Description>
     <Description term="冻结金额">{findData.data.freeze}</Description>
     <Description term="状态">{findData.data.status}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
