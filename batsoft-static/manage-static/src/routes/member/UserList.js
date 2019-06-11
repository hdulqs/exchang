import React, { Fragment } from 'react';
import { connect } from 'dva';
import moment from 'moment';
import {
  Badge, Button, Card, Col, DatePicker, Divider, Dropdown, Form, Icon, Input, Menu, Modal, Radio, Row, Select
} from 'antd';
import StandardTable from '../../components/StandardTable';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import { Link } from 'dva/router';
import styles from '../../assets/style/TableList.less';
import BaseList from '../../components/BatSoft/List';
import { message } from "antd/lib/index";
import { getGlobData } from '../../utils/globData';

const confirm = Modal.confirm;
const FormItem = Form.Item;
const { Option } = Select;
const RadioGroup = Radio.Group;
const { RangePicker } = DatePicker;
const dateFormat = 'YYYY-MM-DD HH:mm:ss';
const statusMap = ['default', 'processing', 'success', 'error'];

//身份审核
const CreateForm = Form.create()((props) => {
  const { modalVisible, form, handleReview, handleModalVisible, selectedRows, hideReason, onHideReason, reviewobj } = props;
  // const user = selectedRows[0];
  const user = reviewobj;
  const globData = JSON.parse(getGlobData("system-globData"));

  const okHandle = () => {
    form.validateFields((err, fieldsValue) => {
      if (err) return;
      const data = {
        ids: user.id,
        data: JSON.stringify({
          ...fieldsValue
        })
      }
      handleReview(data);
    });
  };
  const handleSelectChange = (value) => {
    // console.log('111--------' + value)
    onHideReason(value);
  }
  return (
    <Modal
      width={800}
      title="身份信息审核"
      visible={modalVisible}
      user={user}
      onOk={okHandle}
      onCancel={() => handleModalVisible()}
    >

      <FormItem
        labelCol={{ span: 5 }}
        wrapperCol={{ span: 15 }}
        label="真实姓名"
      >
        {form.getFieldDecorator('realName', {
          initialValue: (typeof (user) == "undefined" || typeof (user) == null) ? '' : user.realName,
        })(
          <Input mode="multiple"
            style={{ width: '100%' }}
            placeholder="真实姓名"
            disabled={true}/>
        )}
      </FormItem>
      <FormItem
        labelCol={{ span: 5 }}
        wrapperCol={{ span: 15 }}
        label="身份证号码"
      >
        {form.getFieldDecorator('userCardNumber', {
          initialValue: (typeof (user) == "undefined" || typeof (user) == null) ? '' : user.userCardNumber,
        })(
          <Input mode="multiple"
            style={{ width: '100%' }}
            placeholder="身份证号码"
            disabled={true}/>
        )
        }
      </FormItem>
      <FormItem
        labelCol={{ span: 5 }}
        wrapperCol={{ span: 15 }}
        label="身份证正面"
      >
        <img alt="" style={{ width: 470, height: 250 }} src={globData.fileHost + ((typeof (user) == "undefined" || typeof (user) == null) ? '' : user.userCardFront)} />
      </FormItem>
      <FormItem
        labelCol={{ span: 5 }}
        wrapperCol={{ span: 15 }}
        label="身份证反面"
      >
        <img alt="" style={{ width: 470, height: 250 }} src={globData.fileHost + ((typeof (user) == "undefined" || typeof (user) == null) ? '' : user.userCardBack)} />
      </FormItem>
      <FormItem
        labelCol={{ span: 5 }}
        wrapperCol={{ span: 15 }}
        label="手持身份证照"
      >
        <img alt="" style={{ width: 470, height: 250 }} src={globData.fileHost + ((typeof (user) == "undefined" || typeof (user) == null) ? '' : user.userCardAll)} />
      </FormItem>
      <FormItem
        labelCol={{ span: 5 }}
        wrapperCol={{ span: 15 }}
        label="是否同意" >
        {form.getFieldDecorator('agree', {
          initialValue: true,
        })(
          <Select placeholder="请选择" onChange={handleSelectChange}>
            <Option value={true} onClick={this.fristAgree}>是</Option>
            <Option value={false} onClick={this.display_name}>否</Option>
          </Select>
        )}
      </FormItem>
      {
        !hideReason ? (
          <FormItem
            labelCol={{ span: 5 }}
            wrapperCol={{ span: 15 }}
            label="理由" >
            {form.getFieldDecorator('msg')(
              <Input placeholder="请输入拒绝理由" />
            )}
          </FormItem>
        ) : null
      }

    </Modal>
  );


});
@connect(({ user, loading }) => ({
  user,
  loading: loading.models.user,
}))
@Form.create()
export default class UserList extends BaseList {
  state = this.state;
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
      dataIndex: 'status',
      title: '状态',
      sortable: true,
      render(val) {
        switch (val) {
          case 0:
            return <Badge status={statusMap[0]} text="正常" />;
            break;
          case 1:
            return <Badge status={statusMap[1]} text="禁用" />;
            break;
        }
      },
    },
    {
      dataIndex: 'userCardType',
      title: '证件类型',
      sortable: true,
      render(val) {
        switch (val) {
          case "0":
            return <Badge status={statusMap[0]} text="身份证" />;
            break;
          case "1":
            return <Badge status={statusMap[1]} text="护照" />;
            break;
          case "2":
            return <Badge status={statusMap[2]} text="其它" />;
            break;
        }
      },
    },
    {
      dataIndex: 'userCardNumber',
      title: '证件号',
      sortable: true,
    },
    {
      dataIndex: 'loginNum',
      title: '登录次数',
      sortable: true,
    },
    {
      dataIndex: 'oldLoginTime',
      title: '上次登录时间',
      sortable: true,
    },
    {
      dataIndex: 'realState',
      title: '是否实名',
      sortable: true,
      render(val) {
        switch (val) {
          case 0:
            return <Badge status={statusMap[0]} text="未实名" />;
            break;
          case 1:
            return <Badge status={statusMap[1]} text="已实名" />;
            break;
          case 2:
            return <Badge status={statusMap[2]} text="待审核" />;
            break;
          case 3:
            return <Badge status={statusMap[3]} text="未通过" />;
            break;
        }
      },
    },
    {
      title: '操作',
      dataIndex: 'action',
      render: (text, record) => (
        <Fragment>
          <Link className={this.hasPermissions("member:user:see") ? "" : styles.hidden} to={{ pathname: `/member/user/info/${record.id}` }}>查看</Link>
          <Divider type="vertical" />
          <Link className={this.hasPermissions("member:user:edit") ? "" : styles.hidden} to={{ pathname: `/member/user/edit/${record.id}` }}>编辑</Link>
          {
            record.realState === 2 ? (
              <span>
                <Divider type="vertical" />
                <a href="javascript:;" onClick={() => this.onReview(record)}>审核</a>
              </span>
            ) : null
          }
        </Fragment>
      ),
    },
  ];

  fristAgree() {

  }
  display_name(){
    
  }
  /**
   * 初始化
   */
  componentDidMount() {
    this.props.dispatch({
      type: 'global/find',
    });
    this.list("user/fetch");
  }

  // ====================================Handle====================================================

  // 隐藏理由
  onHideReason = (value) => {
    // console.log('------' + value);
    this.setState({
      hideReason: value
    })
  }

  // 单个审核
  onReview = (row) => {
    this.setState({
      reviewobj: row
    }, () => {
      const params = {
        ids: row.id
      }
      this.handleSetTrade("coin/setTrade", params);
    })
  }

  /**
   * 列表标题筛选  分页查询 事件
   * @param pagination
   * @param filtersArg
   * @param sorter
   */
  handleStandardTableChange = (pagination, filtersArg, sorter) => {
    const params = this.handleStandardTableChangeParams(pagination, filtersArg, sorter);
    this.list("user/fetch", params);
  }

  /**
   * 删除回调事件
   * @param e
   */
  removeCallBack = () => {
    this.setState({
      selectedRows: [],
    });
    this.list("user/fetch");
  }
  handleMenuClick = (e) => {
    const { selectedRows } = this.state;
    const params = {
      ids: selectedRows.map(row => row.id).join(','),
    }
    if (!selectedRows) return;
    switch (e.key) {
      case 'remove':
        this.remove("user/remove", params, this.removeCallBack);
        break;
      case 'setTrade':
        // this.handleSetTrade("coin/setTrade", params);
        this.handleReview()
        break;
      default:
        break;
    }
  }
  /**
   * 设置交易对弹框11
   */
  handleSetTrade = () => {

    const { user: { findData } } = this.props;
    this.setState({
      user: findData,
    });
    this.handleModalVisible(true);
  }

  /**
   * 条件查询事件
   * @param e
   */
  handleSearch = (e) => {
    e.preventDefault();
    const { form } = this.props;
    form.validateFields((err, fieldsValue) => {



      if (err) return;
      const params = {
        ...fieldsValue,
      };
      this.setState({
        formValues: params,
      });
      this.list("user/fetch", params);
    });
  }

  //身份审核
  handleReview = (value) => {
    confirm({
      title: '温馨提醒！',
      content: '确认通过该用户信息么？',
      onOk: () => {
        let params = null;
        if (value) {
          params = value;
        } else {
          const { selectedRows } = this.state;
          const ids = selectedRows.filter(row => {
            return row.realState === 2;
          })
          if (ids.length) {
            params = { ids: ids.map(row => row.id).join(',')}
          } else {
            message.error('没有未审核的条目');
            return;
          }
        }
        // const params = value ? value : { ids: this.state.selectedRows.map(row => row.id).join(',')}
        const { dispatch } = this.props;
        dispatch({
          type: 'user/review',
          payload: {
            ...params,
          },
          callback: data => {
            if (data.success) {
              message.success(data.msg);
              this.handleModalVisible();
              this.list("user/fetch");
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
   * 渲染基础筛选条件
   * @returns {*}
   */
  renderSimpleForm() {
    const { getFieldDecorator } = this.props.form;
    return (
      <Form onSubmit={this.handleSearch} layout="inline">
        <Row gutter={{ md: 8, lg: 24, xl: 48 }}>



          <Col md={8} sm={24}>
            <FormItem label="用户名">
              {getFieldDecorator('userName_LK')(
                <Input placeholder="请输入用户名" />
              )}
            </FormItem>
          </Col>



          <Col md={8} sm={24}>
            <FormItem label="真实姓名">
              {getFieldDecorator('realName_LK')(
                <Input placeholder="请输入真实姓名" />
              )}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>

            <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">查询</Button>
              <Button style={{ marginLeft: 8 }} onClick={() => this.handleFormReset("user/fetch")}>重置</Button>
              <a style={{ marginLeft: 8 }} onClick={this.toggleForm}>
                展开 <Icon type="down" />
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
    const { getFieldDecorator } = this.props.form;
    const formItemLayout = {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 7 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 12 },
        md: { span: 10 },
      },
    };
    return (
      <Form onSubmit={this.handleSearch} layout="inline">
        <Row gutter={{ md: 8, lg: 24, xl: 48 }}>

          <Col md={8} sm={24}>
            <FormItem label="用户名">
              {getFieldDecorator('userName_LK')(
                <Input placeholder="请输入用户名" />
              )}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>
            <FormItem label="真实姓名">
              {getFieldDecorator('realName_LK')(
                <Input placeholder="请输入真实姓名" />
              )}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>
            <FormItem label="手机号">
              {getFieldDecorator('userMobile_LK')(
                <Input placeholder="请输入手机号" />
              )}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>
            <FormItem label="userCardType">
              {getFieldDecorator('userCardType_EQ')(
                <Select placeholder="请选择" >
                  <Option value="0" >身份证</Option>
                  <Option value="1" >护照</Option>
                  <Option value="2" >其它</Option>
                </Select>
              )}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>
            <FormItem label="证件号">
              {getFieldDecorator('userCardNumber_LK')(
                <Input placeholder="请输入证件号" />
              )}
            </FormItem>
          </Col>

        </Row>
        <div style={{ overflow: 'hidden' }}>
          <span style={{ float: 'right', marginBottom: 24 }}>
            <Button type="primary" htmlType="submit">查询</Button>
            <Button style={{ marginLeft: 8 }} onClick={() => this.handleFormReset("user/fetch")}>重置</Button>
            <a style={{ marginLeft: 8 }} onClick={this.toggleForm}>
              收起 <Icon type="up" />
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

    const { user: { list }, loading } = this.props;
    const { selectedRows, modalVisible, hideReason, reviewobj } = this.state;
    // console.log(this.state);
    const parentMethods = {
      handleReview: this.handleReview,
      handleModalVisible: this.handleModalVisible,
      global: global,
    };
    const menu = (
      <Menu onClick={this.handleMenuClick} selectedKeys={[]}>
        <Menu.Item key="remove" className={this.hasPermissions("member:user:remove") ? "" : styles.hidden} >批量删除</Menu.Item>
        <Menu.Item key="setTrade" className={this.hasPermissions("member:user:review") ? "" : styles.hidden} >身份审核</Menu.Item>
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

              <Link to="/member/user/add" className={this.hasPermissions("member:user:add") ? "" : styles.hidden}>
                <Button icon="plus" type="primary"  >
                  新增
                      </Button>
              </Link>
              {
                selectedRows.length > 0 && (
                  <span>
                    <Dropdown overlay={menu}>
                      <Button>
                        更多操作 <Icon type="down" />
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
          selectedRows={selectedRows}
          onHideReason={this.onHideReason}
          hideReason={hideReason}
          reviewobj={reviewobj}
        />
      </PageHeaderLayout>
    );
  }
}
