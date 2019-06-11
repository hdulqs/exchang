import React from 'react';
import { connect } from 'dva';
import { Card ,Table} from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList';
import BaseAction from '../../components/BatSoft';
const { Description } = DescriptionList;
@connect(({customerAccount,loading}) => ({
  customerAccount,
  loading: loading.models.customerAccount,
}))

export default class CustomerAccountInfo extends BaseAction  {
  componentDidMount() {
    const {match, dispatch} = this.props;
    if (match.params.id !== undefined) {
      this.find('customerAccount/checkAccount',{id: match.params.id});
    }
  }

  render() {
    const { customerAccount:{ accounts:list },loading } = this.props;
    let goodsData = [];
   
    const  data=list.data===undefined?[]:list.data;

    if (data.length) {
      let mysql_available = 0;
      let mysql_freeze = 0;

      let redis_available = 0;
      let redis_freeze = 0;

      let record_available = 0;
      let record_freeze = 0;
      data.forEach(item => {
        mysql_available += Number(item.mysql_available);
        mysql_freeze += Number(item.mysql_freeze);

        redis_available += Number(item.redis_available);
        redis_freeze += Number(item.redis_freeze);

        record_available += Number(item.record_available);
        record_freeze += Number(item.record_freeze);
      });
      goodsData = data/*.concat({
        id: '总计',
        mysql_available,
        mysql_freeze,
        redis_available,
        redis_freeze,
        record_available,
        record_freeze,
      })*/;
    }



    const goodsColumns = [
      {
        title: '币种',
        dataIndex: 'coinCode',
        key: 'coinCode',
        render: (text, row, index) => {
          if (index < data.length) {
            return <a href="">{text}</a>;
          }
          return {
            children: <span style={{ fontWeight: 600 }}>总计</span>,
            props: {
              colSpan: 1,
            },
          };
        },
      },
      {
        title: '数据库可用',
        dataIndex: 'mysql_available',
        key: 'mysql_available'/*,
        render: (text, row, index) => {
          if (index < data.length) {
            return text;
          }
          return <span style={{ fontWeight: 600 }}>{text}</span>;
        },*/
      },
      {
        title: '数据库冻结',
        dataIndex: 'mysql_freeze',
        key: 'mysql_freeze'/*,
        render: (text, row, index) => {
          if (index < data.length) {
            return text;
          }
          return <span style={{ fontWeight: 600 }}>{text}</span>;
        },*/
      },
      {
        title: 'Redis可用',
        dataIndex: 'redis_available',
        key: 'redis_available',
        align: 'right'/*,
        render: (text, row, index) => {
          if (index < data.length) {
            return text;
          }
          return <span style={{ fontWeight: 600 }}>{text}</span>;
        },*/
      },
      {
        title: 'Redis冻结',
        dataIndex: 'redis_freeze',
        key: 'redis_freeze',
        align: 'right'
      },{
        title: '实际流水',
        dataIndex: 'record_available',
        key: 'record_available',
        align: 'right'
      },
      {
        title: '实际冻结',
        dataIndex: 'record_freeze',
        key: 'record_freeze',
        align: 'right'
      },
      /*{
        title: '金额',
        dataIndex: 'amount',
        key: 'amount',
        align: 'right',
        render: (text, row, index) => {
          if (index < basicGoods.length) {
            return text;
          }
          return <span style={{ fontWeight: 600 }}>{text}</span>;
        },
      },*/
    ];
    return (
      <PageHeaderLayout
      >
        <Card bordered={false}>
          <div >账户核算</div>
          <Table
            style={{ marginBottom: 24 }}
            pagination={false}
            loading={loading}
            dataSource={goodsData}
            columns={goodsColumns}
            rowKey="coinCode"
          />
        </Card>
      </PageHeaderLayout>
    );
  }
}
