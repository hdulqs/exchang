import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({articleChannel}) => ({
  articleChannel,
}))
export default class ArticleChannelInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('articleChannel/find',{id: match.params.id});
    }
  }

  render() {
    const { articleChannel:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="文章类别">{findData.data.name}</Description>
     <Description term="类型Key">{findData.data.typeKey}</Description>
     <Description term="是否首页展示">{findData.data.indexShow}</Description>
     <Description term="所属父类">{findData.data.parentId}</Description>
     <Description term="状态">{findData.data.status}</Description>
     <Description term="分类描述">{findData.data.description}</Description>
     <Description term="图标路径">{findData.data.icon}</Description>
     <Description term="排序">{findData.data.sort}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
