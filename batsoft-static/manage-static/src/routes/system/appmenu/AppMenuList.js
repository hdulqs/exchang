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

@connect(({appMenu, loading}) => ({
  appMenu,
  loading: loading.models.appMenu,
}))
@Form.create()
export default class AppMenuList extends BaseList {
  state = this.state;
  columns = [{
    title: '菜单名称',
    dataIndex: 'name',
    key: 'name',
  }, {
    title: '路径',
    dataIndex: 'path',
    key: 'path',
  }, {
    title: '是否菜单',
    dataIndex: 'menu',
    key: 'menu',
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
  }, {
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
  }, {
    title: '是否主导航',
    dataIndex: 'mainNav',
    key: 'mainNav',
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
  }, {
    title: '排序',
    dataIndex: 'sort',
    key: 'sort',
  }/*, {
  title: '备注',
  dataIndex: 'description',
  key: 'description',
} */,{
    title: '操作',
    dataIndex: 'id',
    render: val => (
      <Fragment>
        <Link className={this.hasPermissions("system:manage:appMenu:add")?"":styles.hidden} to={{pathname: `/security/appmenu/Appmenu/add/${val}`}}>下级</Link>
        <Divider type="vertical"/>
        <Link className={this.hasPermissions("system:manage:appMenu:edit")?"":styles.hidden} to={{pathname: `/security/appmenu/Appmenu/edit/${val}`}}>编辑</Link>
      </Fragment>
    ),
  }];
  /**
   * 初始化
   */
  componentDidMount() {
    this.list("appMenu/menuTreeData");
  }

  /**
   * 删除回调事件
   * @param e
   */
  removeCallBack = () => {
    this.setState({
      selectedRows: [],
    });
    this.list("appMenu/menuTreeData");
  }
  handleMenuClick = (e) => {
    const {selectedRows} = this.state;
    const params = {
      ids: selectedRows.map(row => row.key).join(','),
    }
    if (!selectedRows) return;
    switch (e.key) {
      case 'remove':
        this.remove("appMenu/remove", params, this.removeCallBack);
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
    const {appMenu: {menuTreeData}, loading} = this.props;
    const {selectedRows} = this.state;
    if(menuTreeData.data===undefined) menuTreeData.data=[];
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
        <Menu.Item key="remove" className={this.hasPermissions("system:manage:appMenu:remove")?"":styles.hidden}>批量删除</Menu.Item>
      </Menu>
    );

    return (
      <PageHeaderLayout>
        <Card bordered={false}>
          <div className={styles.tableList}>
            <div className={styles.tableListOperator}>

              <Link className={this.hasPermissions("system:manage:appMenu:add")?"":styles.hidden} to="/security/appmenu/appMenu/add/0">
                <Button icon="plus" type="primary" >
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
            <Table columns={this.columns} rowSelection={rowSelection} dataSource={menuTreeData.data} />
          </div>
        </Card>

      </PageHeaderLayout>
    );
  }
}
