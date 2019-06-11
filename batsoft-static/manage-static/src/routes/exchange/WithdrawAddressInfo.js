import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({withdrawAddress}) => ({
  withdrawAddress,
}))
export default class WithdrawAddressInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('withdrawAddress/find',{id: match.params.id});
    }
  }

  render() {
    const { withdrawAddress:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="提币地址">{findData.data.coinAddress}</Description>
     <Description term="memo地址">{findData.data.memo}</Description>
     <Description term="币种代码">{findData.data.coinCode}</Description>
     <Description term="用户id">{findData.data.userId}</Description>
     <Description term="用户名">{findData.data.userName}</Description>
     <Description term="用户手机号">{findData.data.userMobile}</Description>
     <Description term="用户邮箱">{findData.data.userEmail}</Description>
     <Description term="备注">{findData.data.remark}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
