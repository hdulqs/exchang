import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({coinNotify}) => ({
  coinNotify,
}))
export default class CoinNotifyInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('coinNotify/find',{id: match.params.id});
    }
  }

  render() {
    const { coinNotify:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="回调地址">{findData.data.notifyUrl}</Description>
     <Description term="回调用户">{findData.data.notifyUser}</Description>
     <Description term="回调方法">{findData.data.notifyMethod}</Description>
     <Description term="回调参数">{findData.data.notifyParameter}</Description>
     <Description term="回调类型POST/GET">{findData.data.notifyType}</Description>
     <Description term="回调次数">{findData.data.notifyCount}</Description>
     <Description term="回调状态">{findData.data.notifyState}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
