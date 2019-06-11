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
@connect(({ercCoin, loading}) => ({
ercCoin,
  loading: loading.models.ercCoin,
}))
@Form.create()
export default class ErcCoinList extends BaseList {
  state =  this.state;
  columns = [
    {
      dataIndex: 'block',
      title: '链类型',
      sortable: true,
    },
    {
      dataIndex: 'coinCode',
      title: '代币Code',
      sortable: true,
    },
    {
      dataIndex: 'coinAddress',
      title: '合约地址',
      sortable: true,
    },
    {
      dataIndex: 'coinDecimals',
      title: '代币精度',
      sortable: true,
      render(val) {
        switch (val){
          case "ether":
            return <Badge status={statusMap[0]} text="18"/>;
            break;
          case "kwei":
            return <Badge status={statusMap[0]} text="3"/>;
            break;
          case "mwei":
            return <Badge status={statusMap[0]} text="6"/>;
            break;
          case "qwei":
            return <Badge status={statusMap[0]} text="9"/>;
            break;
          case "eight":
            return <Badge status={statusMap[0]} text="8"/>;
            break;
          case "szabo":
            return <Badge status={statusMap[0]} text="12"/>;
            break;
          case "finney":
            return <Badge status={statusMap[0]} text="15"/>;
            break;
          case "kether":
            return <Badge status={statusMap[0]} text="21"/>;
            break;
          case "mether":
            return <Badge status={statusMap[0]} text="24"/>;
            break;
        }
      },
    },
    {
      title: '操作',
      dataIndex: 'id',
      render: val => (
        <Fragment>
          <Link className={this.hasPermissions("blockchain:ercCoin:see")?"":styles.hidden} to={{pathname: `/blockchain/ercCoin/info/${val}`}}>查看</Link>
          <Divider type="vertical"/>
          <Link className={this.hasPermissions("blockchain:ercCoin:edit")?"":styles.hidden} to={{pathname: `/blockchain/ercCoin/edit/${val}`}}>编辑</Link>
        </Fragment>
      ),
    },
  ];

  /**
   * 初始化
   */
  componentDidMount() {
    this.list("ercCoin/fetch");
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
    this.list("ercCoin/fetch",params);
  }

  /**
   * 删除回调事件
   * @param e
   */
  removeCallBack = () =>{
    this.setState({
      selectedRows: [],
    });
    this.list("ercCoin/fetch");
  }
  handleMenuClick = (e) => {
    const {selectedRows} = this.state;
    const params={
      ids: selectedRows.map(row => row.id).join(','),
    }
    if (!selectedRows) return;
    switch (e.key) {
      case 'remove':
        this.remove("ercCoin/remove",params,this.removeCallBack);
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
      this.list("ercCoin/fetch",params);
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
              <FormItem label="代币Code">
                  {getFieldDecorator('coinCode_LK')(
                  <Input placeholder="请输入代币Code"/>
                  )}
              </FormItem>
              </Col>

              <Col md={8} sm={24}>
              <FormItem label="合约地址">
                  {getFieldDecorator('coinAddress_LK')(
                  <Input placeholder="请输入合约地址"/>
                  )}
              </FormItem>
              </Col>
                         <Col md={8} sm={24}>

              <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">查询</Button>
              <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("ercCoin/fetch")}>重置</Button>
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
              <FormItem label="代币Code">
                  {getFieldDecorator('coinCode_LK')(
                  <Input placeholder="请输入代币Code"/>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="合约地址">
                  {getFieldDecorator('coinAddress_LK')(
                  <Input placeholder="请输入合约地址"/>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="coinDecimals">
                  {getFieldDecorator('coinDecimals_EQ')(
                  <Select placeholder="请选择" >
                      <Option value="ether" >18</Option>
                      <Option value="kwei" >3</Option>
                      <Option value="mwei" >6</Option>
                      <Option value="qwei" >9</Option>
                      <Option value="eight" >8</Option>
                      <Option value="szabo" >12</Option>
                      <Option value="finney" >15</Option>
                      <Option value="kether" >21</Option>
                      <Option value="mether" >24</Option>
                  </Select>
                  )}
              </FormItem>
              </Col>

          </Row>
          <div style={{overflow: 'hidden'}}>
          <span style={{float: 'right', marginBottom: 24}}>
          <Button type="primary" htmlType="submit">查询</Button>
          <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("ercCoin/fetch")}>重置</Button>
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
    const {ercCoin: {list}, loading} = this.props;
    const {selectedRows, modalVisible} = this.state;

    const menu = (
      <Menu onClick={this.handleMenuClick} selectedKeys={[]}>
          <Menu.Item key="remove" className={this.hasPermissions("blockchain:ercCoin:remove")?"":styles.hidden}>批量删除</Menu.Item>
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

                      <Link className={this.hasPermissions("blockchain:ercCoin:add")?"":styles.hidden} to="/blockchain/ercCoin/add">
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
