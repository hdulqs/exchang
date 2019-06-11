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

const confirm = Modal.confirm;
const FormItem = Form.Item;
const {Option} = Select;
const RadioGroup = Radio.Group;
const {RangePicker} = DatePicker;
const dateFormat = 'YYYY-MM-DD HH:mm:ss';
const statusMap = ['default', 'processing', 'success', 'error'];

@connect(({coinWithdraw, loading}) => ({
coinWithdraw,
  loading: loading.models.coinWithdraw,
}))
@Form.create()
export default class CoinWithdrawList extends BaseList {
  state =  this.state;
  columns = [
    {
      dataIndex: 'toAddress',
      title: '提币地址',
      sortable: true,
    },
    {
      dataIndex: 'coinCode',
      title: '币种代码',
      sortable: true,
    },
    {
      dataIndex: 'memo',
      title: 'memo币地址',
      sortable: true,
    },
    {
      dataIndex: 'coinCount',
      title: '提币数量',
      sortable: true,
    },
    {
      dataIndex: 'actualCount',
      title: '实际到账',
      sortable: true,
    },
    {
      dataIndex: 'fee',
      title: '手续费',
      sortable: true,
    },
    {
      dataIndex: 'status',
      title: '状态',
      sortable: true,
      render(val) {
        switch (val){
          case 0:
            return <Badge status={statusMap[0]} text="等待中"/>;
            break;
          case 1:
            return <Badge status={statusMap[1]} text="成功"/>;
            break;
          case 2:
            return <Badge status={statusMap[2]} text="失败"/>;
            break;
        }
      },
    },
    {
      dataIndex: 'txOrder',
      title: '交易单号',
      sortable: true,
    },
    {
      dataIndex: 'userName',
      title: '用户名',
      sortable: true,
    },
    {
      dataIndex: 'userMobile',
      title: '用户手机号',
      sortable: true,
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
          <Link className={this.hasPermissions("exchange:coinWithdraw:see")?"":styles.hidden}  to={{pathname: `/exchange_finance/coinWithdraw/info/${val}`}}>查看</Link>
          {/*<Divider type="vertical"/>
           <Link to={{pathname: `/exchange_finance/coinWithdraw/edit/${val}`}}>编辑</Link>*/}
        </Fragment>
      ),
    },
  ];
  /**
   * 初始化
   */
  componentDidMount() {
    this.list("coinWithdraw/fetch");
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
    this.list("coinWithdraw/fetch",params);
  }

  /**
   * 删除回调事件
   * @param e
   */
  removeCallBack = () =>{
    this.setState({
      selectedRows: [],
    });
    this.list("coinWithdraw/fetch");
  }
  /**
   * 审核通过
   * @param type
   * @param params
   */
  pass = (type, params) => {
    const ids = params.ids.split(",");
    confirm({
      title: '温馨提醒！',
      content: '是否确认该客户可以提现？',
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
              this.list("coinWithdraw/fetch");
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
    confirm({
      title: '温馨提醒！',
      content: '是否拒绝该客户的提现申请？',
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
              this.list("coinWithdraw/fetch");
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
  handleMenuClick = (e) => {
    const {selectedRows} = this.state;
    const params={
      ids: selectedRows.map(row => row.id).join(','),
    }
    if (!selectedRows) return;
    switch (e.key) {
      case 'remove':
        this.remove("coinWithdraw/remove",params,this.removeCallBack);
        break;
      case 'pass':
        this.pass("coinWithdraw/passStatus",params);
        break;
      case 'refuse':
        this.refuse("coinWithdraw/refuseStatus",params,this);
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
      this.list("coinWithdraw/fetch",params);
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
              <FormItem label="提币地址">
                  {getFieldDecorator('toAddress_LK')(
                  <Input placeholder="请输入提币地址"/>
                  )}
              </FormItem>
              </Col>

              <Col md={8} sm={24}>
              <FormItem label="币种代码">
                  {getFieldDecorator('coinCode_LK')(
                  <Input placeholder="请输入币种代码"/>
                  )}
              </FormItem>
              </Col>
                         <Col md={8} sm={24}>

              <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">查询</Button>
              <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("coinWithdraw/fetch")}>重置</Button>
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
              <FormItem label="提币地址">
                  {getFieldDecorator('toAddress_LK')(
                  <Input placeholder="请输入提币地址"/>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="币种代码">
                  {getFieldDecorator('coinCode_LK')(
                  <Input placeholder="请输入币种代码"/>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="status">
                  {getFieldDecorator('status_EQ')(
                  <Select placeholder="请选择" >
                      <Option value="0" >等待中</Option>
                      <Option value="1" >成功</Option>
                      <Option value="2" >失败</Option>
                  </Select>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="交易单号">
                  {getFieldDecorator('txOrder_LK')(
                  <Input placeholder="请输入交易单号"/>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="用户名">
                  {getFieldDecorator('userName_LK')(
                  <Input placeholder="请输入用户名"/>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="用户手机号">
                  {getFieldDecorator('userMobile_LK')(
                  <Input placeholder="请输入用户手机号"/>
                  )}
              </FormItem>
              </Col>

          </Row>
          <div style={{overflow: 'hidden'}}>
          <span style={{float: 'right', marginBottom: 24}}>
          <Button type="primary" htmlType="submit">查询</Button>
          <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("coinWithdraw/fetch")}>重置</Button>
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
    const {coinWithdraw: {list}, loading} = this.props;
    const {selectedRows, modalVisible} = this.state;

    const menu = (
      <Menu onClick={this.handleMenuClick} selectedKeys={[]}>
          <Menu.Item key="remove" className={this.hasPermissions("exchange:coinWithdraw:remove")?"":styles.hidden}>批量删除</Menu.Item>
          <Menu.Item key="pass" className={this.hasPermissions("exchange:coinWithdraw:pass")?"":styles.hidden}>审核通过</Menu.Item>
          <Menu.Item key="refuse" className={this.hasPermissions("exchange:coinWithdraw:refuse")?"":styles.hidden}>审核拒绝</Menu.Item>
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

                      {/*<Link to="/exchange_finance/coinWithdraw/add">
                      <Button icon="plus" type="primary">
                          新增
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
