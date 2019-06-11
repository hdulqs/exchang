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

@connect(({entrustIng, loading}) => ({
entrustIng,
  loading: loading.models.entrustIng,
}))
@Form.create()
export default class EntrustIngList extends BaseList {
  state =  this.state;

  columns = [

    {
      dataIndex: 'userName',
      title: '用户名',
      sortable: true,
    },
    {
      dataIndex: 'entrustType',
      title: '类型',
      sortable: true,
    },
    {
      dataIndex: 'tradeCoinCode',
      title: '交易币',
      sortable: true,
    },
    {
      dataIndex: 'pricingCoinCode',
      title: '定价币',
      sortable: true,
    },
    {
      dataIndex: 'entrustPrice',
      title: '委托价格',
      sortable: true,
    },
    {
      dataIndex: 'entrustAmoutSql',
      title: '委托数量',
      sortable: true,
    },
    {
      dataIndex: 'entrustAmout',
      title: '未成交数量',
      sortable: true,
    },
    {
      dataIndex: 'totalMoney',
      title: '成交总额',
      sortable: true
    },
    {
      dataIndex: 'entrustTime',
      title: '委托时间',
      sortable: true,
    },
    {
      dataIndex: 'entrustState',
      title: '委托状态',
      sortable: true,
      render(val) {
        switch (val){
          case 0:
            return <Badge status={statusMap[0]} text="未成交"/>;
            break;
          case 1:
            return <Badge status={statusMap[1]} text="部分成交"/>;
            break;
          case 2:
            return <Badge status={statusMap[2]} text="全部成交"/>;
            break;
          case 3:
            return <Badge status={statusMap[3]} text="撤销"/>;
            break;
        }
      },
    },
    {
      dataIndex: 'category',
      title: '委托类别',
      sortable: true,
      render(val) {
        switch (val){
          case 0:
            return <Badge status={statusMap[0]} text="限价委托"/>;
            break;
          case 1:
            return <Badge status={statusMap[1]} text="计划委托"/>;
            break;
        }
      },
    },
    {
      title: '操作',
      dataIndex: 'id',
      render: val => (
        <Fragment>
          <Link className={this.hasPermissions("exchange:entrustIng:see")?"":styles.hidden} to={{pathname: `/exchange_entrust/entrustIng/info/${val}`}}>查看</Link>
          <Divider type="vertical"/>
          <Link className={this.hasPermissions("exchange:entrustIng:edit")?"":styles.hidden} to={{pathname: `/exchange_entrust/entrustIng/edit/${val}`}}>编辑</Link>
        </Fragment>
      ),
    },
  ];
  /**
   * 初始化
   */
  componentDidMount() {
    this.list("entrustIng/fetch");
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
    this.list("entrustIng/fetch",params);
  }

  /**
   * 更多操作 批量删除 批量通过 ... 事件
   * @param e
   */
  removeCallBack = () =>{
    this.setState({
      selectedRows: [],
    });
    this.list("entrustIng/fetch");
  }

  handleMenuClick = (e) => {
    const {selectedRows} = this.state;
    const params={
      ids: selectedRows.map(row => row.id).join(','),
    }
    const revertParams = {
      list: selectedRows.map(row => ({
        userid: row.customerId,
        coinpair: row.tradeCoinCode + '_' + row.pricingCoinCode,
        orderid: row.orderId
      }))
    }
    if (!selectedRows) return;
    switch (e.key) {
      case 'remove':
        this.remove("entrustIng/remove",params,this.removeCallBack);
        break;
      case 'revert':
        this.revert("entrustIng/revert",revertParams,this.removeCallBack);
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
      this.list("entrustIng/fetch",params);
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
              <FormItem label="账户id">
                  {getFieldDecorator('accountId_LK')(
                  <Input placeholder="请输入账户id"/>
                  )}
              </FormItem>
              </Col>

              <Col md={8} sm={24}>
              <FormItem label="客户id">
                  {getFieldDecorator('customerId_LK')(
                  <Input placeholder="请输入客户id"/>
                  )}
              </FormItem>
              </Col>
                         <Col md={8} sm={24}>

              <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">查询</Button>
              <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("entrustIng/fetch")}>重置</Button>
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
              <FormItem label="账户id">
                  {getFieldDecorator('accountId_LK')(
                  <Input placeholder="请输入账户id"/>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="客户id">
                  {getFieldDecorator('customerId_LK')(
                  <Input placeholder="请输入客户id"/>
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
              <FormItem label="委托价格">
                  {getFieldDecorator('entrustPrice_EQ')(
                  <Input placeholder="请输入委托价格"/>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="委托数量">
                  {getFieldDecorator('entrustAmout_EQ')(
                  <Input placeholder="请输入委托数量"/>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="entrustState">
                  {getFieldDecorator('entrustState_EQ')(
                  <Select placeholder="请选择" >
                      <Option value="0" >未成交</Option>
                      <Option value="1" >部分成交</Option>
                      <Option value="2" >全部成交</Option>
                      <Option value="3" >撤销</Option>
                  </Select>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="category">
                  {getFieldDecorator('category_EQ')(
                  <Select placeholder="请选择" >
                      <Option value="0" >限价委托</Option>
                      <Option value="1" >计划委托</Option>
                  </Select>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="orderFrom">
                  {getFieldDecorator('orderFrom_EQ')(
                  <Select placeholder="请选择" >
                      <Option value="0" >网页</Option>
                      <Option value="1" >移动端</Option>
                  </Select>
                  )}
              </FormItem>
              </Col>

          </Row>
          <div style={{overflow: 'hidden'}}>
          <span style={{float: 'right', marginBottom: 24}}>
          <Button type="primary" htmlType="submit">查询</Button>
          <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("entrustIng/fetch")}>重置</Button>
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
    const {entrustIng: {list}, loading} = this.props;
    const {selectedRows, modalVisible} = this.state;

    const menu = (
      <Menu onClick={this.handleMenuClick} selectedKeys={[]}>
          <Menu.Item key="remove" className={this.hasPermissions("exchange:entrustIng:remove")?"":styles.hidden}>批量删除</Menu.Item>
          <Menu.Item key="revert" className={this.hasPermissions("exchange:entrustIng:remove")?"":styles.hidden}>批量撤銷</Menu.Item>
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

                      <Link to="/exchange_entrust/entrustIng/add" className={this.hasPermissions("exchange:entrustIng:add")?"":styles.hidden}>
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
