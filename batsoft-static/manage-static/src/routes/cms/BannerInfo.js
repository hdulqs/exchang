import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({banner}) => ({
  banner,
}))
export default class BannerInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('banner/find',{id: match.params.id});
    }
  }

  render() {
    const { banner:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="Banner名称">{findData.data.name}</Description>
     <Description term="链接地址">{findData.data.url}</Description>
     <Description term="banner图片">{findData.data.image}</Description>
     <Description term="是否显示">{findData.data.display}</Description>
     <Description term="排序">{findData.data.sort}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
