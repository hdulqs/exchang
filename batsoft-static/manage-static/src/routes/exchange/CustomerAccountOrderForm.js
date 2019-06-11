import React from 'react';
import {Card, Button, Form, Icon, Col, Row, Input, Select, Popover,DatePicker,Radio} from 'antd';
import {connect} from 'dva';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import FooterToolbar from '../../components/FooterToolbar';
import styles from '../../assets/style/style.less';
import {Link} from 'dva/router';
import moment from 'moment';
import BaseForm from '../../components/BatSoft/Form';

const {Option} = Select;
const FormItem = Form.Item;
const dateFormat = 'YYYY-MM-DD HH:mm:ss';
const { TextArea } = Input;
const fieldLabels = {
            accountId: '货币账户id',
            customerId: '客户id',
            accountNumber: '账户账号',
            coinCode: '币代码',
            direction: '方向',
            operationType: '操作类型',
            opeationMoney: '订单金额',
            tradeTime: '交易时间',
            opeationState: '订单状态',
            remark: '订单说明',
};

@connect(({customerAccountOrder}) => ({
customerAccountOrder,
}))
class CustomerAccountOrderForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('customerAccountOrder/find',{id: match.params.id});
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
    const {customerAccountOrder: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
            params.tradeTime  =params.tradeTime .format(dateFormat);
          this.add('customerAccountOrder/add',params,this.addCallBack);
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
                                    message: '请正确填写货币账户id',
                                    }],
                                    })(
                                    <Input placeholder="请输入货币账户id"/>
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
                                    message: '请正确填写币代码',
                                    }],
                                    })(
                                    <Input placeholder="请输入币代码"/>
                                    )}
                                </FormItem>


                                 <FormItem {...formItemLayout} label={fieldLabels.direction}>
                                     {getFieldDecorator('direction', {
                                        initialValue:`${findData.data.direction===undefined?'':findData.data.direction}`,
                                     rules: [{
                                        max:1,
                                        message: '请正确填写方向',
                                     }],
                                     })(
                                     <Select placeholder="请选择方向">
                                              <Option value="0" >支出</Option>
                                              <Option value="1" >收入</Option>
                                     </Select>
                                     )}
                                 </FormItem>


                                 <FormItem {...formItemLayout} label={fieldLabels.operationType}>
                                     {getFieldDecorator('operationType', {
                                       initialValue:`${findData.data.operationType===undefined?'':findData.data.operationType}`,
                                     rules: [{
                                        max:10,
                                        message: '请正确填写操作类型',
                                     }],
                                     })(
                                     <Select placeholder="请选择操作类型">
                                              <Option value="0" >充值</Option>
                                              <Option value="1" >提现</Option>
                                              <Option value="2" >转入</Option>
                                              <Option value="3" >转出</Option>
                                              <Option value="4" >系统</Option>
                                     </Select>
                                     )}
                                 </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.opeationMoney}>
                                    {getFieldDecorator('opeationMoney', {
                                       initialValue:`${findData.data.opeationMoney===undefined?'0.0000000000':findData.data.opeationMoney}`,
                                    rules: [{
                                        max:25,
                                    message: '请正确填写订单金额',
                                    }],
                                    })(
                                    <Input placeholder="请输入订单金额"/>
                                    )}
                                </FormItem>


                                 <FormItem {...formItemLayout} label={fieldLabels.tradeTime}>
                                     {getFieldDecorator('tradeTime', {
                                     initialValue:findData.data.tradeTime==null?'': moment(findData.data.tradeTime,dateFormat) ,
                                     rules: [{
                                     message: '请正确填写交易时间',
                                     }],
                                     })(
                                     <DatePicker format={dateFormat} placeholder="请输入交易时间"/>
                                     )}
                                 </FormItem>


                                 <FormItem {...formItemLayout} label={fieldLabels.opeationState}>
                                     {getFieldDecorator('opeationState', {
                                       initialValue:`${findData.data.opeationState===undefined?'0':findData.data.opeationState}`,
                                     rules: [{
                                        max:1,
                                        message: '请正确填写订单状态',
                                     }],
                                     })(
                                     <Select placeholder="请选择订单状态">
                                              <Option value="0" >进行中</Option>
                                              <Option value="1" >已完成</Option>
                                              <Option value="2" >失败</Option>
                                     </Select>
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
                submitting: loading.effects['customerAccountOrder/add'],
                }))(Form.create()(CustomerAccountOrderForm));
