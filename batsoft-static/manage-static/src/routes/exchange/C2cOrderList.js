import React, {Fragment} from 'react';
import {connect} from 'dva';
import moment from 'moment';
import {
  Badge, Button, Card, Col, DatePicker, Divider, Dropdown, Form, Icon, Input, Menu, Modal, Radio, Row, Select
} from 'antd';
import StandardTable from '../../components/StandardTable';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import {Link} from 'dva/router';
import styles from '../../assets/style/TableList.less';
import BaseList from '../../components/BatSoft/List';
import {message} from "antd/lib/index";

const confirm = Modal.confirm;
const FormItem = Form.Item;
const {Option} = Select;
const RadioGroup = Radio.Group;
const {RangePicker} = DatePicker;
const dateFormat = 'YYYY-MM-DD HH:mm:ss';
const statusMap = ['default', 'processing', 'success', 'error'];
@connect(({c2cOrder, loading}) => ({
  c2cOrder,
  loading: loading.models.c2cOrder,
}))
@Form.create()
export default class C2cOrderList extends BaseList {
  state = this.state;
  columns = [
    {
      dataIndex: 'userName',
      title: '用户名',
      sortable: true,
    },
    {
      dataIndex: 'coinCode',
      title: '币种',
      sortable: true,
    },
    {
      dataIndex: 'direction',
      title: '方向',
      sortable: true,
      render(val) {
        switch (val) {
          case 0:
            return <Badge status={statusMap[0]} text="支出"/>;
            break;
          case 1:
            return <Badge status={statusMap[1]} text="收入"/>;
            break;
        }
      },
    },
    {
      dataIndex: 'operationType',
      title: '操作类型',
      sortable: true,
      render(val) {
        switch (val) {
          case 1:
            return <Badge status={statusMap[1]} text="买入"/>;
            break;
          case 2:
            return <Badge status={statusMap[2]} text="卖出"/>;
            break;
        }
      },
    },
    {
      dataIndex: 'opeationMoney',
      title: '订单金额',
      sortable: true,
    },
    {
      dataIndex: 'tradeTime',
      title: '交易时间',
      sortable: true,
    },
    {
      dataIndex: 'opeationState',
      title: '订单状态',
      sortable: true,
      render(val) {
        switch (val) {
          case 0:
            return <Badge status={statusMap[0]} text="进行中"/>;
            break;
          case 1:
            return <Badge status={statusMap[1]} text="已完成"/>;
            break;
          case 2:
            return <Badge status={statusMap[2]} text="失败"/>;
            break;
        }
      },
    },
    {
      dataIndex: 'traBankcard',
      title: 'tra_bankcard',
      sortable: true,
    },
    {
      title: '操作',
      dataIndex: 'id',
      render: val => (
        <Fragment>
          <Link className={this.hasPermissions("exchange:c2cOrder:see")?"":styles.hidden} to={{pathname: `/exchange_c2c/c2cOrder/info/${val}`}}>查看</Link>
          {/*<Divider type="vertical"/>
           <Link to={{pathname: `/exchange/c2cOrder/edit/${val}`}}>编辑</Link>*/}
        </Fragment>
      ),
    },
  ];

  /**
   * 初始化
   */
  componentDidMount() {
    this.list("c2cOrder/fetch");
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
    this.list("c2cOrder/fetch", params);
  }

