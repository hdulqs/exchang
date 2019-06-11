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
            accountId: '账户id',
            customerId: '客户id',
            accountNumber: '账户账号',
            coinCode: '货币代码',
            orderId: '订单id',
            money: '流水金额',
            remark: '订单说明',
};

@connect(({customerAccountRecord}) => ({
customerAccountRecord,
}))
class CustomerAccountRecordForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('customerAccountRecord/find',{id: match.params.id});
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
    const {customerAccountRecord: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          this.add('customerAccountRecord/add',params,this.addCallBack);
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
                                <FormItem {...formItemLayout} label={fieldLabels.accountId}>
                                    {getFieldDecorator('accountId', {
                                       initialValue:`${findData.data.accountId===undefined?'':findData.data.accountId}`,
                                    rules: [{
                                        max:50,
                                    message: '请正确填写账户id',
                                    }],
                                    })(
                                    <Input placeholder="请输入账户id"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.customerId}>
                                    {getFieldDecorator('customerId', {
                                       initialValue:`${findData.data.customerId===undefined?'':findData.data.customerId}`,
                                    rules: [{
                                        max:50,
                                    message: '请正确填写客户id',
                                    }],
                                    })(
                                    <Input placeholder="请输入客户id"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.accountNumber}>
                                    {getFieldDecorator('accountNumber', {
                                       initialValue:`${findData.data.accountNumber===undefined?'':findData.data.accountNumber}`,
                                    rules: [{
                                        max:50,
                                    message: '请正确填写账户账号',
                                    }],
                                    })(
                                    <Input placeholder="请输入账户账号"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.coinCode}>
                                    {getFieldDecorator('coinCode', {
                                       initialValue:`${findData.data.coinCode===undefined?'':findData.data.coinCode}`,
                                    rules: [{
                                        max:50,
                                    message: '请正确填写货币代码',
                                    }],
                                    })(
                                    <Input placeholder="请输入货币代码"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.orderId}>
                                    {getFieldDecorator('orderId', {
                                       initialValue:`${findData.data.orderId===undefined?'':findData.data.orderId}`,
                                    rules: [{
                                        max:10,
                                    message: '请正确填写订单id',
                                    }],
                                    })(
                                    <Input placeholder="请输入订单id"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.money}>
                                    {getFieldDecorator('money', {
                                       initialValue:`${findData.data.money===undefined?'0.0000000000':findData.data.money}`,
                                    rules: [{
                                        max:25,
                                    message: '请正确填写流水金额',
                                    }],
                                    })(
                                    <Input placeholder="请输入流水金额"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.remark}>
                                    {getFieldDecorator('remark', {
                                       initialValue:`${findData.data.remark===undefined?'':findData.data.remark}`,
                                    rules: [{
                                        max:50,
                                    message: '请正确填写订单说明',
                                    }],
                                    })(
                                    <TextArea style={{ minHeight: 32 }} placeholder="订单说明" rows={4} />
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
                submitting: loading.effects['customerAccountRecord/add'],
                }))(Form.create()(CustomerAccountRecordForm));
