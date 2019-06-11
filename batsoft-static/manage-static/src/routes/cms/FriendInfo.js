import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({friend}) => ({
  friend,
}))
export default class FriendInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('friend/find',{id: match.params.id});
    }
  }

  render() {
    const { friend:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="名称">{findData.data.name}</Description>
     <Description term="链接类型">{findData.data.type}</Description>
     <Description term="图片上传">{findData.data.imageUrl}</Description>
     <Description term="排序">{findData.data.sort}</Description>
     <Description term="链接地址">{findData.data.linkUrl}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
