import React from 'react';
import {Card, Button, Form, Icon, Col, Row, Input, Select, Popover, DatePicker, Radio} from 'antd';
import {connect} from 'dva';
import PageHeaderLayout from '../../../layouts/PageHeaderLayout';
import FooterToolbar from '../../../components/FooterToolbar';
import styles from '../../../assets/style/style.less';
import {Link} from 'dva/router';
import BaseForm from '../../../components/BatSoft/Form';

const {Option} = Select;
const FormItem = Form.Item;
const {TextArea} = Input;
const fieldLabels = {
  confKey: '配置key',
  confName: '配置名称',
  dataType: '数据类型',
  confValue: '配置值',
  selectValue: '选项值',
  typeKey: '配置类型',
  typeName: '类型名称',
  description: '描述',
  sort: '排序',
  remark: '备注',
};


@connect(({appConfig,appConfigType}) => ({
  appConfig,
  appConfigType,
}))
class AppConfigForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    this.find('appConfigType/findTypes');
    if (match.params.id !== undefined) {
      this.setState({
        formType: "update",
      })
      this.find('appConfig/find', {id: match.params.id});
    }
  }

  /**
   * 增加、修改回调函数
   * @param data
   */
  addCallBack = data => {
    const {form} = this.props;
    form.resetFields();
  }

  render() {
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
    const {appConfig: {findData},appConfigType: {findData:typeData},form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    const children = [];
    var arr = Object.keys(typeData.data);
    if(arr.length >0) {
      typeData.data.forEach((item) => {
        children.push(<Option key={item.typeKey}>{item.typeName}</Option>);
      });
    }
    if (this.state.formType == "add") {
      findData.data = {};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          this.add('appConfig/add', params, this.addCallBack);
        }
      });
    };
    const errors = getFieldsError();
    /**
     * 错误信息合集
     * @returns {*}
     */
    const getErrorInfo = () => {
      const errorCount = Object.keys(errors).filter(key => errors[key]).length;
      if (!errors || errorCount === 0) {
        return null;
      }
      const scrollToField = (fieldKey) => {
        const labelNode = document.querySelector(`label[for="${fieldKey}"]`);
        if (labelNode) {
          labelNode.scrollIntoView(true);
        }
      };
      const errorList = Object.keys(errors).map((key) => {
        if (!errors[key]) {
          return null;
        }
        return (
          <li key={key} className={styles.errorListItem} onClick={() => scrollToField(key)}>
            <Icon type="cross-circle-o" className={styles.errorIcon}/>
            <div className={styles.errorMessage}>{errors[key][0]}</div>
            <div className={styles.errorField}>{fieldLabels[key]}</div>
          </li>
        );
      });
      return (
        <span className={styles.errorIcon}>
          <Popover
            title="表单校验信息"
            content={errorList}
            overlayClassName={styles.errorPopover}
            trigger="click"
            getPopupContainer={trigger => trigger.parentNode}
          >
            <Icon type="exclamation-circle"/>
                    </Popover>
          {errorCount}
        </span>
      );
    };
    return (
      <PageHeaderLayout
        wrapperClassName={styles.advancedForm}
      >
        <Card className={styles.card} bordered={false}>
          <Form hideRequiredMark>
            <Form.Item {...formItemLayout} label={fieldLabels.id}>
              {getFieldDecorator('id', {
                initialValue: findData.data.id,
              })(
                <Input style={{display: 'none'}}/>
              )}
            </Form.Item>
            <FormItem {...formItemLayout} label={fieldLabels.typeKey}>
              {getFieldDecorator('typeKey', {
                initialValue: `${findData.data.typeKey === undefined ?'' : findData.data.typeKey}`,
                rules: [{
                  required: true,
                  message: '请正确填写配置类型',
                }],
              })(
                <Select placeholder="请选择配置类型">
                  {children}
                </Select>
              )}
            </FormItem>
            <FormItem {...formItemLayout} label={fieldLabels.confKey}>
              {getFieldDecorator('confKey', {
                initialValue: `${findData.data.confKey=== undefined ? '' : findData.data.confKey}`,
                rules: [{
                  max: 255,
                  message: '请正确填写配置key',
                }],
              })(
                <Input placeholder="请输入配置key"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.confName}>
              {getFieldDecorator('confName', {
                initialValue: `${findData.data.confName=== undefined ? '' : findData.data.confName}`,
                rules: [{
                  max: 100,
                  message: '请正确填写配置名称',
                }],
              })(
                <Input placeholder="请输入配置名称"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.dataType}>
              {getFieldDecorator('dataType', {
                initialValue: `${findData.data.dataType === undefined ? '0' : findData.data.dataType}`,
                rules: [{
                  max: 2,
                  message: '请正确填写数据类型',
                }],
              })(
                <Select placeholder="请选择数据类型">
                  <Option value="0">文本</Option>
                  <Option value="1">文本域</Option>
                  <Option value="2">单选</Option>
                  <Option value="3">文件</Option>
                  <Option value="4">复选</Option>
                </Select>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.confValue}>
              {getFieldDecorator('confValue', {
                initialValue: `${findData.data.confValue=== undefined ? '' : findData.data.confValue}`,
                rules: [{
                  max: 255,
                  message: '请正确填写配置值',
                }],
              })(
                <Input placeholder="请输入配置值"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.selectValue}>
              {getFieldDecorator('selectValue', {
                initialValue: `${findData.data.selectValue=== undefined ? '' : findData.data.selectValue}`,
                rules: [{
                  max: 255,
                  message: '请正确填写选项值',
                }],
              })(
                <Input placeholder="请输入选项值"/>
              )}
            </FormItem>



            <FormItem {...formItemLayout} label={fieldLabels.description}>
              {getFieldDecorator('description', {
                initialValue: `${findData.data.description=== undefined ? '' : findData.data.description}`,
                rules: [{
                  max: 255,
                  message: '请正确填写描述',
                }],
              })(
                <Input placeholder="请输入描述"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.sort}>
              {getFieldDecorator('sort', {
                initialValue: `${findData.data.sort=== undefined ? '0' : findData.data.sort}` ,
                rules: [{
                  required: true,
                  message: '请正确填写排序',
                }],
              })(
                <Input placeholder="请输入排序"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.remark}>
              {getFieldDecorator('remark', {
                initialValue: `${findData.data.remark=== undefined ? '' : findData.data.remark}`,
                rules: [{
                  max: 255,
                  message: '请正确填写备注',
                }],
              })(
                <TextArea style={{minHeight: 32}} placeholder="备注" rows={4}/>
              )}
            </FormItem>


          </Form>
        </Card>

        <FooterToolbar style={{width: this.state.width}}>
          {getErrorInfo()}
          <Button type="primary" onClick={validate} loading={submitting}>
            提交
          </Button>
        </FooterToolbar>
      </PageHeaderLayout>
    );
  }
}

export default connect(({global, loading}) => ({
  collapsed: global.collapsed,
  submitting: loading.effects['appConfig/add'],
}))(Form.create()(AppConfigForm));
