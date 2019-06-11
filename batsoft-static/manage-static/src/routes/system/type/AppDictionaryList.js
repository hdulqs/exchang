import React, {Fragment} from 'react';
import {connect} from 'dva';
import moment from 'moment';
import {
  Badge,Table, Button, Card, Col, DatePicker, Divider, Dropdown, Form, Icon, Input, Menu, Modal, Radio, Row, Select
} from 'antd';
import StandardTable from '../../../components/StandardTable';
import PageHeaderLayout from '../../../layouts/PageHeaderLayout';
import {Link} from 'dva/router';
import styles from '../../../assets/style/TableList.less';
import BaseList from '../../../components/BatSoft/List';
import {message} from "antd/lib/index";

const FormItem = Form.Item;
const {Option} = Select;
const RadioGroup = Radio.Group;
const {RangePicker} = DatePicker;
const dateFormat = 'YYYY-MM-DD HH:mm:ss';
const statusMap = ['default', 'processing', 'success', 'error'];
@connect(({appDictionary, loading}) => ({
  appDictionary,
  loading: loading.models.appDictionary,
}))
@Form.create()
export default class AppDictionaryList extends BaseList {
  state = this.state;
  columns = [

    {
      dataIndex: 'name',
      title: '名称',
      sortable: true,
    },
    {
      dataIndex: 'dicKey',
      title: '字典Key',
      sortable: true,
    },
    {
      dataIndex: 'icon',
      title: 'icon',
      sortable: true,
    },
    {
      dataIndex: 'sort',
      title: '排序',
      sortable: true,
    },


    {
      dataIndex: 'description',
      title: '描述',
      sortable: true,
    },
    {
      title: '是否显示',
      dataIndex: 'display',
      key: 'display',
      render(val) {
        switch (val) {
          case 0:
            return <Badge status={statusMap[0]} text="否"/>;
            break;
          case 1:
            return <Badge status={statusMap[1]} text="是"/>;
            break;
        }
      },
    },
    {
      title: '操作',
      dataIndex: 'id',
      render: val => (
        <Fragment>
          <Link className={this.hasPermissions("cms:appDictionary:add")?"":styles.hidden} to={{pathname: `/system/type/appDictionary/add/${val}`}}>下级</Link>
          <Divider type="vertical"/>
          <Link className={this.hasPermissions("cms:appDictionary:edit")?"":styles.hidden} to={{pathname: `/system/type/appDictionary/edit/${val}`}}>编辑</Link>
        </Fragment>
      ),

    },
  ];

  /**
   * 初始化
   */
  componentDidMount() {
    this.list("appDictionary/dicTreeData");
  }

  /**
   * 删除回调事件
   * @param e
   */
  removeCallBack = () => {
    this.setState({
      selectedRows: [],
    });
    this.list("appDictionary/dicTreeData");
  }
  handleMenuClick = (e) => {
    const {selectedRows} = this.state;
    const params = {
      ids: selectedRows.map(row => row.key).join(','),
    }
    if (!selectedRows) return;
    switch (e.key) {
      case 'remove':
        this.remove("appDictionary/remove", params, this.removeCallBack);
        break;
      default:
        break;
    }
  }

  /**
   * 主页面渲染12
   * @returns {*}
   */
  render() {
    const {appDictionary: {dicTreeData}, loading} = this.props;
    const {selectedRows} = this.state;
    if (dicTreeData.data === undefined) dicTreeData.data = [];
    // rowSelection objects indicates the need for row selection
    const rowSelection = {
      onChange: (selectedRowKeys, selectedRows) => {
        console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
      },
      onSelect: (record, selected, selectedRows) => {
        this.setState({
          selectedRows: selectedRows,
        });
        console.log(record, selected, selectedRows);
      },
      onSelectAll: (selected, selectedRows, changeRows) => {
        this.setState({
          selectedRows: selectedRows,
        });
        console.log(selected, selectedRows, changeRows);
      },
    };


    const menu = (
      <Menu onClick={this.handleMenuClick} selectedKeys={[]}>
        <Menu.Item key="remove" className={this.hasPermissions("cms:appDictionary:remove")?"":styles.hidden}>批量删除</Menu.Item>
      </Menu>
    );

    return (
      <PageHeaderLayout>
        <Card bordered={false}>
          <div className={styles.tableList}>
            <div className={styles.tableListOperator}>

              <Link className={this.hasPermissions("cms:appDictionary:add")?"":styles.hidden} to="/system/type/appDictionary/add/0">
                <Button icon="plus" type="primary">
                  新增
                </Button>
              </Link>
              {
                selectedRows.length > 0 && (
                  <span>
                      <Dropdown overlay={menu}>
                        <Button>
                        更多操作 <Icon type="down"/>
                        </Button>
                      </Dropdown>
                    </span>
                )
              }
            </div>
            <Table columns={this.columns} rowSelection={rowSelection} dataSource={dicTreeData.data}/>
          </div>
        </Card>

      </PageHeaderLayout>
    );
  }
}
