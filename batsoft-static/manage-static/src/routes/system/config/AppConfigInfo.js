import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../../layouts/PageHeaderLayout';
import DescriptionList from '../../../components/DescriptionList';
import BaseAction from '../../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({appConfig}) => ({
  appConfig,
}))
export default class AppConfigInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('appConfig/find',{id: match.params.id});
    }
  }

  render() {
    const { appConfig:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="配置key">{findData.data.confKey}</Description>
     <Description term="配置名称">{findData.data.confName}</Description>
     <Description term="数据类型">{findData.data.dataType}</Description>
     <Description term="配置值">{findData.data.confValue}</Description>
     <Description term="选项值">{findData.data.selectValue}</Description>
     <Description term="类型key">{findData.data.typeKey}</Description>
     <Description term="类型名称">{findData.data.typeName}</Description>
     <Description term="描述">{findData.data.description}</Description>
     <Description term="排序">{findData.data.sort}</Description>
     <Description term="备注">{findData.data.remark}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
