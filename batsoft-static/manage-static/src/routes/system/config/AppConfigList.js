import React, {Fragment} from 'react';
import {connect} from 'dva';
import moment from 'moment';
import {
  Badge, Button, Card, Col, DatePicker, Divider, Dropdown, Form, Icon, Input, Menu,  Modal, Radio, Row,Select
} from 'antd';
import StandardTable from '../../../components/StandardTable';
import PageHeaderLayout from '../../../layouts/PageHeaderLayout';
import {Link} from 'dva/router';
import styles from '../../../assets/style/TableList.less';
import BaseList from '../../../components/BatSoft/List';
import FileUpload from '../../../components/FileUpload';
const FormItem = Form.Item;
const { TextArea } = Input;
const {Option} = Select;
const RadioGroup = Radio.Group;
const {RangePicker} = DatePicker;
const dateFormat = 'YYYY-MM-DD HH:mm:ss';
const statusMap = ['default', 'processing', 'success', 'error'];


const CreateForm = Form.create()((props) => {
  const {modalVisible, form, handleAdd, handleModalVisible, configData,handleFileChange} = props;

  const options = [];


  if(configData.dataType==0){
    options.push(
      <FormItem
        labelCol={{span: 5}}
        wrapperCol={{span: 15}}
        label={configData.confName}
      >
        {form.getFieldDecorator(configData.confKey, {
          initialValue: configData.confValue,

        })(
          <Input placeholder={configData.description} />
        )}
      </FormItem>
    )
  }

  if(configData.dataType==1){
    options.push(
      <FormItem
        labelCol={{span: 5}}
        wrapperCol={{span: 15}}
        label={configData.confName}
      >
        {form.getFieldDecorator(configData.confKey, {
          initialValue: configData.confValue,

        })(
          <TextArea style={{minHeight: 32}} placeholder={configData.description} rows={4}/>
        )}
      </FormItem>
    )
  }

  if(configData.dataType==2){
    options.push(
      <FormItem
        labelCol={{span: 5}}
        wrapperCol={{span: 15}}
        label={configData.confName}
      >
        {form.getFieldDecorator(configData.confKey, {
          initialValue: configData.confValue,

        })(
          <RadioGroup >
            <Radio value={"1"}>是</Radio>
            <Radio value={"0"}>否</Radio>
          </RadioGroup>
        )}
      </FormItem>
    )
  }

  if(configData.dataType==3){
    options.push(
      <FormItem
        labelCol={{span: 5}}
        wrapperCol={{span: 15}}
        label={configData.confName}
      >
        {form.getFieldDecorator(configData.confKey, {
          initialValue: configData.confValue,

        })(
          <FileUpload
            callback={handleFileChange}>
          </FileUpload>
        )}
      </FormItem>
    )
  }

  const okHandle = () => {
    form.validateFields((err, fieldsValue) => {
      if (err) return;
      handleAdd(fieldsValue);
    });
  };
  return (
    <Modal
      title="设置参数"
      visible={modalVisible}
      onOk={okHandle}
      onCancel={() => handleModalVisible()}
    >
      {options}
    </Modal>
  );
});
@connect(({appConfig, loading}) => ({
appConfig,
  loading: loading.models.appConfig,
}))
@Form.create()
export default class AppConfigList extends BaseList {
  state = {
    ...this.state,
    modalVisible: false,
    fileList: null,
    configData: {},
  }
   columns = [
    {
      dataIndex: 'confKey',
      title: '配置key',
      sortable: true,
    },
    {
      dataIndex: 'confName',
      title: '配置名称',
      sortable: true,
    },
    {
      dataIndex: 'dataType',
      title: '数据类型',
      sortable: true,
      render(val) {
        switch (val){
          case 0:
            return <Badge status={statusMap[0]} text="文本"/>;
            break;
          case 1:
            return <Badge status={statusMap[1]} text="文本域"/>;
            break;
          case 2:
            return <Badge status={statusMap[2]} text="单项"/>;
            break;
          case 3:
            return <Badge status={statusMap[3]} text="文件"/>;
            break;
          case 4:
            return <Badge status={statusMap[4]} text="复选"/>;
            break;
        }
      },
    },

    {
      dataIndex: 'selectValue',
      title: '选项值',
      sortable: true,
    },
    {
      dataIndex: 'typeName',
      title: '类型名称',
      sortable: true,
    },
    /*{
      dataIndex: 'description',
      title: '描述',
      sortable: true,
    },*/
    {
      dataIndex: 'sort',
      title: '排序',
      sortable: true,
    },
    {
      title: '操作',
      dataIndex: 'id',
      render: (val ,record)=> (
        <Fragment>
          <Link className={this.hasPermissions("system:config:appConfig:see")?"":styles.hidden} to={{pathname: `/system/config/appConfig/info/${val}`}}>查看</Link>
          <Divider type="vertical"/>
          <Link className={this.hasPermissions("system:config:appConfig:edit")?"":styles.hidden} to={{pathname: `/system/config/appConfig/edit/${val}`}}>编辑</Link>
          <Divider type="vertical"/>
          <a className={this.hasPermissions("system:config:appConfig:setting")?"":styles.hidden}   onClick={()=>this.handleSetClick(record)}>
            设置
          </a>
        </Fragment>
      ),
    },
  ];
  /**
   * 初始化
   */
  componentDidMount() {
    this.list("appConfig/fetch");
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
    this.list("appConfig/fetch",params);
  }

