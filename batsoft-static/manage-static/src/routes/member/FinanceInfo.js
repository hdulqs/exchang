import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({finance}) => ({
  finance,
}))
export default class FinanceInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('finance/find',{id: match.params.id});
    }
  }

  render() {
    const { finance:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="货币名称">{findData.data.currencyName}</Description>
     <Description term="币种类型">{findData.data.currencyCode}</Description>
     <Description term="可用金额">{findData.data.availableMoney}</Description>
     <Description term="冻结金额">{findData.data.freezeMoney}</Description>
     <Description term="币种logo">{findData.data.currencyLogo}</Description>
     <Description term="用户id">{findData.data.userId}</Description>
     <Description term="用户真实姓名">{findData.data.userRealName}</Description>
     <Description term="用户手机号">{findData.data.userMobile}</Description>
     <Description term="状态">{findData.data.status}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
