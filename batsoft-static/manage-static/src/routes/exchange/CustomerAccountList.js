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

const CreateForm = Form.create()((props) => {
  const {modalVisible, form, handleAdd, handleModalVisible} = props;

  const okHandle = () => {
    form.validateFields((err, fieldsValue) => {
      if (err) return;
      handleAdd(fieldsValue);
    });
  };
  return (
    <Modal
      title="手动充币"
      visible={modalVisible}
      onOk={okHandle}
      onCancel={() => handleModalVisible()}
    >
      <FormItem
        labelCol={{span: 5}}
        wrapperCol={{span: 15}}
        label="充币数量"
      >
        {form.getFieldDecorator('amount', {
          rules: [{
            required: true,
            message: '请正确填写充币数量',
          }],
        })(
          <Input placeholder="请输入充币数量"/>
        )}
      </FormItem>
    </Modal>
  );
});
@connect(({customerAccount, loading}) => ({
customerAccount,
  loading: loading.models.customerAccount,
}))
@Form.create()
export default class CustomerAccountList extends BaseList {
  state =  this.state;
  columns = [
    //  {
    //  dataIndex: 'accountNumber',
    //  title: '账户账号',
    //  sortable: true,
    // },
    {
      dataIndex: 'userName',
      title: '用户名',
      sortable: true,
    },
    {
      dataIndex: 'coinCode',
      title: '币种',
      sortable: true,
    },
    {
      dataIndex: 'coinAddress',
      title: '币地址',
      sortable: true,
    },
    {
      dataIndex: 'memo',
      title: '币地址标签',
      sortable: true,
    },
    {
      dataIndex: 'available',
      title: '可用金额',
      sortable: true,
    },
    {
      dataIndex: 'freeze',
      title: '冻结金额',
      sortable: true,
    },
    {
      dataIndex: 'status',
      title: '状态',
      sortable: true,
      render(val) {
        switch (val){
          case 0:
            return <Badge status="processing" text="正常"/>;
            break;
          case 1:
            return <Badge status="processing" text="冻结"/>;
            break;
        }
      },
    },
    {
      title: '操作',
      dataIndex: 'id',
      render: val => (
        <Fragment>
          <Link className={this.hasPermissions("exchange:customerAccount:see")?"":styles.hidden}  to={{pathname: `/exchange_finance/customerAccount/info/${val}`}}>查看</Link>
          <Divider type="vertical"/>
          <Link className={this.hasPermissions("exchange:customerAccount:edit")?"":styles.hidden}  to={{pathname: `/exchange_finance/customerAccount/edit/${val}`}}>编辑</Link>
        </Fragment>
      ),
    },
  ];
  /**
   * 初始化
   */
  componentDidMount() {
    this.list("customerAccount/fetch");
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
    this.list("customerAccount/fetch",params);
  }

  /**
   * 更多操作 批量删除 批量通过 ... 事件
   * @param e
   */
  removeCallBack = () =>{
    this.setState({
      selectedRows: [],
    });
    this.list("customerAccount/fetch");
  }

  /**
   * 手动充币
   */
  handleDepost = () => {

    this.handleModalVisible(true);
  }

  /**
   * 手动充币
   */
  depostAdd = (obj) => {

    debugger
    confirm({
      title: '温馨提醒！',
      content: '确定充币？',
      onOk: () => {
        const {selectedRows} = this.state;
        const params = {
          ids: selectedRows.map(row => row.id).join(','),
          amount: obj.amount,
        };
        const {dispatch} = this.props;
        dispatch({
          type: 'customerAccount/depostSave',
          payload: {
            ...params,
          },
          callback: data => {
            if (data.success) {
              message.success(data.msg);
              this.list("customerAccount/fetch");
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
        this.remove("customerAccount/remove",params,this.removeCallBack);
        break;
      case 'depost':
        this.handleDepost();
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
      this.list("customerAccount/fetch",params);
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
              <FormItem label="账户账号">
                  {getFieldDecorator('accountNumber_LK')(
                  <Input placeholder="请输入账户账号"/>
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

              <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">查询</Button>
              <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("customerAccount/fetch")}>重置</Button>
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
              <FormItem label="账户账号">
                  {getFieldDecorator('accountNumber_LK')(
                  <Input placeholder="请输入账户账号"/>
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
              <FormItem label="币种">
                  {getFieldDecorator('coinCode_LK')(
                  <Input placeholder="请输入币种"/>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="状态">
                  {getFieldDecorator('status_EQ')(
                  <Input placeholder="请输入状态"/>
                  )}
              </FormItem>
              </Col>

          </Row>
          <div style={{overflow: 'hidden'}}>
          <span style={{float: 'right', marginBottom: 24}}>
          <Button type="primary" htmlType="submit">查询</Button>
          <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("customerAccount/fetch")}>重置</Button>
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
    const parentMethods = {
      handleAdd: this.depostAdd,
      handleModalVisible: this.handleModalVisible,
    };
    const menu = (
      <Menu onClick={this.handleMenuClick} selectedKeys={[]}>
          <Menu.Item key="remove" className={this.hasPermissions("exchange:customerAccount:remove")?"":styles.hidden} >批量删除</Menu.Item>
          <Menu.Item key="depost" className={this.hasPermissions("exchange:customerAccount:remove")?"":styles.hidden} >手动充币</Menu.Item>
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

                      <Link to="/exchange_finance/customerAccount/add" className={this.hasPermissions("exchange:customerAccount:add")?"":styles.hidden}>
                      <Button icon="plus" type="primary"  >
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
        <CreateForm
          {...parentMethods}
          modalVisible={modalVisible}
        />
      </PageHeaderLayout>
    );
  }
}
