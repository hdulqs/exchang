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
const { TextArea } = Input;
const fieldLabels = {
            coinAddress: '提币地址',
            memo: 'memo地址',
            coinCode: '币种代码',
            userId: '用户id',
            userName: '用户名',
            userMobile: '用户手机号',
            userEmail: '用户邮箱',
            remark: '备注',
};

@connect(({withdrawAddress}) => ({
withdrawAddress,
}))
class WithdrawAddressForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('withdrawAddress/find',{id: match.params.id});
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
    const {withdrawAddress: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          this.add('withdrawAddress/add',params,this.addCallBack);
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
                                <FormItem {...formItemLayout} label={fieldLabels.coinAddress}>
                                    {getFieldDecorator('coinAddress', {
                                        initialValue:`${findData.data.coinAddress===undefined?'':findData.data.coinAddress}`,
                                    rules: [{
                                        required: true,
                                        max:255,
                                    message: '请正确填写提币地址',
                                    }],
                                    })(
                                    <Input placeholder="请输入提币地址"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.memo}>
                                    {getFieldDecorator('memo', {
                                        initialValue:`${findData.data.memo===undefined?'':findData.data.memo}`,
                                    rules: [{
                                        max:255,
                                    message: '请正确填写memo地址',
                                    }],
                                    })(
                                    <Input placeholder="请输入memo地址"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.coinCode}>
                                    {getFieldDecorator('coinCode', {
                                        initialValue:`${findData.data.coinCode===undefined?'':findData.data.coinCode}`,
                                    rules: [{
                                        required: true,
                                        max:50,
                                    message: '请正确填写币种代码',
                                    }],
                                    })(
                                    <Input placeholder="请输入币种代码"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.userId}>
                                    {getFieldDecorator('userId', {
                                        initialValue:`${findData.data.userId===undefined?'':findData.data.userId}`,
                                    rules: [{
                                        required: true,
                                        max:64,
                                    message: '请正确填写用户id',
                                    }],
                                    })(
                                    <Input placeholder="请输入用户id"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.userName}>
                                    {getFieldDecorator('userName', {
                                        initialValue:`${findData.data.userName===undefined?'':findData.data.userName}`,
                                    rules: [{
                                        max:50,
                                    message: '请正确填写用户名',
                                    }],
                                    })(
                                    <Input placeholder="请输入用户名"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.userMobile}>
                                    {getFieldDecorator('userMobile', {
                                        initialValue:`${findData.data.userMobile===undefined?'':findData.data.userMobile}`,
                                    rules: [{
                                        max:20,
                                    message: '请正确填写用户手机号',
                                    }],
                                    })(
                                    <Input placeholder="请输入用户手机号"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.userEmail}>
                                    {getFieldDecorator('userEmail', {
                                        initialValue:`${findData.data.userEmail===undefined?'':findData.data.userEmail}`,
                                    rules: [{
                                        max:255,
                                    message: '请正确填写用户邮箱',
                                    }],
                                    })(
                                    <Input placeholder="请输入用户邮箱"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.remark}>
                                    {getFieldDecorator('remark', {
                                        initialValue:`${findData.data.remark===undefined?'':findData.data.remark}`,
                                    rules: [{
                                        max:255,
                                    message: '请正确填写备注',
                                    }],
                                    })(
                                    <TextArea style={{ minHeight: 32 }} placeholder="备注" rows={4} />
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
                submitting: loading.effects['withdrawAddress/add'],
                }))(Form.create()(WithdrawAddressForm));
