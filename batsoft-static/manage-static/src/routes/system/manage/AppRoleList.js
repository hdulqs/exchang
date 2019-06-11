import React, {Fragment} from 'react';
import {connect} from 'dva';
import {
  Badge, Button, Tree, Card, Col, message, Divider, Dropdown, Form, Icon, Input, Menu, Modal, Row, Select
} from 'antd';
import StandardTable from '../../../components/StandardTable';
import PageHeaderLayout from '../../../layouts/PageHeaderLayout';
import {Link} from 'dva/router';
import styles from '../../../assets/style/TableList.less';
import BaseList from '../../../components/BatSoft/List';
import {addPerm} from "../../../services/system/manage/AppRoleApi";

const TreeNode = Tree.TreeNode;
const FormItem = Form.Item;
const {Option} = Select;
const statusMap = ['default', 'processing', 'success', 'error'];


@connect(({appRole, appMenu, loading}) => ({
  appRole,
  appMenu,
  loading: loading.models.appRole,
}))
@Form.create()
export default class AppRoleList extends BaseList {

  state = {
    ...this.state,
    roleId: undefined,
    checkedKeys: [],
    confirmLoading: false,
  }
  /**
   * 授权确认
   */
  okHandle = () => {

    this.setState({
      confirmLoading: true,
    });

    const params = {
      roleId: this.state.roleId,
      //perms: this.state.checkedKeys.join(","),
      perms: this.state.checkedKeys.checked.join(","),

    };
    this.props.dispatch({
      type: 'appRole/addPerm',
      payload: {
        ...params,
      },
      callback: data => {
        message.success(data.msg);
        this.setState({
          confirmLoading: false,
          modalVisible: false,
          checkedKeys: [],
        });
      }
    })
    ;


  };
  /**
   * 渲染树node
   * @param data
   */
  renderTreeNodes = (data) => {
    return data.map((item) => {
      if (item.children) {
        return (
          <TreeNode title={item.title} key={item.key} dataRef={item}>
            {this.renderTreeNodes(item.children)}
          </TreeNode>
        );
      }
      return <TreeNode {...item} />;
    });
  }

  onCheck = (checkedKeys, info) => {
    debugger
    this.setState({
      checkedKeys: checkedKeys,
    });
    console.log('onCheck', checkedKeys, info);
  }

  columns = [
    {
      dataIndex: 'name',
      title: '角色名称',
      sortable: true,
    },
    {
      dataIndex: 'roleKey',
      title: '角色唯一标识',
      sortable: true,
    },
    {
      dataIndex: 'available',
      title: '是否禁用',
      sortable: true,
      render(val) {
        switch (val) {
          case 0:
            return <Badge status="processing" text="否"/>;
            break;
          case 1:
            return <Badge status="processing" text="是"/>;
            break;
        }
      },
    },
    {
      dataIndex: 'createUser',
      title: '创建人',
      sortable: true,
    },
    {
      dataIndex: 'updateUser',
      title: '修改人',
      sortable: true,
    },
    {
      dataIndex: 'remark',
      title: '备注',
      sortable: true,
    },
    {
      title: '操作',
      dataIndex: 'id',
      render: val => (
        <Fragment>
          <Link className={this.hasPermissions("system:manage:appRole:see")?"":styles.hidden} to={{pathname: `/security/manage/appRole/info/${val}`}}>查看</Link>
          <Divider type="vertical"/>
          <Link className={this.hasPermissions("system:manage:appRole:edit")?"":styles.hidden} to={{pathname: `/security/manage/appRole/edit/${val}`}}>编辑</Link>
          <Divider type="vertical"/>
          <a className={this.hasPermissions("system:manage:appRole:perm")?"":styles.hidden} href={"javascript:void(0)"} onClick={() => this.handleSelectPermissions(`${val}`)}>授权</a>
        </Fragment>
      ),
    },
  ];


  /**
   * 初始化
   */
  componentDidMount() {
    this.list("appRole/fetch");
  }

  /**
   * 授权
   * @param roleid
   */
  handleSelectPermissions = (roleid) => {
    this.setState({
      roleId: roleid,
    });
    const {dispatch} = this.props;
    dispatch({
      type: 'appMenu/menuTreeData',
      payload: {
      },
      callback: data => {
        this.initSelectMenu(roleid);
      },
    });

    this.handleModalVisible(true);
  }

  /**
   * 初始化已有权限
   * @param roleid
   */
  initSelectMenu = (roleid) => {
    const {dispatch} = this.props;
    dispatch({
      type: 'appRole/findMenu',
      payload: {
        roleid: roleid,
      },
      callback: data => {
        const menuIds = [];
        data.forEach(item => {
          menuIds.push(item.menuId);
        })
        debugger
        this.setState({
          checkedKeys: menuIds,
        });
      },
    });

  }

  handleModalVisible = (flag) => {
    this.setState({
      modalVisible: !!flag,
    });
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
    this.list("appRole/fetch", params);
  }

  /**
   * 更多操作 批量删除 批量通过 ... 事件
   * @param e
   */
  removeCallBack = () => {
    this.setState({
      selectedRows: [],
    });
    this.list("appRole/fetch");
  }
  handleMenuClick = (e) => {
    const {selectedRows} = this.state;
    const params = {
      ids: selectedRows.map(row => row.id).join(','),
    }
    if (!selectedRows) return;
    switch (e.key) {
      case 'remove':
        this.remove("appRole/remove", params, this.removeCallBack);
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
      this.list("appRole/fetch", params);
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
            <FormItem label="角色名称">
              {getFieldDecorator('name_LK')(
                <Input placeholder="请输入角色名称"/>
              )}
            </FormItem>
          </Col>


          <Col md={8} sm={24}>

              <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">查询</Button>
              <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("appRole/fetch")}>重置</Button>
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
            <FormItem label="角色名称">
              {getFieldDecorator('name_LK')(
                <Input placeholder="请输入角色名称"/>
              )}
            </FormItem>
          </Col>

        </Row>
        <div style={{overflow: 'hidden'}}>
          <span style={{float: 'right', marginBottom: 24}}>
          <Button type="primary" htmlType="submit">查询</Button>
          <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("appRole/fetch")}>重置</Button>
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
    const {appRole: {list}, appMenu: {menuTreeData}, loading} = this.props;
    const {selectedRows, confirmLoading} = this.state;
    if (menuTreeData.data === undefined) menuTreeData.data = [];
    const menu = (
      <Menu onClick={this.handleMenuClick} selectedKeys={[]}>
        <Menu.Item key="remove" className={this.hasPermissions("system:manage:appRole:remove")?"":styles.hidden}>批量删除</Menu.Item>
      </Menu>
    );
    return (
      <PageHeaderLayout>
        <Card bordered={false}>
          <div className={styles.tableList}>
            <div className={styles.tableListForm}>
              {this.renderForm()}
            </div>
            <div className={styles.tableListOperator}>

              <Link to="/security/manage/appRole/add" className={this.hasPermissions("system:manage:appRole:add")?"":styles.hidden}>
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
        <Modal
          title="角色授权"
          visible={this.state.modalVisible}
          onOk={this.okHandle}
          confirmLoading={confirmLoading}
          onCancel={() => this.handleModalVisible()}
        >
          <Tree
            checkStrictly={true}
            checkable
            onCheck={this.onCheck}
            checkedKeys={this.state.checkedKeys}
          >
            {this.renderTreeNodes(menuTreeData.data)}
          </Tree>
        </Modal>
      </PageHeaderLayout>
    );
  }
}
