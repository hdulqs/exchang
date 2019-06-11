import React, {Fragment} from 'react';
import {connect} from 'dva';
import moment from 'moment';
import {
  Badge, Button, Card, Col, DatePicker, Divider, Dropdown, Form, Icon, Input, Menu, Modal, Radio, Row, Select
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
@connect(({appCache, loading}) => ({
  appCache,
  loading: loading.models.appCache,
}))
@Form.create()
export default class AppCacheList extends BaseList {
  state = this.state;
  columns = [
    {
      dataIndex: 'cacheKey',
      title: '缓存key',
      sortable: true,
    },
    /*{
      dataIndex: 'cacheValue',
      title: 'cache_value',
      sortable: true,
    },*/
    {
      dataIndex: 'cacheType',
      title: '缓存类型',
      sortable: true,
      render(val) {
        switch (val) {
          case 0:
            return <Badge status={statusMap[0]} text="永久缓存"/>;
            break;
          case 1:
            return <Badge status={statusMap[1]} text="临时缓存"/>;
            break;
        }
      },
    },
    {
      dataIndex: 'cacheValueType',
      title: '缓存值类型',
      sortable: true,
      render(val) {
        switch (val) {
          case 0:
            return <Badge status={statusMap[0]} text="字符串"/>;
            break;
          case 1:
            return <Badge status={statusMap[1]} text="对象"/>;
            break;
        }
      },
    },
    {
      dataIndex: 'cacheTime',
      title: '缓存时间(s)0为永久',
      sortable: true,
    },
    {
      title: '操作',
      dataIndex: 'id',
      render: val => (
        <Fragment>
          <Link className={this.hasPermissions("system:config:appCache:see")?"":styles.hidden} to={{pathname: `/system/cache/appCache/info/${val}`}}>查看</Link>
          <Divider type="vertical"/>
          <Link className={this.hasPermissions("system:config:appCache:edit")?"":styles.hidden} to={{pathname: `/system/cache/appCache/edit/${val}`}}>编辑</Link>
        </Fragment>
      ),
    },
  ];

  /**
   * 初始化
   */
  componentDidMount() {
    this.list("appCache/fetch");
  }

  // ====================================Handle====================================================

  /**
   * 列表标题筛选  分页查询 事件
   * @param pagination
   * @param filtersArg
   * @param sorter
   */
  handleStandardTableChange = (pagination, filtersArg, sorter) => {
    const params = this.handleStandardTableChangeParams(pagination, filtersArg, sorter);
    this.list("appCache/fetch", params);
  }

  updateAppConfigCache = () => {
    const {dispatch} = this.props;
    dispatch({
      type: 'appCache/refreshCache',
      payload: {
      },
      callback: data => {
        if (data.success) {
          message.success(data.msg);
          this.list("appCache/fetch");
        } else {
          message.error(data.msg);
        }
      },
    });
  }

  /**
   * 更多操作 批量删除 批量通过 ... 事件
   * @param e
   */
  removeCallBack = () => {
    this.setState({
      selectedRows: [],
    });
    this.list("appCache/fetch");
  }
  handleMenuClick = (e) => {
    const {selectedRows} = this.state;
    const params = {
      ids: selectedRows.map(row => row.id).join(','),
    }
    if (!selectedRows) return;
    switch (e.key) {
      case 'remove':
        this.remove("appCache/remove", params, this.removeCallBack);
        break;
      default:
        break;
    }
  }

  /**
   * 条件查询事件
   * @param e
   */
  handleSearch = (e) => {
    e.preventDefault();
    const {form} = this.props;
    form.validateFields((err, fieldsValue) => {


      if (err) return;
      const params = {
        ...fieldsValue,
      };
      this.setState({
        formValues: params,
      });
      this.list("appCache/fetch", params);
    });
  }


  /**
   * 渲染基础筛选条件
   * @returns {*}
   */
  renderSimpleForm() {
    const {getFieldDecorator} = this.props.form;
    return (
      <Form onSubmit={this.handleSearch} layout="inline">
        <Row gutter={{md: 8, lg: 24, xl: 48}}>


          <Col md={8} sm={24}>
            <FormItem label="缓存key">
              {getFieldDecorator('cacheKey_LK')(
                <Input placeholder="请输入缓存key"/>
              )}
            </FormItem>
          </Col>


          <Col md={8} sm={24}>
            <FormItem label="cacheType">
              {getFieldDecorator('cacheType_EQ')(
                <Select placeholder="请选择">
                  <Option value="0">永久缓存</Option>
                  <Option value="1">临时缓存</Option>
                </Select>
              )}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>

              <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">查询</Button>
              <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("appCache/fetch")}>重置</Button>
                  <a style={{marginLeft: 8}} onClick={this.toggleForm}>
                展开 <Icon type="down"/>
              </a>
            </span>
          </Col>
        </Row>
      </Form>
    );
  }

  /**
   * 渲染更多筛选条件
   * @returns {*}
   */
  renderAdvancedForm() {
    const {getFieldDecorator} = this.props.form;
    const formItemLayout = {
      labelCol: {
        xs: {span: 24},
        sm: {span: 7},
      },
      wrapperCol: {
        xs: {span: 24},
        sm: {span: 12},
        md: {span: 10},
      },
    };
    return (
      <Form onSubmit={this.handleSearch} layout="inline">
        <Row gutter={{md: 8, lg: 24, xl: 48}}>

          <Col md={8} sm={24}>
            <FormItem label="缓存key">
              {getFieldDecorator('cacheKey_LK')(
                <Input placeholder="请输入缓存key"/>
              )}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>
            <FormItem label="cacheType">
              {getFieldDecorator('cacheType_EQ')(
                <Select placeholder="请选择">
                  <Option value="0">永久缓存</Option>
                  <Option value="1">临时缓存</Option>
                </Select>
              )}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>
            <FormItem label="cacheValueType">
              {getFieldDecorator('cacheValueType_EQ')(
                <Select placeholder="请选择">
                  <Option value="0">字符串</Option>
                  <Option value="1">对象</Option>
                </Select>
              )}
            </FormItem>
          </Col>

        </Row>
        <div style={{overflow: 'hidden'}}>
          <span style={{float: 'right', marginBottom: 24}}>
          <Button type="primary" htmlType="submit">查询</Button>
          <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("appCache/fetch")}>重置</Button>
          <a style={{marginLeft: 8}} onClick={this.toggleForm}>
              收起 <Icon type="up"/>
          </a>
          </span>
        </div>
      </Form>
    );
  }

  /**
   * 主页面渲染
   * @returns {*}
   */
  render() {
    const {appCache: {list}, loading} = this.props;
    const {selectedRows, modalVisible} = this.state;

    const menu = (
      <Menu onClick={this.handleMenuClick} selectedKeys={[]}>
        <Menu.Item key="remove" className={this.hasPermissions("system:config:appCache:remove")?"":styles.hidden}>批量删除</Menu.Item>
      </Menu>
    );


    return (
      <PageHeaderLayout>
        <Card bordered={false}>
          <div className={styles.tableList}>
            <div className={styles.tableListForm}>
              {this.renderForm()}
            </div>
            <div className={styles.tableListOperator}>

              <Link className={this.hasPermissions("system:config:appCache:add")?"":styles.hidden} to="/system/cache/appCache/add">
                <Button icon="plus" type="primary">
                  新增
                </Button>
              </Link>
              <Button className={this.hasPermissions("system:config:appCache:updateAppConfigCache")?"":styles.hidden} icon="plus" type="primary" onClick={this.updateAppConfigCache}>
                刷新缓存
              </Button>
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
            <StandardTable
              selectedRows={selectedRows}
              loading={loading}
              data={list}
              columns={this.columns}
              onSelectRow={this.handleSelectRows}
              onChange={this.handleStandardTableChange}
            />
          </div>
        </Card>

      </PageHeaderLayout>
    );
  }
}
