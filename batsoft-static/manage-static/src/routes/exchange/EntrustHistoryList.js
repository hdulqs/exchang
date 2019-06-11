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
const dateToday= 'YYYY-MM-DD';
const FormItem = Form.Item;
const {Option} = Select;
const RadioGroup = Radio.Group;
const {RangePicker} = DatePicker;
const dateFormat = 'YYYY-MM-DD HH:mm:ss';
const statusMap = ['default', 'processing', 'success', 'error'];

@connect(({entrustHistory, loading}) => ({
entrustHistory,
  loading: loading.models.entrustHistory,
}))
@Form.create()
export default class EntrustHistoryList extends BaseList {
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
      sortable: true,
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
          <Link className={this.hasPermissions("exchange:entrustHistory:see")?"":styles.hidden} to={{pathname: `/exchange_entrust/entrustHistory/info/${val}`}}>查看</Link>
          <Divider type="vertical"/>
          <Link className={this.hasPermissions("exchange:entrustHistory:edit")?"":styles.hidden} to={{pathname: `/exchange_entrust/entrustHistory/edit/${val}`}}>编辑</Link>
        </Fragment>
      ),
    },
  ];
  /**
   * 初始化
   */
  componentDidMount() {
    this.list("entrustHistory/fetch");
  }
  handleData(time){
		if(!time){
			return false
		}else{
    // 大于当前日期不能选 
      return time > moment()
		// 小于当前日期不能选 time < moment().subtract(1, "days")
		// 只能选前7后7 time < moment().subtract(7, "days") || time > moment().add(7, 'd')
			// return time < moment().subtract(1, "days") || time > moment().add(1, 'd')
		}
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
    this.list("entrustHistory/fetch",params);
  }

  /**
   * 更多操作 批量删除 批量通过 ... 事件
   * @param e
   */
  removeCallBack = () =>{
    this.setState({
      selectedRows: [],
    });
    this.list("entrustHistory/fetch");
  }
  handleMenuClick = (e) => {
    const {selectedRows} = this.state;
    const {form} = this.props;
    form.validateFields((err, fieldsValue) => {
      if (err) return;
    const params={
      ids: selectedRows.map(row => row.id).join(','),
      date: fieldsValue['date'].format('YYYY-MM-DD')
    }
    if (!selectedRows) return;
    switch (e.key) {
      case 'remove':
        this.remove("entrustHistory/remove",params,this.removeCallBack);
        break;
      default:
        break;
    }
  });
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
        'date': fieldsValue['date'].format('YYYY-MM-DD')
      };
      this.setState({
        formValues: params,
      });
      this.list("entrustHistory/fetch",params);
    });
  }


  /**
   * 渲染基础筛选条件
   * @returns {*}
   */
  renderSimpleForm() {
    const {getFieldDecorator} = this.props.form;
    var date = new Date();
    var seperator1 = "-";
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = year + seperator1 + month + seperator1 + strDate;

    return (
      <Form onSubmit={this.handleSearch} layout="inline">
          <Row gutter={{md: 6, lg: 24, xl: 48}}>

          <Col md={6} sm={24}>
           <FormItem label="时间">
                {getFieldDecorator('date',{
                  initialValue: moment(currentdate,dateToday),
                })(
                  <DatePicker style={{width:287}} disabledDate={this.handleData} />
                )}


              </FormItem>
              </Col>

              <Col md={6} sm={24}>
              <FormItem label="账户id">
                  {getFieldDecorator('accountId')(
                  <Input placeholder="请输入账户id"/>
                  )}
              </FormItem>
              </Col>

              <Col md={6} sm={24}>
              <FormItem label="客户id">
                  {getFieldDecorator('customerId')(
                  <Input placeholder="请输入客户id"/>
                  )}
              </FormItem>
              </Col>
                         <Col md={6} sm={24}>

              <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">查询</Button>
              <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("entrustHistory/fetch")}>重置</Button>
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
    var date = new Date();
    var seperator1 = "-";
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = year + seperator1 + month + seperator1 + strDate;
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
          <Row gutter={{md: 6, lg: 24, xl: 48}}>

          <Col md={6} sm={24}>
           <FormItem label="时间">
                {getFieldDecorator('date',{
                  initialValue: moment(currentdate,dateToday),
                })(
                  <DatePicker style={{width:287}} disabledDate={this.handleData} />
                )}
              </FormItem>
              </Col>
              <Col md={6} sm={24}>
              <FormItem label="账户id">
                  {getFieldDecorator('accountId')(
                  <Input placeholder="请输入账户id"/>
                  )}
              </FormItem>
              </Col>
              <Col md={6} sm={24}>
              <FormItem label="客户id">
                  {getFieldDecorator('customerId')(
                  <Input placeholder="请输入客户id"/>
                  )}
              </FormItem>
              </Col>
              <Col md={6} sm={24}>
              <FormItem label="币代码">
                  {getFieldDecorator('coinCode')(
                  <Input placeholder="请输入币代码"/>
                  )}
              </FormItem>
              </Col>
              <Col md={6} sm={24}>
              <FormItem label="委托价格">
                  {getFieldDecorator('entrustPrice')(
                  <Input placeholder="请输入委托价格"/>
                  )}
              </FormItem>
              </Col>
              <Col md={6} sm={24}>
              <FormItem label="委托数量">
                  {getFieldDecorator('entrustAmout')(
                  <Input placeholder="请输入委托数量"/>
                  )}
              </FormItem>
              </Col>
              <Col md={6} sm={24}>
              <FormItem label="entrustState">
                  {getFieldDecorator('entrustState')(
                  <Select placeholder="请选择" >
                      <Option value="0" >未成交</Option>
                      <Option value="1" >部分成交</Option>
                      <Option value="2" >全部成交</Option>
                      <Option value="3" >撤销</Option>
                  </Select>
                  )}
              </FormItem>
              </Col>
              <Col md={6} sm={24}>
              <FormItem label="category">
                  {getFieldDecorator('category')(
                  <Select placeholder="请选择" >
                      <Option value="0" >限价委托</Option>
                      <Option value="1" >计划委托</Option>
                  </Select>
                  )}
              </FormItem>
              </Col>
              <Col md={6} sm={24}>
              <FormItem label="orderFrom">
                  {getFieldDecorator('orderFrom')(
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
          <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("entrustHistory/fetch")}>重置</Button>
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
    const {entrustHistory: {list}, loading} = this.props;
    const {selectedRows, modalVisible} = this.state;

    const menu = (
      <Menu onClick={this.handleMenuClick} selectedKeys={[]}>
          <Menu.Item key="remove" className={this.hasPermissions("exchange:entrustHistory:remove")?"":styles.hidden}>批量删除</Menu.Item>
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

                      <Link to="/exchange_entrust/entrustHistory/add" className={this.hasPermissions("exchange:entrustHistory:add")?"":styles.hidden}>
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
