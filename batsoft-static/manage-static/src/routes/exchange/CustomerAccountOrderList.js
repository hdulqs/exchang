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
const {Option} = Select;
const RadioGroup = Radio.Group;
const {RangePicker} = DatePicker;
const dateFormat = 'YYYY-MM-DD HH:mm:ss';
const statusMap = ['default', 'processing', 'success', 'error'];

@connect(({customerAccountOrder, loading}) => ({
customerAccountOrder,
  loading: loading.models.customerAccountOrder,
}))
@Form.create()
export default class CustomerAccountOrderList extends BaseList {
  state =  this.state;
  columns = [
    {
      dataIndex: 'accountId',
      title: '货币账户id',
      sortable: true,
    },
    {
      dataIndex: 'customerId',
      title: '客户id',
      sortable: true,
    },
    {
      dataIndex: 'accountNumber',
      title: '账户账号',
      sortable: true,
    },
    {
      dataIndex: 'coinCode',
      title: '币代码',
      sortable: true,
    },
    {
      dataIndex: 'direction',
      title: '方向',
      sortable: true,
      render(val) {
        switch (val){
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
        switch (val){
          case "0":
            return <Badge status={statusMap[0]} text="充值"/>;
            break;
          case "1":
            return <Badge status={statusMap[1]} text="提现"/>;
            break;
          case "2":
            return <Badge status={statusMap[2]} text="转入"/>;
            break;
          case "3":
            return <Badge status={statusMap[3]} text="转出"/>;
            break;
          case "4":
            return <Badge status={statusMap[4]} text="系统"/>;
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
        switch (val){
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
      title: '操作',
      dataIndex: 'id',
      render: val => (
        <Fragment>
          <Link className={this.hasPermissions("exchange:customerAccountOrder:see")?"":styles.hidden} to={{pathname: `/exchange_finance/customerAccountOrder/info/${val}`}}>查看</Link>
          <Divider type="vertical"/>
          <Link className={this.hasPermissions("exchange:customerAccountOrder:edit")?"":styles.hidden} to={{pathname: `/exchange_finance/customerAccountOrder/edit/${val}`}}>编辑</Link>
        </Fragment>
      ),
    },
  ];
  /**
   * 初始化
   */
  componentDidMount() {
    this.list("customerAccountOrder/fetch");
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
    this.list("customerAccountOrder/fetch",params);
  }

  /**
   * 更多操作 批量删除 批量通过 ... 事件
   * @param e
   */
  removeCallBack = () =>{
    this.setState({
      selectedRows: [],
    });
    this.list("customerAccountOrder/fetch");
  }
  handleMenuClick = (e) => {
    const {selectedRows} = this.state;
    const params={
      ids: selectedRows.map(row => row.id).join(','),
    }
    if (!selectedRows) return;
    switch (e.key) {
      case 'remove':
        this.remove("customerAccountOrder/remove",params,this.removeCallBack);
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
      this.list("customerAccountOrder/fetch",params);
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
              <FormItem label="客户id">
                  {getFieldDecorator('customerId_LK')(
                  <Input placeholder="请输入客户id"/>
                  )}
              </FormItem>
              </Col>

              <Col md={8} sm={24}>
              <FormItem label="账户账号">
                  {getFieldDecorator('accountNumber_LK')(
                  <Input placeholder="请输入账户账号"/>
                  )}
              </FormItem>
              </Col>
                         <Col md={8} sm={24}>

              <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">查询</Button>
              <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("customerAccountOrder/fetch")}>重置</Button>
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
              <FormItem label="客户id">
                  {getFieldDecorator('customerId_LK')(
                  <Input placeholder="请输入客户id"/>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="账户账号">
                  {getFieldDecorator('accountNumber_LK')(
                  <Input placeholder="请输入账户账号"/>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="币代码">
                  {getFieldDecorator('coinCode_LK')(
                  <Input placeholder="请输入币代码"/>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="operationType">
                  {getFieldDecorator('operationType_EQ')(
                  <Select placeholder="请选择" >
                      <Option value="0" >充值</Option>
                      <Option value="1" >提现</Option>
                      <Option value="2" >转入</Option>
                      <Option value="3" >转出</Option>
                      <Option value="4" >系统</Option>
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
              <FormItem label="opeationState">
                  {getFieldDecorator('opeationState_EQ')(
                  <Select placeholder="请选择" >
                      <Option value="0" >进行中</Option>
                      <Option value="1" >已完成</Option>
                      <Option value="2" >失败</Option>
                  </Select>
                  )}
              </FormItem>
              </Col>

          </Row>
          <div style={{overflow: 'hidden'}}>
          <span style={{float: 'right', marginBottom: 24}}>
          <Button type="primary" htmlType="submit">查询</Button>
          <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("customerAccountOrder/fetch")}>重置</Button>
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
    const {customerAccountOrder: {list}, loading} = this.props;
    const {selectedRows, modalVisible} = this.state;

    const menu = (
      <Menu onClick={this.handleMenuClick} selectedKeys={[]}>
          <Menu.Item key="remove" className={this.hasPermissions("exchange:customerAccountOrder:remove")?"":styles.hidden}>批量删除</Menu.Item>
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

                      <Link to="/exchange_finance/customerAccountOrder/add" className={this.hasPermissions("exchange:customerAccountOrder:add")?"":styles.hidden}>
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
