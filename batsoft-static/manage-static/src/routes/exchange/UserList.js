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
import {message} from "antd/lib/index";
const FormItem = Form.Item;
const confirm = Modal.confirm;
const {Option} = Select;
const RadioGroup = Radio.Group;
const {RangePicker} = DatePicker;
const dateFormat = 'YYYY-MM-DD HH:mm:ss';
const statusMap = ['default', 'processing', 'success', 'error'];


@connect(({customerAccount, loading}) => ({
customerAccount,
  loading: loading.models.customerAccount,
}))
@Form.create()
export default class UserList extends BaseList {
  state =  this.state;
  columns = [
    {
      dataIndex: 'userName',
      title: '用户名',
      sortable: true,
    },
    {
      dataIndex: 'realName',
      title: '真实姓名',
      sortable: true,
    },
    {
      dataIndex: 'areaCode',
      title: '手机区域号',
      sortable: true,
    },
    {
      dataIndex: 'userMobile',
      title: '手机号',
      sortable: true,
    },
    {
      title: '操作',
      dataIndex: 'id',
      render: val => (
        <Fragment>
          <Link className={this.hasPermissions("exchange:customerAccount:checkAccout")?"":styles.hidden}  to={{pathname: `/exchange_finance/customerAccount/check/${val}`}}>核算</Link>

        </Fragment>
      ),
    },
  ];
  /**
   * 初始化
   */
  componentDidMount() {
    this.list("customerAccount/userList");
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
    this.list("customerAccount/userList",params);
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
      this.list("customerAccount/userList",params);
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
            <FormItem label="真实姓名">
              {getFieldDecorator('realName_LK')(
                <Input placeholder="请输入真实姓名"/>
              )}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>

              <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">查询</Button>
              <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("user/fetch")}>重置</Button>
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
            <FormItem label="真实姓名">
              {getFieldDecorator('realName_LK')(
                <Input placeholder="请输入真实姓名"/>
              )}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>
            <FormItem label="手机号">
              {getFieldDecorator('userMobile_LK')(
                <Input placeholder="请输入手机号"/>
              )}
            </FormItem>
          </Col>


        </Row>
        <div style={{overflow: 'hidden'}}>
          <span style={{float: 'right', marginBottom: 24}}>
          <Button type="primary" htmlType="submit">查询</Button>
          <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("user/fetch")}>重置</Button>
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
    const {customerAccount: {list}, loading} = this.props;
    const {selectedRows, modalVisible} = this.state;

    debugger


    return (
      <PageHeaderLayout   >
          <Card bordered={false}>
              <div className={styles.tableList}>
                  <div className={styles.tableListForm}>
                      {this.renderForm()}
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
