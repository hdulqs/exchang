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


const CreateForm = Form.create()(props => {
  const { modalVisible, form, handleAdd, handleModalVisible } = props;
  const okHandle = () => {
    form.validateFields((err, fieldsValue) => {
      if (err) return;
      form.resetFields();
      handleAdd(fieldsValue);
    });
  };
  return (
    <Modal
      title="创建钱包地址"
      visible={modalVisible}
      onOk={okHandle}
      onCancel={() => handleModalVisible()}
    >

      <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="钱包">
        {form.getFieldDecorator('coinCode', {
          rules: [{ required: true, message: 'Please input some description...' }],
        })(<Input placeholder="请输入" />)}
      </FormItem>
    </Modal>
  );
});

@connect(({depositWallet, loading}) => ({
depositWallet,
  loading: loading.models.depositWallet,
}))
@Form.create()
export default class DepositWalletList extends BaseList {
  state =  this.state;
  columns = [
        {
        dataIndex: 'walletType',
        title: '钱包类型',
        sortable: true,
       },
        {
        dataIndex: 'coinCode',
        title: '币代码',
        sortable: true,
       },
        {
        dataIndex: 'walletAddress',
        title: '提币地址',
        sortable: true,
       },
        {
        dataIndex: 'walletAmount',
        title: '币数量',
        sortable: true,
       },
        {
        dataIndex: 'fee',
        title: '旷工费',
        sortable: true,
       },

    {
      dataIndex: 'gasPrice',
      title: 'gas',
      sortable: true,
    },

    {
      dataIndex: 'gasLimit',
      title: 'gasLimit',
      sortable: true,
    },

        {
        dataIndex: 'memo',
        title: 'memo标签',
        sortable: true,
       },
  {
    title: '操作',
    dataIndex: 'id',
    render: val => (
      <Fragment>
          <Link className={this.hasPermissions("blockchain:depositWallet:see")?"":styles.hidden} to={{pathname: `/blockchain/depositWallet/info/${val}`}}>查看</Link>
          <Divider type="vertical"/>
          <Link className={this.hasPermissions("blockchain:depositWallet:edit")?"":styles.hidden} to={{pathname: `/blockchain/depositWallet/edit/${val}`}}>编辑</Link>
      </Fragment>
    ),
  },
];
  /**
   * 初始化
   */
  componentDidMount() {
    this.list("depositWallet/fetch");
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
    this.list("depositWallet/fetch",params);
  }

  /**
   * 删除回调事件
   * @param e
   */
  removeCallBack = () =>{
    this.setState({
      selectedRows: [],
    });
    this.list("depositWallet/fetch");
  }
  handleMenuClick = (e) => {
    const {selectedRows} = this.state;
    const params={
      ids: selectedRows.map(row => row.id).join(','),
    }
    if (!selectedRows) return;
    switch (e.key) {
      case 'remove':
        this.remove("depositWallet/remove",params,this.removeCallBack);
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
      this.list("depositWallet/fetch",params);
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
              <FormItem label="钱包类型">
                  {getFieldDecorator('walletType_LK')(
                  <Input placeholder="请输入钱包类型"/>
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

              <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">查询</Button>
              <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("depositWallet/fetch")}>重置</Button>
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
              <FormItem label="钱包类型">
                  {getFieldDecorator('walletType_LK')(
                  <Input placeholder="请输入钱包类型"/>
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

          </Row>
          <div style={{overflow: 'hidden'}}>
          <span style={{float: 'right', marginBottom: 24}}>
          <Button type="primary" htmlType="submit">查询</Button>
          <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("depositWallet/fetch")}>重置</Button>
          <a style={{marginLeft: 8}} onClick={this.toggleForm}>
              收起 <Icon type="up"/>
          </a>
          </span>
          </div>
      </Form>
    );
  }



  handleModalVisible = flag => {
    this.setState({
      modalVisible: !!flag,
    });
  };
  handleAdd = (value) => {

    confirm({
      title: '温馨提醒！',
      content: '确定生成币地址？',
      onOk: () => {
        const params = {
          coinCode:value.coinCode,
        };
        const {dispatch} = this.props;
        dispatch({
          type: 'depositWallet/createAddress',
          payload: {
            ...params,
          },
          callback: data => {
            if (data.success) {
              message.success(data.msg);
              this.list("depositWallet/fetch");
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
   * 主页面渲染
   * @returns {*}
   */
  render() {
    const {depositWallet: {list}, loading} = this.props;
    const {selectedRows, modalVisible} = this.state;

    const menu = (
      <Menu onClick={this.handleMenuClick} selectedKeys={[]}>
          <Menu.Item className={this.hasPermissions("blockchain::depositWallet:remove")?"":styles.hidden} key="remove">批量删除</Menu.Item>
      </Menu>
    );
    const parentMethods = {
      handleAdd: this.handleAdd,
      handleModalVisible: this.handleModalVisible,
    };

    return (
      <PageHeaderLayout   >
          <Card bordered={false}>
              <div className={styles.tableList}>
                  <div className={styles.tableListForm}>
                      {this.renderForm()}
                  </div>
                  <div className={styles.tableListOperator}>

                    <Button className={this.hasPermissions("blockchain:depositWallet:createAddress")?"":styles.hidden} icon="plus" type="primary" onClick={() => this.handleModalVisible(true)}>
                      生成提币地址
                    </Button>
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
        <CreateForm {...parentMethods} modalVisible={modalVisible} />
      </PageHeaderLayout>
    );
  }
}
