import React from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../../layouts/PageHeaderLayout';
import DescriptionList from '../../../components/DescriptionList';
import BaseAction from '../../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({appRole}) => ({
  appRole,
}))
export default class AppRoleInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('appRole/find',{id: match.params.id});
    }
  }

  render() {
    const { appRole:{ findData } } = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large"  >
     <Description term="角色名称">{findData.data.name}</Description>
     <Description term="角色唯一标识">{findData.data.roleKey}</Description>
     <Description term="是否禁用">{findData.data.available==0?'否':'是'}</Description>
     <Description term="创建人">{findData.data.createUser}</Description>
     <Description term="修改人">{findData.data.updateUser}</Description>
     <Description term="备注">{findData.data.remark}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
