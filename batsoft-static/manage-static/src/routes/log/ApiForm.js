import React from 'react';
import {Card, Button, Form, Icon, Col, Row, Input, Select, Popover,DatePicker,Radio} from 'antd';
import {connect} from 'dva';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import FooterToolbar from '../../components/FooterToolbar';
import styles from '../../assets/style/style.less';
import {Link} from 'dva/router';
import BaseForm from '../../components/BatSoft/Form';

const {Option} = Select;
const FormItem = Form.Item;
const fieldLabels = {
            url: '请求地址',
            ip: 'ip地址',
            method: '请求方法',
            mediaType: '请求类型（application/json等等）',
            requestMethod: '请求方法（get/post）',
            callbackParams: '回调参数',
            callbackNum: '回调次数',
            requestFrom: '来源(app/pc)',
            executeTime: '执行时间MS',
            status: '状态',
            params: '请求参数',
            remark: '备注',
};

@connect(({api}) => ({
api,
}))
class ApiForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('api/find',{id: match.params.id});
    }
  }

  /**
   * 增加、修改回调函数
   * @param data
   */
  addCallBack = data =>{
    const { form} = this.props;
     if(this.state.formType==="add"){
      form.resetFields();
    }
  }
  render() {
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
    const {api: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          this.add('api/add',params,this.addCallBack);
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
                    <Form  hideRequiredMark>
                                  <Form.Item {...formItemLayout} label={fieldLabels.id}>
                                      {getFieldDecorator('id', {
                                      initialValue: findData.data.id,
                                      })(
                                      <Input style={{display: 'none'}}/>
                                      )}
                                  </Form.Item>
                                <FormItem {...formItemLayout} label={fieldLabels.url}>
                                    {getFieldDecorator('url', {
                                        initialValue:`${findData.data.url===undefined?'':findData.data.url}`,
                                    rules: [{
                                        max:255,
                                    message: '请正确填写请求地址',
                                    }],
                                    })(
                                    <Input placeholder="请输入请求地址"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.ip}>
                                    {getFieldDecorator('ip', {
                                        initialValue:`${findData.data.ip===undefined?'':findData.data.ip}`,
                                    rules: [{
                                        max:50,
                                    message: '请正确填写ip地址',
                                    }],
                                    })(
                                    <Input placeholder="请输入ip地址"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.method}>
                                    {getFieldDecorator('method', {
                                        initialValue:`${findData.data.method===undefined?'':findData.data.method}`,
                                    rules: [{
                                        max:255,
                                    message: '请正确填写请求方法',
                                    }],
                                    })(
                                    <Input placeholder="请输入请求方法"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.mediaType}>
                                    {getFieldDecorator('mediaType', {
                                        initialValue:`${findData.data.mediaType===undefined?'':findData.data.mediaType}`,
                                    rules: [{
                                        max:255,
                                    message: '请正确填写请求类型（application/json等等）',
                                    }],
                                    })(
                                    <Input placeholder="请输入请求类型（application/json等等）"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.requestMethod}>
                                    {getFieldDecorator('requestMethod', {
                                        initialValue:`${findData.data.requestMethod===undefined?'':findData.data.requestMethod}`,
                                    rules: [{
                                        max:10,
                                    message: '请正确填写请求方法（get/post）',
                                    }],
                                    })(
                                    <Input placeholder="请输入请求方法（get/post）"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.callbackParams}>
                                    {getFieldDecorator('callbackParams', {
                                        initialValue:`${findData.data.callbackParams===undefined?'':findData.data.callbackParams}`,
                                    rules: [{
                                    message: '请正确填写回调参数',
                                    }],
                                    })(
                                    <Input placeholder="请输入回调参数"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.callbackNum}>
                                    {getFieldDecorator('callbackNum', {
                                       initialValue:`${findData.data.callbackNum===undefined?'0':findData.data.callbackNum}`,
                                    rules: [{
                                        max:100,
                                    message: '请正确填写回调次数',
                                    }],
                                    })(
                                    <Input placeholder="请输入回调次数"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.requestFrom}>
                                    {getFieldDecorator('requestFrom', {
                                        initialValue:`${findData.data.requestFrom===undefined?'':findData.data.requestFrom}`,
                                    rules: [{
                                        max:50,
                                    message: '请正确填写来源(app/pc)',
                                    }],
                                    })(
                                    <Input placeholder="请输入来源(app/pc)"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.executeTime}>
                                    {getFieldDecorator('executeTime', {
                                        initialValue:`${findData.data.executeTime===undefined?'':findData.data.executeTime}`,
                                    rules: [{
                                        max:20,
                                    message: '请正确填写执行时间MS',
                                    }],
                                    })(
                                    <Input placeholder="请输入执行时间MS"/>
                                    )}
                                </FormItem>


                                 <FormItem {...formItemLayout} label={fieldLabels.status}>
                                     {getFieldDecorator('status', {
                                        initialValue:`${findData.data.status===undefined?'':findData.data.status}`,
                                     rules: [{
                                        max:1,
                                        message: '请正确填写状态',
                                     }],
                                     })(
                                     <Select placeholder="请选择状态">
                                              <Option value="0" >接收成功</Option>
                                              <Option value="1" >回调成功</Option>
                                     </Select>
                                     )}
                                 </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.params}>
                                    {getFieldDecorator('params', {
                                        initialValue:`${findData.data.params===undefined?'':findData.data.params}`,
                                    rules: [{
                                    message: '请正确填写请求参数',
                                    }],
                                    })(
                                    <Input placeholder="请输入请求参数"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.remark}>
                                    {getFieldDecorator('remark', {
                                        initialValue:`${findData.data.remark===undefined?'':findData.data.remark}`,
                                    rules: [{
                                    message: '请正确填写备注',
                                    }],
                                    })(
                                    <Input placeholder="请输入备注"/>
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
                submitting: loading.effects['api/add'],
                }))(Form.create()(ApiForm));
