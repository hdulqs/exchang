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
const dateToday= 'YYYY-MM-DD';
const dateFormat = 'YYYY-MM-DD HH:mm:ss';
const statusMap = ['default', 'processing', 'success', 'error'];

@connect(({customerAccountFreeze, loading}) => ({
customerAccountFreeze,
  loading: loading.models.customerAccountFreeze,
}))
@Form.create()
export default class CustomerAccountFreezeList extends BaseList {
  state =  this.state;
  columns = [

    {
      dataIndex: 'userName',
      title: '用户名',
      sortable: true,
    },
    {
      dataIndex: 'coinCode',
      title: '币代码',
      sortable: true,
    },
    {
      dataIndex: 'freezeType',
      title: '冻结类型',
      sortable: true,
      render(val) {
        switch (val) {
          case 'entrust':
            return "委托";
            break;
          case 'cancel':
            return "撤单";
            break;
          case 'c2c':
            return "C2C";
            break;
          case 'recharge':
            return "充值";
            break;
          case 'withdraw':
            return "提现";
            break;
        }
      },
    },
    {
      dataIndex: 'orderId',
      title: '订单id',
      sortable: true,
    },
    {
      dataIndex: 'freezeMoney',
      title: '冻结金额',
      sortable: true,
    },
    {
      dataIndex: 'createTime',
      title: '冻结时间',
      sortable: true,
    },
    {
      title: '操作',
      dataIndex: 'id',
      render: val => (
        <Fragment>
          <Link className={this.hasPermissions("exchange:customerAccountFreeze:see")?"":styles.hidden}  to={{pathname: `/exchange_finance/customerAccountFreeze/info/${val}`}}>查看</Link>
          <Divider type="vertical"/>
          <Link className={this.hasPermissions("exchange:customerAccountFreeze:edit")?"":styles.hidden}  to={{pathname: `/exchange_finance/customerAccountFreeze/edit/${val}`}}>编辑</Link>
        </Fragment>
      ),
    },
  ];
  /**
   * 初始化
   */
  componentDidMount() {
    this.list("customerAccountFreeze/fetch");
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
    this.list("customerAccountFreeze/fetch",params);
  }

  /**
   * 更多操作 批量删除 批量通过 ... 事件
   * @param e
   */
  removeCallBack = () =>{
    this.setState({
      selectedRows: [],
    });
    this.list("customerAccountFreeze/fetch");
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
        this.remove("customerAccountFreeze/remove",params,this.removeCallBack);
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
      this.list("customerAccountFreeze/fetch",params);
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
              
              <FormItem label="账户号">
                  {getFieldDecorator('accountNumber')(
                  <Input placeholder="请输入账户号"/>
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

              <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">查询</Button>
              <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("customerAccountFreeze/fetch")}>重置</Button>
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
              <FormItem label="账户号">
                  {getFieldDecorator('accountNumber')(
                  <Input placeholder="请输入账户号"/>
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

          </Row>
          <div style={{overflow: 'hidden'}}>
          <span style={{float: 'right', marginBottom: 24}}>
          <Button type="primary" htmlType="submit">查询</Button>
          <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("customerAccountFreeze/fetch")}>重置</Button>
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
    const {customerAccountFreeze: {list}, loading} = this.props;
    const {selectedRows, modalVisible} = this.state;

    const menu = (
      <Menu onClick={this.handleMenuClick} selectedKeys={[]}>
          <Menu.Item key="remove" className={this.hasPermissions("exchange:customerAccountFreeze:remove")?"":styles.hidden} >批量删除</Menu.Item>
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

                      <Link to="/exchange_finance/customerAccountFreeze/add" className={this.hasPermissions("exchange:customerAccountFreeze:add")?"":styles.hidden} >
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
