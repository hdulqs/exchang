import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({article}) => ({
  article,
}))
export default class ArticleInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('article/find',{id: match.params.id});
    }
  }

  render() {
    const { article:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="文章类别">{findData.data.typeName}</Description>
     <Description term="标题">{findData.data.title}</Description>
     <Description term="标题图片">{findData.data.filePath}</Description>
     <Description term="文章类型key">{findData.data.typeKey}</Description>
     <Description term="作者">{findData.data.author}</Description>
     <Description term="来源">{findData.data.articleFrom}</Description>
     <Description term="来源URL">{findData.data.fromUrl}</Description>
     <Description term="是否显示">{findData.data.display}</Description>
     <Description term="点击数">{findData.data.hits}</Description>
     <Description term="简介">{findData.data.shortContent}</Description>
     <Description term="seo关键字">{findData.data.seoKey}</Description>
     <Description term="内容">{findData.data.content}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
