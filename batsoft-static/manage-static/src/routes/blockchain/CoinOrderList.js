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
@connect(({coinOrder, loading}) => ({
coinOrder,
  loading: loading.models.coinOrder,
}))
@Form.create()
export default class CoinOrderList extends BaseList {
  state =  this.state;
  columns = [
    {
      dataIndex: 'coinCode',
      title: '币代码',
      sortable: true,
    },

    {
      dataIndex: 'toAddress',
      title: 'to_address',
      sortable: true,
    },
    {
      dataIndex: 'amount',
      title: '交易金额',
      sortable: true,
    },
    {
      dataIndex: 'fee',
      title: '手续费',
      sortable: true,
    },
    {
      dataIndex: 'txHash',
      title: '区块链流水号',
      sortable: true,
    },
    {
      dataIndex: 'txType',
      title: '交易类型',
      sortable: true,
      render(val) {
        switch (val){
          case 0:
            return <Badge status={statusMap[0]} text="系统外"/>;
            break;
          case 1:
            return <Badge status={statusMap[1]} text="系统内"/>;
            break;
        }
      },
    },
    {
      dataIndex: 'blockNumber',
      title: 'block_number',
      sortable: true,
    },



    {
      dataIndex: 'consume',
      title: '是否确认',
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
      dataIndex: 'updateTime',
      title: '更新时间',
      sortable: true,
    },
    {
      title: '操作',
      dataIndex: 'id',
      render: val => (
        <Fragment>
          <Link className={this.hasPermissions("blockchain:coinOrder:see")?"":styles.hidden} to={{pathname: `/blockchain/coinOrder/info/${val}`}}>查看</Link>
          <Divider type="vertical"/>
          <Link className={this.hasPermissions("blockchain:coinOrder:edit")?"":styles.hidden} to={{pathname: `/blockchain/coinOrder/edit/${val}`}}>编辑</Link>
        </Fragment>
      ),
    },
  ];

  /**
   * 初始化
   */
  componentDidMount() {
    this.list("coinOrder/fetch");
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
    this.list("coinOrder/fetch",params);
  }

  /**
   * 删除回调事件
   * @param e
   */
  removeCallBack = () =>{
    this.setState({
      selectedRows: [],
    });
    this.list("coinOrder/fetch");
  }
  handleMenuClick = (e) => {
    const {selectedRows} = this.state;
    const params={
      ids: selectedRows.map(row => row.id).join(','),
    }
    if (!selectedRows) return;
    switch (e.key) {
      case 'remove':
        this.remove("coinOrder/remove",params,this.removeCallBack);
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
      this.list("coinOrder/fetch",params);
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
              <FormItem label="from">
                  {getFieldDecorator('fromAddress_LK')(
                  <Input placeholder="请输入from"/>
                  )}
              </FormItem>
              </Col>





              <Col md={8} sm={24}>
              <FormItem label="txType">
                  {getFieldDecorator('txType_EQ')(
                  <Select placeholder="请选择" >
                      <Option value="0" >系统外</Option>
                      <Option value="1" >系统内</Option>
                  </Select>
                  )}
              </FormItem>
              </Col>
                         <Col md={8} sm={24}>

              <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">查询</Button>
              <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("coinOrder/fetch")}>重置</Button>
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
              <FormItem label="from">
                  {getFieldDecorator('fromAddress_LK')(
                  <Input placeholder="请输入from"/>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="txType">
                  {getFieldDecorator('txType_EQ')(
                  <Select placeholder="请选择" >
                      <Option value="0" >系统外</Option>
                      <Option value="1" >系统内</Option>
                  </Select>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="consume">
                  {getFieldDecorator('consume_EQ')(
                  <Select placeholder="请选择" >
                      <Option value="0" >否</Option>
                      <Option value="1" >是</Option>
                  </Select>
                  )}
              </FormItem>
              </Col>

          </Row>
          <div style={{overflow: 'hidden'}}>
          <span style={{float: 'right', marginBottom: 24}}>
          <Button type="primary" htmlType="submit">查询</Button>
          <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("coinOrder/fetch")}>重置</Button>
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
    const {coinOrder: {list}, loading} = this.props;
    const {selectedRows, modalVisible} = this.state;

    const menu = (
      <Menu onClick={this.handleMenuClick} selectedKeys={[]}>
          <Menu.Item key="remove" className={this.hasPermissions("blockchain:coinOrder:remove")?"":styles.hidden}>批量删除</Menu.Item>
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

                      <Link className={this.hasPermissions("blockchain:coinOrder:add")?"":styles.hidden} to="/blockchain/coinOrder/add">
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
