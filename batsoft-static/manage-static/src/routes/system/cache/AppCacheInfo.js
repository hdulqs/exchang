import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../../layouts/PageHeaderLayout';
import DescriptionList from '../../../components/DescriptionList';
import BaseAction from '../../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({appCache}) => ({
  appCache,
}))
export default class AppCacheInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('appCache/find',{id: match.params.id});
    }
  }

  render() {
    const { appCache:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="缓存key">{findData.data.cacheKey}</Description>
     <Description term="cache_value">{findData.data.cacheValue}</Description>
     <Description term="缓存类型">{findData.data.cacheType}</Description>
     <Description term="缓存值类型">{findData.data.cacheValueType}</Description>
     <Description term="缓存时间(s)0为永久">{findData.data.cacheTime}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
