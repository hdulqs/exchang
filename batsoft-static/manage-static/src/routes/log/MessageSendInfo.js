import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({messageSend}) => ({
  messageSend,
}))
export default class MessageSendInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('messageSend/find',{id: match.params.id});
    }
  }

  render() {
    const { messageSend:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="短信方">{findData.data.smsProvider}</Description>
     <Description term="短信类型">{findData.data.smsType}</Description>
     <Description term="短信内容">{findData.data.smsContent}</Description>
     <Description term="发送时间">{findData.data.sendTime}</Description>
     <Description term="电话号码">{findData.data.cellphone}</Description>
     <Description term="发送状态">{findData.data.status}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
