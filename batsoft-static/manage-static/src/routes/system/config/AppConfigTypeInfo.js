import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../../layouts/PageHeaderLayout';
import DescriptionList from '../../../components/DescriptionList';
import BaseAction from '../../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({appConfigType}) => ({
  appConfigType,
}))
export default class AppConfigTypeInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('appConfigType/find',{id: match.params.id});
    }
  }

  render() {
    const { appConfigType:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="类型名称">{findData.data.typeName}</Description>
     <Description term="类型key">{findData.data.typeKey}</Description>
     <Description term="排序">{findData.data.sort}</Description>
     <Description term="备注">{findData.data.remark}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
