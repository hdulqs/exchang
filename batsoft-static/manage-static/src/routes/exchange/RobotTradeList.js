import React, {Fragment} from 'react';
import {connect} from 'dva';
import moment from 'moment';
import {
  Badge, Button, Card, Col, DatePicker, Divider, Dropdown, Form, Icon, Input, Menu,  Modal, Radio, Row,Select
} from 'antd';

import StandardTable from '../../components/StandardTable';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import {Link} from 'dva/router';
import styles from '../../assets/style/TableList.less';
import BaseList from '../../components/BatSoft/List';
const FormItem = Form.Item;
const confirm = Modal.confirm;

const {Option} = Select;
const RadioGroup = Radio.Group;
const {RangePicker} = DatePicker;
const dateFormat = 'YYYY-MM-DD HH:mm:ss';
const statusMap = ['default', 'processing', 'success', 'error'];

@connect(({robotTrade, loading}) => ({
robotTrade,
  loading: loading.models.robotTrade,
}))
@Form.create()
export default class RobotTradeList extends BaseList {
  state =  this.state;
  columns = [
    {
      dataIndex: 'coinPair',
      title: '交易对',
      sortable: true,
    },
    {
      dataIndex: 'userName',
      title: '下单用户',
      sortable: true,
    },
    {
      dataIndex: 'entrustNumMax',
      title: '最大下单数量',
      sortable: true,
    },
    {
      dataIndex: 'entrustNumMin',
      title: '最小下单数量',
      sortable: true,
    },
    {
      dataIndex: 'entrustTimeMax',
      title: '下单时间最大',
      sortable: true,
    },
    {
      dataIndex: 'entrustTimeMin',
      title: '下单时间最小',
      sortable: true,
    },
    {
      dataIndex: 'entrustPriceMax',
      title: '最大下单价格',
      sortable: true,
    },
    {
      dataIndex: 'entrustPriceMin',
      title: '最小下单价格',
      sortable: true,
    },
    {
      dataIndex: 'basePrice',
      title: '步长',
      sortable: true,
    },
    {
      dataIndex: 'fromThird',
      title: '是否三方数据',
      sortable: true,
      render(val) {
        switch (val){
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
      dataIndex: 'thirdApi',
      title: 'api地址',
      sortable: true,
    },
    {
      dataIndex: 'status',
      title: '运行状态',
      sortable: true,
      render(val) {
        switch (val){
          case 0:
            return <Badge status={statusMap[0]} text="停止"/>;
            break;
          case 1:
            return <Badge status={statusMap[1]} text="启动"/>;
            break;
        }
      },
    },
    {
      title: '操作',
      dataIndex: 'id',
      render: val => (
        <Fragment>
          <Link className={this.hasPermissions("exchange:robotTrade:see")?"":styles.hidden} to={{pathname: `/exchange_robot/robotTrade/info/${val}`}}>查看</Link>
          <Divider type="vertical"/>
          <Link className={this.hasPermissions("exchange:robotTrade:edit")?"":styles.hidden} to={{pathname: `/exchange_robot/robotTrade/edit/${val}`}}>编辑</Link>
        </Fragment>
      ),
    },
  ];
  /**
   * 初始化
   */
  componentDidMount() {
    this.list("robotTrade/fetch");
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
    this.list("robotTrade/fetch",params);
  }

  /**
   * 删除回调事件
   * @param e
   */
  removeCallBack = () =>{
    this.setState({
      selectedRows: [],
    });
    this.list("robotTrade/fetch");
  }

  openCallBack = () =>{
    this.setState({
      selectedRows: [],
    });
    this.list("robotTrade/fetch");
  }

  stopCallBack = () =>{
    this.setState({
      selectedRows: [],
    });
    this.list("robotTrade/fetch");
  }

  /**
   * 开启机器人
   * @param e
   */
  open = (type,params,callback) => {
    confirm({
      title: '温馨提醒！',
      content: '是否确认开启？',
      onOk: () => {
        const {dispatch} = this.props;
        dispatch({
          type: type,
          payload: {
            ...params,
          },
          callback: () => {
            callback();
          },
        });
      },
      onCancel() {
        console.log('Cancel');
      },
    });
  }

  /**
   * 关闭机器人
   * @param e
   */
  stop = (type,params,callback) => {
    confirm({
      title: '温馨提醒！',
      content: '是否确认关闭？',
      onOk: () => {
        const {dispatch} = this.props;
        dispatch({
          type: type,
          payload: {
            ...params,
          },
          callback: () => {
            callback();
          },
        });
      },
      onCancel() {
        console.log('Cancel');
      },
    });
  }
  handleMenuClick = (e) => {
    const {selectedRows} = this.state;
    const params={
      ids: selectedRows.map(row => row.id).join(','),
    }
    if (!selectedRows) return;
    switch (e.key) {
      case 'remove':
        this.remove("robotTrade/remove",params,this.removeCallBack);
        break;
      case 'open':
        this.open("robotTrade/open",params,this.openCallBack);
        break;
      case 'stop':
        this.stop("robotTrade/stop",params,this.stopCallBack);
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
      this.list("robotTrade/fetch",params);
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
              <FormItem label="交易对">
                  {getFieldDecorator('coinPair_LK')(
                  <Input placeholder="请输入交易对"/>
                  )}
              </FormItem>
              </Col>










              <Col md={8} sm={24}>
              <FormItem label="fromThird">
                  {getFieldDecorator('fromThird_EQ')(
                  <Select placeholder="请选择" >
                      <Option value="0" >否</Option>
                      <Option value="1" >是</Option>
                  </Select>
                  )}
              </FormItem>
              </Col>
                         <Col md={8} sm={24}>

              <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">查询</Button>
              <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("robotTrade/fetch")}>重置</Button>
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
              <FormItem label="交易对">
                  {getFieldDecorator('coinPair_LK')(
                  <Input placeholder="请输入交易对"/>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="fromThird">
                  {getFieldDecorator('fromThird_EQ')(
                  <Select placeholder="请选择" >
                      <Option value="0" >否</Option>
                      <Option value="1" >是</Option>
                  </Select>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="status">
                  {getFieldDecorator('status_EQ')(
                  <Select placeholder="请选择" >
                      <Option value="0" >停止</Option>
                      <Option value="1" >启动</Option>
                  </Select>
                  )}
              </FormItem>
              </Col>

          </Row>
          <div style={{overflow: 'hidden'}}>
          <span style={{float: 'right', marginBottom: 24}}>
          <Button type="primary" htmlType="submit">查询</Button>
          <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("robotTrade/fetch")}>重置</Button>
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
    const {robotTrade: {list}, loading} = this.props;
    const {selectedRows, modalVisible} = this.state;

    const menu = (
      <Menu onClick={this.handleMenuClick} selectedKeys={[]}>
          <Menu.Item key="remove" className={this.hasPermissions("exchange:robotTrade:remove")?"":styles.hidden}>批量删除</Menu.Item>
          <Menu.Item key="open" className={this.hasPermissions("exchange:robotTrade:open")?"":styles.hidden}>开启</Menu.Item>
          <Menu.Item key="stop" className={this.hasPermissions("exchange:robotTrade:stop")?"":styles.hidden}>停止</Menu.Item>
      </Menu>
    );


    return (
      <PageHeaderLayout   >
          <Card bordered={false}>
              <div className={styles.tableList}>
                  <div className={styles.tableListForm}>
                      {this.renderForm()}
                  </div>
                  <div className={styles.tableListOperator}>

                      <Link to="/exchange_robot/robotTrade/add" className={this.hasPermissions("exchange:robotTrade:add")?"":styles.hidden}>
                      <Button icon="plus" type="primary"  >
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
