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
@connect(({wallet, loading}) => ({
wallet,
  loading: loading.models.wallet,
}))
@Form.create()
export default class WalletList extends BaseList {
  state =  this.state;
  columns = [
    {
      dataIndex: 'walletType',
      title: '钱包类型',
      sortable: true,
      render(val) {
        switch (val){
          case "BTC":
            return <Badge status={statusMap[0]} text="BTC"/>;
            break;
          case "ETH":
            return <Badge status={statusMap[0]} text="ETH"/>;
            break;
          case "EOS":
            return <Badge status={statusMap[0]} text="EOS"/>;
            break;
        }
      },
    },
    {
      dataIndex: 'walletName',
      title: '钱包名称',
      sortable: true,
    },
    {
      dataIndex: 'walletCode',
      title: '钱包代码',
      sortable: true,
    },
    {
      dataIndex: 'rpcProtocol',
      title: '协议',
      sortable: true,
    },
    {
      dataIndex: 'rpcIp',
      title: '钱包ip',
      sortable: true,
    },
    {
      dataIndex: 'rpcAccount',
      title: '钱包账号',
      sortable: true,
    },
    {
      dataIndex: 'rpcPassword',
      title: '钱包密码',
      sortable: true,
    },
    {
      dataIndex: 'rpcPort',
      title: '钱包端口',
      sortable: true,
    },
    {
      dataIndex: 'walletRemark',
      title: '钱包描述',
      sortable: true,
    },
    {
      dataIndex: 'status',
      title: '状态',
      sortable: true,
      render(val) {
        switch (val){
          case 0:
            return <Badge status={statusMap[0]} text="禁用"/>;
            break;
          case 1:
            return <Badge status={statusMap[1]} text="正常"/>;
            break;
        }
      },
    },
    {
      dataIndex: 'blockHigh',
      title: '区块高度',
      sortable: true,
    },
    {
      title: '操作',
      dataIndex: 'id',
      render: val => (
        <Fragment>
          <Link className={this.hasPermissions("blockchain:wallet:see")?"":styles.hidden} to={{pathname: `/blockchain/wallet/info/${val}`}}>查看</Link>
          <Divider type="vertical"/>
          <Link className={this.hasPermissions("blockchain:wallet:edit")?"":styles.hidden} to={{pathname: `/blockchain/wallet/edit/${val}`}}>编辑</Link>
        </Fragment>
      ),
    },
  ];

  /**
   * 初始化
   */
  componentDidMount() {
    this.list("wallet/fetch");
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
    this.list("wallet/fetch",params);
  }

  /**
   * 删除回调事件
   * @param e
   */
  removeCallBack = () =>{
    this.setState({
      selectedRows: [],
    });
    this.list("wallet/fetch");
  }
  handleMenuClick = (e) => {
    const {selectedRows} = this.state;
    const params={
      ids: selectedRows.map(row => row.id).join(','),
    }
    if (!selectedRows) return;
    switch (e.key) {
      case 'remove':
        this.remove("wallet/remove",params,this.removeCallBack);
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
      this.list("wallet/fetch",params);
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
              <FormItem label="walletType">
                  {getFieldDecorator('walletType_EQ')(
                  <Select placeholder="请选择" >
                      <Option value="BTC" >BTC</Option>
                      <Option value="ETH" >ETH</Option>
                      <Option value="EOS" >EOS</Option>
                  </Select>
                  )}
              </FormItem>
              </Col>

              <Col md={8} sm={24}>
              <FormItem label="钱包名称">
                  {getFieldDecorator('walletName_LK')(
                  <Input placeholder="请输入钱包名称"/>
                  )}
              </FormItem>
              </Col>
                         <Col md={8} sm={24}>

              <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">查询</Button>
              <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("wallet/fetch")}>重置</Button>
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
              <FormItem label="walletType">
                  {getFieldDecorator('walletType_EQ')(
                  <Select placeholder="请选择" >
                      <Option value="BTC" >BTC</Option>
                      <Option value="ETH" >ETH</Option>
                      <Option value="EOS" >EOS</Option>
                  </Select>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="钱包名称">
                  {getFieldDecorator('walletName_LK')(
                  <Input placeholder="请输入钱包名称"/>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="钱包代码">
                  {getFieldDecorator('walletCode_LK')(
                  <Input placeholder="请输入钱包代码"/>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="钱包ip">
                  {getFieldDecorator('rpcIp_LK')(
                  <Input placeholder="请输入钱包ip"/>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="status">
                  {getFieldDecorator('status_EQ')(
                  <Select placeholder="请选择" >
                      <Option value="0" >禁用</Option>
                      <Option value="1" >正常</Option>
                  </Select>
                  )}
              </FormItem>
              </Col>

          </Row>
          <div style={{overflow: 'hidden'}}>
          <span style={{float: 'right', marginBottom: 24}}>
          <Button type="primary" htmlType="submit">查询</Button>
          <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("wallet/fetch")}>重置</Button>
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
    const {wallet: {list}, loading} = this.props;
    const {selectedRows, modalVisible} = this.state;

    const menu = (
      <Menu onClick={this.handleMenuClick} selectedKeys={[]}>
          <Menu.Item key="remove" className={this.hasPermissions("blockchain:wallet:remove")?"":styles.hidden}>批量删除</Menu.Item>
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

                      <Link className={this.hasPermissions("blockchain:wallet:add")?"":styles.hidden} to="/blockchain/wallet/add">
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