  /**
   * 删除回调事件
   * @param e
   */
  removeCallBack = () => {
    this.setState({
      selectedRows: [],
    });
    this.list("c2cOrder/fetch");
  }
  handleMenuClick = (e) => {
    const {selectedRows} = this.state;
    const params = {
      ids: selectedRows.map(row => row.id).join(','),
    }
    if (!selectedRows) return;
    switch (e.key) {
      case 'remove':
        this.remove("c2cOrder/remove", params, this.removeCallBack);
        break;
      case 'pass':
        this.pass("c2cOrder/c2cOrderPass", params);
        break;
      case 'refuse':
        this.refuse("c2cOrder/c2cRefuseOrder", params);
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

      if (fieldsValue.date) {
        fieldsValue.tradeTime_GT = fieldsValue.date[0].format(dateFormat);
        fieldsValue.tradeTime_LT = fieldsValue.date[1].format(dateFormat);
        fieldsValue.date = null;
      }


      if (err) return;
      const params = {
        ...fieldsValue,
      };
      this.setState({
        formValues: params,
      });
      this.list("c2cOrder/fetch", params);
    });
  }

  /**
   * 审核通过
   * @param type
   * @param params
   */
  pass = (type, params) => {
    const ids = params.ids.split(",");
   /* if (ids.length > 1) {
      message.error("只能操作一条数据");
      return false;
    }*/
    confirm({
      title: '温馨提醒！',
      content: '是否确认到账，要通过通过该订单？',
      onOk: () => {
        const {dispatch} = this.props;
        dispatch({
          type: type,
          payload: {
            ...params,
          },
          callback: data => {
            if (data.success) {
              message.success(data.msg);
              this.list("c2cOrder/fetch");
            } else {
              message.error(data.msg);
            }
          },
        });

      },
      onCancel() {
        console.log('Cancel');
      },
    });
  }
  /**
   * 审核拒绝
   * @param type
   * @param params
   */
  refuse = (type, params) => {

    const ids = params.ids.split(",");
    /*if (ids.length > 1) {
      message.error("只能操作一条数据");
      return false;
    }*/
    confirm({
      title: '温馨提醒！',
      content: '是否要拒绝这笔操作？一旦操作将不能恢复',
      onOk: () => {
        const {dispatch} = this.props;
        dispatch({
          type: type,
          payload: {
            ...params,
          },
          callback: data => {
            if (data.success) {
              message.success(data.msg);
              this.list("c2cOrder/fetch");
            } else {
              message.error(data.msg);
            }
          },
        });

      },
      onCancel() {
        console.log('Cancel');
      },
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
            <FormItem label="用户名">
              {getFieldDecorator('userName_LK')(
                <Input placeholder="请输入用户名"/>
              )}
            </FormItem>
          </Col>

          <Col md={8} sm={24}>
            <FormItem label="币种">
              {getFieldDecorator('coinCode_LK')(
                <Input placeholder="请输入币种"/>
              )}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>

              <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">查询</Button>
              <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("c2cOrder/fetch")}>重置</Button>
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
            <FormItem label="用户名">
              {getFieldDecorator('userName_LK')(
                <Input placeholder="请输入用户名"/>
              )}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>
            <FormItem label="币种">
              {getFieldDecorator('coinCode_LK')(
                <Input placeholder="请输入币种"/>
              )}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>
            <FormItem label="operationType">
              {getFieldDecorator('operationType_EQ')(
                <Select placeholder="请选择">
                  <Option value="1">买入</Option>
                  <Option value="2">卖出</Option>
                </Select>
              )}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>
            <FormItem label="订单金额">
              {getFieldDecorator('opeationMoney_EQ')(
                <Input placeholder="请输入订单金额"/>
              )}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>
            <FormItem
              {...formItemLayout}
              label="起止日期"
            >
              {getFieldDecorator('date', {})(
                <RangePicker style={{width: '100%'}} placeholder={['开始日期', '结束日期']}/>
              )}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>
            <FormItem label="opeationState">
              {getFieldDecorator('opeationState_EQ')(
                <Select placeholder="请选择">
                  <Option value="0">进行中</Option>
                  <Option value="1">已完成</Option>
                  <Option value="2">失败</Option>
                </Select>
              )}
            </FormItem>
          </Col>

        </Row>
        <div style={{overflow: 'hidden'}}>
          <span style={{float: 'right', marginBottom: 24}}>
          <Button type="primary" htmlType="submit">查询</Button>
          <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("c2cOrder/fetch")}>重置</Button>
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
    const {c2cOrder: {list}, loading} = this.props;
    const {selectedRows, modalVisible} = this.state;

    const menu = (
      <Menu onClick={this.handleMenuClick} selectedKeys={[]}>
        <Menu.Item key="pass" className={this.hasPermissions("exchange:c2cOrder:passStatus")?"":styles.hidden}>审核通过</Menu.Item>
        <Menu.Item key="refuse" className={this.hasPermissions("exchange:c2cOrder:refuseStatus")?"":styles.hidden}>拒绝</Menu.Item>
        <Menu.Item key="remove" className={this.hasPermissions("exchange:c2cOrder:remove")?"":styles.hidden}>批量删除</Menu.Item>
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

              {/*<Link to="/exchange/c2cOrder/add">
                      <Button icon="plus" type="primary">
                          新增
                      </Button>
                      </Link>*/}
              {/*<Link to="/exchange/c2cOrder/add">
                      <Button icon="plus" type="primary">
                        审核通过
                      </Button>
                    </Link>
                    <Link to="/exchange/c2cOrder/add">
                      <Button icon="plus" type="primary">
                        审核拒绝
                      </Button>
                    </Link>*/}
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
