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

@connect(({coinPair, loading}) => ({
coinPair,
  loading: loading.models.coinPair,
}))
@Form.create()
export default class CoinPairList extends BaseList {
  state =  this.state;
  columns = [
    {
      dataIndex: 'tradeCoinCode',
      title: '交易币代码',
      sortable: true,
    },
    {
      dataIndex: 'pricingCoinCode',
      title: '定价币代码',
      sortable: true,
    },
    {
      dataIndex: 'openPrice',
      title: '发行价',
      sortable: true,
    },

    {
      dataIndex: 'status',
      title: '状态',
      sortable: true,
      render(val) {
        switch (val){
          case 0:
            return <Badge status="processing" text="禁止交易"/>;
            break;
          case 1:
            return <Badge status="processing" text="开启交易"/>;
            break;
        }
      },
    },
    {
      dataIndex: 'recommend',
      title: '是否推荐',
      sortable: true,
      render(val) {
        switch (val){
          case 0:
            return <Badge status="processing" text="否"/>;
            break;
          case 1:
            return <Badge status="processing" text="是"/>;
            break;
          default:
            return <Badge status="processing" text="否"/>;
            break;
        }
      },
    },
    {
      dataIndex: 'sort',
      title: '排序',
      sortable: true,
    },
    {
      title: '操作',
      dataIndex: 'id',
      render: val => (
        <Fragment>
          <Link className={this.hasPermissions("exchange:coinPair:see")?"":styles.hidden} to={{pathname: `/exchange/coinPair/info/${val}`}}>查看</Link>
          <Divider type="vertical"/>
          <Link className={this.hasPermissions("exchange:coinPair:edit")?"":styles.hidden} to={{pathname: `/exchange/coinPair/edit/${val}`}}>编辑</Link>
        </Fragment>
      ),
    },
  ];
  /**
   * 初始化
   */
  componentDidMount() {
    this.list("coinPair/fetch");
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
    this.list("coinPair/fetch",params);
  }

  /**
   * 更多操作 批量删除 批量通过 ... 事件
   * @param e
   */
  removeCallBack = () =>{
    this.setState({
      selectedRows: [],
    });
    this.list("coinPair/fetch");
  }
  handleMenuClick = (e) => {
    const {selectedRows} = this.state;
    const params={
      ids: selectedRows.map(row => row.id).join(','),
    }
    if (!selectedRows) return;
    switch (e.key) {
      case 'remove':
        this.remove("coinPair/remove",params,this.removeCallBack);
        break;
      case 'recommend':
        this.recommendCallBack("coinPair/recommendCoin",params);
        break;
      case 'unRecommend':
        this.unRecommendCallBack("coinPair/unRecommendCoin",params,this.unRecommendCallBack);
        break;
      default:
        break;
    }
  }
  /**
   * 设置推荐
   * @param type
   * @param params
   */
  recommendCallBack = (type, params) => {
    const ids = params.ids.split(",");
    confirm({
      title: '温馨提醒！',
      content: '是否确认设置为推荐币(最好不超过5个)',
      onOk: () => {
        const {dispatch} = this.props;
        dispatch({
          type: type,
          payload: {
            ...params,
          },
          callback: data => {
            if (data.success) {
              debugger
              message.success(data.msg);
              this.list("coinPair/fetch");
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
   * 取消推荐
   * @param type
   * @param params
   */
  unRecommendCallBack = (type, params) => {
    debugger
    const ids = params.ids.split(",");
    confirm({
      title: '温馨提醒！',
      content: '是否取消推荐？',
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
              this.list("coinPair/fetch");
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
      this.list("coinPair/fetch",params);
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
              <FormItem label="交易币代码">
                  {getFieldDecorator('tradeCoinCode_LK')(
                  <Input placeholder="请输入交易币代码"/>
                  )}
              </FormItem>
              </Col>

              <Col md={8} sm={24}>
              <FormItem label="定价币代码">
                  {getFieldDecorator('pricingCoinCode_LK')(
                  <Input placeholder="请输入定价币代码"/>
                  )}
              </FormItem>
              </Col>
                         <Col md={8} sm={24}>

              <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">查询</Button>
              <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("coinPair/fetch")}>重置</Button>
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
              <FormItem label="交易币代码">
                  {getFieldDecorator('tradeCoinCode_LK')(
                  <Input placeholder="请输入交易币代码"/>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="定价币代码">
                  {getFieldDecorator('pricingCoinCode_LK')(
                  <Input placeholder="请输入定价币代码"/>
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
          <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("coinPair/fetch")}>重置</Button>
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
    const {coinPair: {list}, loading} = this.props;
    const {selectedRows, modalVisible} = this.state;

    const menu = (
      <Menu onClick={this.handleMenuClick} selectedKeys={[]}>
          <Menu.Item key="remove" className={this.hasPermissions("exchange:coinPair:remove")?"":styles.hidden}>批量删除</Menu.Item>
          <Menu.Item key="recommend" className={this.hasPermissions("exchange:coinPair:recommend")?"":styles.hidden}>推荐设置</Menu.Item>
          <Menu.Item key="unRecommend" className={this.hasPermissions("exchange:coinPair:unRecommend")?"":styles.hidden}>取消推荐</Menu.Item>
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

                      {/*<Link to="/exchange/coinPair/add">
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
