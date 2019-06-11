import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({coinApiLog}) => ({
  coinApiLog,
}))
export default class CoinApiLogInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('coinApiLog/find',{id: match.params.id});
    }
  }

  render() {
    const { coinApiLog:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="请求ip">{findData.data.requestIp}</Description>
     <Description term="请求用户">{findData.data.requestUser}</Description>
     <Description term="请求方法">{findData.data.requestMethod}</Description>
     <Description term="请求参数">{findData.data.requestParameter}</Description>
     <Description term="返回状态">{findData.data.requestState}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
