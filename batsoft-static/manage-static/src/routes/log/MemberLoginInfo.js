import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({memberLogin}) => ({
  memberLogin,
}))
export default class MemberLoginInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('memberLogin/find',{id: match.params.id});
    }
  }

  render() {
    const { memberLogin:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="请求地址22">{findData.data.url}</Description>
     <Description term="ip地址">{findData.data.ip}</Description>
     <Description term="所在地">{findData.data.address}</Description>
     <Description term="请求方法">{findData.data.method}</Description>
     <Description term="请求类型（application/json等等）">{findData.data.mediaType}</Description>
     <Description term="请求方法（get/post）">{findData.data.requestMethod}</Description>
     <Description term="回调参数">{findData.data.callbackParams}</Description>
     <Description term="回调次数">{findData.data.callbackNum}</Description>
     <Description term="来源(app/pc)">{findData.data.requestFrom}</Description>
     <Description term="执行时间MS">{findData.data.executeTime}</Description>
     <Description term="状态">{findData.data.status}</Description>
     <Description term="请求参数">{findData.data.params}</Description>
     <Description term="备注">{findData.data.remark}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
