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
            userId: '用户ID',
            userName: '用户名',
            bankId: '银行ID',
            bankCard: '银行卡号',
            bankName: '银行名称',
            branchName: '支行名称',
            province: '开户省',
            city: '开户市',
            status: '是否有效',
};

@connect(({bankcard}) => ({
bankcard,
}))
class BankcardForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('bankcard/find',{id: match.params.id});
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
    const {bankcard: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          this.add('bankcard/add',params,this.addCallBack);
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
                                <FormItem {...formItemLayout} label={fieldLabels.userId}>
                                    {getFieldDecorator('userId', {
                                        initialValue:`${findData.data.userId===undefined?'':findData.data.userId}`,
                                    rules: [{
                                        required: true,
                                        max:64,
                                    message: '请正确填写用户ID',
                                    }],
                                    })(
                                    <Input placeholder="请输入用户ID"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.userName}>
                                    {getFieldDecorator('userName', {
                                        initialValue:`${findData.data.userName===undefined?'':findData.data.userName}`,
                                    rules: [{
                                        required: true,
                                        max:50,
                                    message: '请正确填写用户名',
                                    }],
                                    })(
                                    <Input placeholder="请输入用户名"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.bankId}>
                                    {getFieldDecorator('bankId', {
                                        initialValue:`${findData.data.bankId===undefined?'':findData.data.bankId}`,
                                    rules: [{
                                        max:64,
                                    message: '请正确填写银行ID',
                                    }],
                                    })(
                                    <Input placeholder="请输入银行ID"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.bankCard}>
                                    {getFieldDecorator('bankCard', {
                                        initialValue:`${findData.data.bankCard===undefined?'':findData.data.bankCard}`,
                                    rules: [{
                                        required: true,
                                        max:50,
                                    message: '请正确填写银行卡号',
                                    }],
                                    })(
                                    <Input placeholder="请输入银行卡号"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.bankName}>
                                    {getFieldDecorator('bankName', {
                                        initialValue:`${findData.data.bankName===undefined?'':findData.data.bankName}`,
                                    rules: [{
                                        required: true,
                                        max:100,
                                    message: '请正确填写银行名称',
                                    }],
                                    })(
                                    <Input placeholder="请输入银行名称"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.branchName}>
                                    {getFieldDecorator('branchName', {
                                        initialValue:`${findData.data.branchName===undefined?'':findData.data.branchName}`,
                                    rules: [{
                                        max:100,
                                    message: '请正确填写支行名称',
                                    }],
                                    })(
                                    <Input placeholder="请输入支行名称"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.province}>
                                    {getFieldDecorator('province', {
                                        initialValue:`${findData.data.province===undefined?'':findData.data.province}`,
                                    rules: [{
                                        max:30,
                                    message: '请正确填写开户省',
                                    }],
                                    })(
                                    <Input placeholder="请输入开户省"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.city}>
                                    {getFieldDecorator('city', {
                                        initialValue:`${findData.data.city===undefined?'':findData.data.city}`,
                                    rules: [{
                                        max:30,
                                    message: '请正确填写开户市',
                                    }],
                                    })(
                                    <Input placeholder="请输入开户市"/>
                                    )}
                                </FormItem>


                                 <FormItem  {...formItemLayout} label={fieldLabels.status}>
                                     {getFieldDecorator('status', {
                                        initialValue:`${findData.data.status===undefined?'':findData.data.status}`,
                                     rules: [{
                                        max:5,
                                     message: '请正确填写是否有效',
                                     }],
                                     })(
                                     <Radio.Group>
                                              <Radio value="0" >无效</Radio>
                                              <Radio value="1" >有效</Radio>
                                     </Radio.Group>
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
                submitting: loading.effects['bankcard/add'],
                }))(Form.create()(BankcardForm));
