import React from 'react';
import {connect} from 'dva';
import {Card} from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';

const {Description} = DescriptionList;
@connect(({single}) => ({
  single,
}))
export default class SingleInfo extends BaseAction {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('single/find', {id: match.params.id});
    }
  }

  render() {
    const {single: {findData}} = this.props;
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <DescriptionList size="large">
            <Description term="简标题">{findData.data.nav}</Description>
            <Description term="文章题目">{findData.data.title}</Description>
            <Description term="文章内容">{findData.data.singleForm}</Description>
            <Description term="文章标识">{findData.data.singleKey}</Description>
            <Description term="是否显示">{findData.data.status}</Description>
            <Description term="logo">{findData.data.logoPath}</Description>
          </DescriptionList>
        </Card>
      </PageHeaderLayout>
    );
  }
}
