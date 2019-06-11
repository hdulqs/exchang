import React, {Fragment} from 'react';
import {connect} from 'dva';
import moment from 'moment';
import {
  Badge, Button, Card, Col, DatePicker, Divider, Dropdown, Form, Icon, Input, Menu, Modal, Radio, Row, Select
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

const CreateForm = Form.create()((props) => {
  const {modalVisible, form, handleAdd, handleModalVisible, priceCoins} = props;

  const options = [];
  priceCoins.forEach((item) => {
    options.push(<Option key={item.coinCode}>{item.coinName}</Option>);
  });

  const okHandle = () => {
    form.validateFields((err, fieldsValue) => {
      if (err) return;
      handleAdd(fieldsValue);
    });
  };
  return (
    <Modal
      title="选择定价币"
      visible={modalVisible}
      onOk={okHandle}
      onCancel={() => handleModalVisible()}
    >
      <FormItem
        labelCol={{span: 5}}
        wrapperCol={{span: 15}}
        label="选择定价币"
      >
        {form.getFieldDecorator('priceCoin', {
          rules: [{required: true, message: '请选择定价币'}],
        })(
          <Select
            mode="multiple"
            style={{width: '100%'}}
            placeholder="请选择定价币"
          >
            {options}
          </Select>
        )}
      </FormItem>
    </Modal>
  );
});
@connect(({coin, loading}) => ({
  coin,
  loading: loading.models.coin,
}))
@Form.create()
export default class CoinList extends BaseList {
  state = {
    ...this.state,
    modalVisible: false,
    priceCoins: [],
  }
  columns = [
    {
      dataIndex: 'coinName',
      title: '币种名称',
      sortable: true,
    },
    {
      dataIndex: 'coinCode',
      title: '币种代码',
      sortable: true,
    },
    {
      dataIndex: 'priceCoin',
      title: '是否定价币',
      sortable: true,
      render(val) {
        switch (val) {
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
      dataIndex: 'erc20Status',
      title: '是否erc20代币',
      sortable: true,
      render(val) {
        switch (val) {
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
      dataIndex: 'allowRecharge',
      title: '允许充值',
      sortable: true,
      render(val) {
        switch (val) {
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
      dataIndex: 'allowWithdraw',
      title: '允许提现',
      sortable: true,
      render(val) {
        switch (val) {
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
      dataIndex: 'chargeRate',
      title: '充手续费%',
      sortable: true,
    },
    {
      dataIndex: 'withdrawRate',
      title: '提手续费%',
      sortable: true,
    },
    {
      dataIndex: 'sellRate',
      title: '卖手续费%',
      sortable: true,
    },
    {
      dataIndex: 'buyRate',
      title: '买手续费%',
      sortable: true,
    },
    {
      dataIndex: 'sort',
      title: '排序',
      sortable: true,
    },
    {
      dataIndex: 'status',
      title: '状态',
      sortable: true,
      render(val) {
        switch (val) {
          case 0:
            return <Badge status={statusMap[0]} text="关闭"/>;
            break;
          case 1:
            return <Badge status={statusMap[1]} text="开启"/>;
            break;
          case 2:
            return <Badge status={statusMap[2]} text="下架"/>;
            break;
        }
      },
    },
    {
      title: '操作',
      dataIndex: 'id',
      render: val => (
        <Fragment>
          <Link className={this.hasPermissions("exchange:coin:see")?"":styles.hidden}  to={{pathname: `/exchange/coin/info/${val}`}}>查看</Link>
          <Divider type="vertical"/>
          <Link className={this.hasPermissions("exchange:coin:edit")?"":styles.hidden} to={{pathname: `/exchange/coin/edit/${val}`}}>编辑</Link>
        </Fragment>
      ),
    },
  ];
  /**
   * 初始化
   */
  componentDidMount() {
    const {dispatch} = this.props;
    dispatch({
      type: 'coin/findPriceCoins',
    });
    this.list("coin/fetch");
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
    this.list("coin/fetch", params);
  }

  /**
   * 更多操作 批量删除 批量通过 ... 事件
   * @param e
   */
  removeCallBack = () => {
    this.setState({
      selectedRows: [],
    });
    this.list("coin/fetch");
  }

  /**
   * 开启状态
   * @param type
   * @param params
   */
  updateStatus = (type, params) => {
    confirm({
      title: '温馨提醒！',
      content: '是否确认批量开启币种？',
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
              this.list("coin/fetch");
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
   * 关闭状态
   * @param type
   * @param params
   */
  closeStatus = (type, params) => {
    confirm({
      title: '温馨提醒！',
      content: '是否确认批量关闭币种？关闭后将不能交易',
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
              this.list("coin/fetch");
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
   * 设置定价币
   * @param type
   * @param params
   */
  setPriceCoin = (type, params) => {
    confirm({
      title: '温馨提醒！',
      content: '是否确认批量设置定价币状态？',
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
              this.list("coin/fetch");
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
   * 允许充币
   * @param type
   * @param params
   */
  allowRecharge = (type, params) => {
    confirm({
      title: '温馨提醒！',
      content: '是否确认批量设置允许充币状态？',
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
              this.list("coin/fetch");
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
   * 允许提币
   * @param type
   * @param params
   */
  allowWithdraw = (type, params) => {
    confirm({
      title: '温馨提醒！',
      content: '是否确认批量设置允许提币状态？',
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
              this.list("coin/fetch");
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
   * 设置交易对弹框11
   */
  handleSetTrade = () => {
    const { coin: { findData } } = this.props;
    this.setState({
      priceCoins: findData,
    });
    this.handleModalVisible(true);
  }

  handleMenuClick = (e) => {
    const {selectedRows} = this.state;
    const params = {
      ids: selectedRows.map(row => row.id).join(','),
    }
    if (!selectedRows) return;
    switch (e.key) {
      case 'remove':
        this.remove("coin/remove", params, this.removeCallBack);
        break;
      case 'updateStatus':
        this.updateStatus("coin/updateStatus", params);
        break;
      case 'closeStatus':
        this.closeStatus("coin/closeStatus", params);
        break;
      case 'setPriceCoin':
        this.setPriceCoin("coin/setPriceCoin", params);
        break;
      case 'allowRecharge':
        this.allowRecharge("coin/allowRecharge", params);
        break;
      case 'allowWithdraw':
        this.allowWithdraw("coin/allowWithdraw", params);
        break;
      case 'setTrade':
        this.handleSetTrade("coin/setTrade", params);
        break;
      default:
        break;
    }
  }
  /**
   * 条件查询事件12
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
      this.list("coin/fetch", params);
    });
  }

  handleAdd = (value) => {

    confirm({
      title: '温馨提醒！',
      content: '确定设置交易对？',
      onOk: () => {
        const {selectedRows} = this.state;
        const params = {
          ids: selectedRows.map(row => row.id).join(','),
          prices: value.priceCoin.join(','),
        };
        const {dispatch} = this.props;
        dispatch({
          type: 'coin/setTrade',
          payload: {
            ...params,
          },
          callback: data => {
            if (data.success) {
              message.success(data.msg);
              this.list("coin/fetch");
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
    const {getFieldDecorator} = this.props.form;
    return (
      <Form onSubmit={this.handleSearch} layout="inline">
        <Row gutter={{md: 8, lg: 24, xl: 48}}>


          <Col md={8} sm={24}>
            <FormItem label="币种名称">
              {getFieldDecorator('coinName_LK')(
                <Input placeholder="请输入币种名称"/>
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
              <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("coin/fetch")}>重置</Button>
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
            <FormItem label="币种名称">
              {getFieldDecorator('coinName_LK')(
                <Input placeholder="请输入币种名称"/>
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
            <FormItem label="是否定价币">
              {getFieldDecorator('priceCoin_EQ')(
                <Select placeholder="请选择">
                  <Option value="0">否</Option>
                  <Option value="1">是</Option>
                </Select>
              )}
            </FormItem>
          </Col>
        </Row>
        <Row gutter={{md: 8, lg: 24, xl: 48}}>
          <Col md={8} sm={24}>
            <FormItem label="状态">
              {getFieldDecorator('status_EQ')(
                <Select placeholder="请选择">
                  <Option value="0">关闭</Option>
                  <Option value="1">开启</Option>
                  <Option value="2">下架</Option>
                </Select>
              )}
            </FormItem>
          </Col>

        </Row>
        <div style={{overflow: 'hidden'}}>
          <span style={{float: 'right', marginBottom: 24}}>
          <Button type="primary" htmlType="submit">查询</Button>
          <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("coin/fetch")}>重置</Button>
          <a style={{marginLeft: 8}} onClick={this.toggleForm}>
              收起 <Icon type="up"/>
          </a>
          </span>
        </div>
      </Form>
    );
  }

  /**
   * 主页面渲染1
   * @returns {*}
   */
  render() {
    const {coin: {list}, loading} = this.props;
    const {selectedRows, modalVisible, priceCoins} = this.state;
    const parentMethods = {
      handleAdd: this.handleAdd,
      handleModalVisible: this.handleModalVisible,
    };
    const menu = (
      <Menu onClick={this.handleMenuClick} selectedKeys={[]}>
        <Menu.Item key="remove" className={this.hasPermissions("exchange:coin:remove")?"":styles.hidden}>批量删除</Menu.Item>
        <Menu.Item key="updateStatus" className={this.hasPermissions("exchange:coin:updateStatus")?"":styles.hidden}>开启</Menu.Item>
        <Menu.Item key="closeStatus" className={this.hasPermissions("exchange:coin:closeStatus")?"":styles.hidden}>关闭</Menu.Item>
        <Menu.Item key="setPriceCoin" className={this.hasPermissions("exchange:coin:priceCoin")?"":styles.hidden}>设置定价币</Menu.Item>
        <Menu.Item key="allowRecharge" className={this.hasPermissions("exchange:coin:allowRecharge")?"":styles.hidden}>允许/禁止充币</Menu.Item>
        <Menu.Item key="allowWithdraw" className={this.hasPermissions("exchange:coin:allowDeposit")?"":styles.hidden}>允许/禁止提币</Menu.Item>
        <Menu.Item key="setTrade" className={this.hasPermissions("exchange:coin:setTrade")?"":styles.hidden}>设置交易对</Menu.Item>
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

              <Link to="/exchange/coin/add" className={this.hasPermissions("exchange:coin:add")?"":styles.hidden}>
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
        <CreateForm
          {...parentMethods}
          modalVisible={modalVisible}
          priceCoins={priceCoins}
        />
      </PageHeaderLayout>
    );
  }
}