  /**
   * 设置操作
   */
  handleSetClick =(record) =>{

    this.setState({
      configData: record,
    });

    this.handleModalVisible(true);
  }

  /**
   * 文件上传返回内容
   * @param fileList
   */
  handleFileChange = (fileList) =>{

    this.setState({ fileList });
  }

  /**
   * 设置值
   * @param value
   */
  handleSetting=(value)=>{

    if (this.state.fileList != null) {

      for(var key in value){//遍历json对象的每个key/value对,p为key
        value[key]=this.state.fileList[0].response.result;
      }
    }

    this.add('appConfig/update',value, this.addCallBack);

  }

  /**
   * 增加、修改回调函数
   * @param data
   */
  addCallBack = data => {
  }
  /**
   * 更多操作 批量删除 批量通过 ... 事件
   * @param e
   */
  removeCallBack = () =>{
    this.setState({
      selectedRows: [],
    });
    this.list("appConfig/fetch");
  }
  handleMenuClick = (e) => {
    const {selectedRows} = this.state;
    const params={
      ids: selectedRows.map(row => row.id).join(','),
    }
    if (!selectedRows) return;
    switch (e.key) {
      case 'remove':
        this.remove("appConfig/remove",params,this.removeCallBack);
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
      this.list("appConfig/fetch",params);
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
              <FormItem label="配置key">
                  {getFieldDecorator('confKey_LK')(
                  <Input placeholder="请输入配置key"/>
                  )}
              </FormItem>
              </Col>

              <Col md={8} sm={24}>
              <FormItem label="配置名称">
                  {getFieldDecorator('confName_LK')(
                  <Input placeholder="请输入配置名称"/>
                  )}
              </FormItem>
              </Col>
                         <Col md={8} sm={24}>

              <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">查询</Button>
              <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("appConfig/fetch")}>重置</Button>
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
              <FormItem label="配置key">
                  {getFieldDecorator('confKey_LK')(
                  <Input placeholder="请输入配置key"/>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="配置名称">
                  {getFieldDecorator('confName_LK')(
                  <Input placeholder="请输入配置名称"/>
                  )}
              </FormItem>
              </Col>
              <Col md={8} sm={24}>
              <FormItem label="类型名称">
                  {getFieldDecorator('typeName_LK')(
                  <Input placeholder="请输入类型名称"/>
                  )}
              </FormItem>
              </Col>

          </Row>
          <div style={{overflow: 'hidden'}}>
          <span style={{float: 'right', marginBottom: 24}}>
          <Button type="primary" htmlType="submit">查询</Button>
          <Button style={{marginLeft: 8}} onClick={() => this.handleFormReset("appConfig/fetch")}>重置</Button>
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
    const {appConfig: {list}, loading} = this.props;
    const {selectedRows, modalVisible,configData,handleFileChange} = this.state;

    const parentMethods = {
      handleFileChange:this.handleFileChange,
      handleAdd: this.handleSetting,
      handleModalVisible: this.handleModalVisible,
    };

    const menu = (
      <Menu onClick={this.handleMenuClick} selectedKeys={[]}>
          <Menu.Item key="remove" className={this.hasPermissions("system:config:appConfig:remove")?"":styles.hidden}>批量删除</Menu.Item>
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

                      <Link className={this.hasPermissions("system:config:appConfig:add")?"":styles.hidden} to="/system/config/appConfig/add">
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
                          expandedRowRender={record => <p style={{ margin: 0 }}>{record.confValue}</p>}
                          onSelectRow={this.handleSelectRows}
                          onChange={this.handleStandardTableChange}
                  />
              </div>
          </Card>
        <CreateForm
          {...parentMethods}
          modalVisible={modalVisible}
          configData={configData}
        />
      </PageHeaderLayout>
    );
  }
}
