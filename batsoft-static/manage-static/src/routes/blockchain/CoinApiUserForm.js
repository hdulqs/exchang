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
            userName: '用户名',
            userPassword: '用户密码',
            userKey: '用户秘钥',
            userIp: '用户ip',
            userPhone: '联系方式',
            notifyUrl: '通知地址',
            status: '状态',
};

@connect(({coinApiUser}) => ({
coinApiUser,
}))
class CoinApiUserForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('coinApiUser/find',{id: match.params.id});
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
    const {coinApiUser: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          this.add('coinApiUser/add',params,this.addCallBack);
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
                                <FormItem {...formItemLayout} label={fieldLabels.userName}>
                                    {getFieldDecorator('userName', {
                                        initialValue:`${findData.data.userName===undefined?'':findData.data.userName}`,
                                    rules: [{
                                        required: true,
                                        max:32,
                                    message: '请正确填写用户名',
                                    }],
                                    })(
                                    <Input placeholder="请输入用户名"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.userPassword}>
                                    {getFieldDecorator('userPassword', {
                                        initialValue:`${findData.data.userPassword===undefined?'':findData.data.userPassword}`,
                                    rules: [{
                                        max:128,
                                    message: '请正确填写用户密码',
                                    }],
                                    })(
                                    <Input placeholder="请输入用户密码"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.userKey}>
                                    {getFieldDecorator('userKey', {
                                        initialValue:`${findData.data.userKey===undefined?'':findData.data.userKey}`,
                                    rules: [{
                                        required: true,
                                        max:64,
                                    message: '请正确填写用户秘钥',
                                    }],
                                    })(
                                    <Input placeholder="请输入用户秘钥"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.userIp}>
                                    {getFieldDecorator('userIp', {
                                        initialValue:`${findData.data.userIp===undefined?'':findData.data.userIp}`,
                                    rules: [{
                                        max:20,
                                    message: '请正确填写用户ip',
                                    }],
                                    })(
                                    <Input placeholder="请输入用户ip"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.userPhone}>
                                    {getFieldDecorator('userPhone', {
                                        initialValue:`${findData.data.userPhone===undefined?'':findData.data.userPhone}`,
                                    rules: [{
                                        max:15,
                                    message: '请正确填写联系方式',
                                    }],
                                    })(
                                    <Input placeholder="请输入联系方式"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.notifyUrl}>
                                    {getFieldDecorator('notifyUrl', {
                                        initialValue:`${findData.data.notifyUrl===undefined?'':findData.data.notifyUrl}`,
                                    rules: [{
                                        required: true,
                                        max:255,
                                    message: '请正确填写通知地址',
                                    }],
                                    })(
                                    <Input placeholder="请输入通知地址"/>
                                    )}
                                </FormItem>


                                 <FormItem {...formItemLayout} label={fieldLabels.status}>
                                     {getFieldDecorator('status', {
                                       initialValue:`${findData.data.status===undefined?'1':findData.data.status}`,
                                     rules: [{
                                        required: true,
                                        max:5,
                                        message: '请正确填写状态',
                                     }],
                                     })(
                                     <Select placeholder="请选择状态">
                                              <Option value="0" >禁用</Option>
                                              <Option value="1" >正常</Option>
                                     </Select>
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
                submitting: loading.effects['coinApiUser/add'],
                }))(Form.create()(CoinApiUserForm));
