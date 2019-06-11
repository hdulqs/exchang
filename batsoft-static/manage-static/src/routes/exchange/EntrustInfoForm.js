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
const fieldLabels = {
            coinCode: '成交货币',
            entrustOrder: '委托单号',
            entrustPrice: '成交价格',
            entrustAmout: '成交数量',
            entrustTime: '成交时间',
            sellAccountId: '卖方账户',
            buyAccountId: '买方账户',
            sellCustomerId: '卖方客户',
            buyCustomerId: '买方客户',
            remark: '备注',
};

@connect(({entrustInfo}) => ({
entrustInfo,
}))
class EntrustInfoForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('entrustInfo/find',{id: match.params.id});
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
    const {entrustInfo: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
            params.entrustTime  =params.entrustTime .format(dateFormat);
          this.add('entrustInfo/add',params,this.addCallBack);
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
                                <FormItem {...formItemLayout} label={fieldLabels.coinCode}>
                                    {getFieldDecorator('coinCode', {
                                       initialValue:`${findData.data.coinCode===undefined?'':findData.data.coinCode}`,
                                    rules: [{
                                        max:50,
                                    message: '请正确填写成交货币',
                                    }],
                                    })(
                                    <Input placeholder="请输入成交货币"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.entrustOrder}>
                                    {getFieldDecorator('entrustOrder', {
                                        initialValue:`${findData.data.entrustOrder===undefined?'':findData.data.entrustOrder}`,
                                    rules: [{
                                        required: true,
                                        max:255,
                                    message: '请正确填写委托单号',
                                    }],
                                    })(
                                    <Input placeholder="请输入委托单号"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.entrustPrice}>
                                    {getFieldDecorator('entrustPrice', {
                                       initialValue:`${findData.data.entrustPrice===undefined?'0.0000000000':findData.data.entrustPrice}`,
                                    rules: [{
                                        max:25,
                                    message: '请正确填写成交价格',
                                    }],
                                    })(
                                    <Input placeholder="请输入成交价格"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.entrustAmout}>
                                    {getFieldDecorator('entrustAmout', {
                                       initialValue:`${findData.data.entrustAmout===undefined?'0.0000000000':findData.data.entrustAmout}`,
                                    rules: [{
                                        max:25,
                                    message: '请正确填写成交数量',
                                    }],
                                    })(
                                    <Input placeholder="请输入成交数量"/>
                                    )}
                                </FormItem>


                                 <FormItem {...formItemLayout} label={fieldLabels.entrustTime}>
                                     {getFieldDecorator('entrustTime', {
                                     initialValue:findData.data.entrustTime==null?'': moment(findData.data.entrustTime,dateFormat) ,
                                     rules: [{
                                     message: '请正确填写成交时间',
                                     }],
                                     })(
                                     <DatePicker format={dateFormat} placeholder="请输入成交时间"/>
                                     )}
                                 </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.sellAccountId}>
                                    {getFieldDecorator('sellAccountId', {
                                       initialValue:`${findData.data.sellAccountId===undefined?'':findData.data.sellAccountId}`,
                                    rules: [{
                                        max:50,
                                    message: '请正确填写卖方账户',
                                    }],
                                    })(
                                    <Input placeholder="请输入卖方账户"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.buyAccountId}>
                                    {getFieldDecorator('buyAccountId', {
                                       initialValue:`${findData.data.buyAccountId===undefined?'':findData.data.buyAccountId}`,
                                    rules: [{
                                        max:50,
                                    message: '请正确填写买方账户',
                                    }],
                                    })(
                                    <Input placeholder="请输入买方账户"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.sellCustomerId}>
                                    {getFieldDecorator('sellCustomerId', {
                                       initialValue:`${findData.data.sellCustomerId===undefined?'':findData.data.sellCustomerId}`,
                                    rules: [{
                                        max:50,
                                    message: '请正确填写卖方客户',
                                    }],
                                    })(
                                    <Input placeholder="请输入卖方客户"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.buyCustomerId}>
                                    {getFieldDecorator('buyCustomerId', {
                                       initialValue:`${findData.data.buyCustomerId===undefined?'':findData.data.buyCustomerId}`,
                                    rules: [{
                                        max:50,
                                    message: '请正确填写买方客户',
                                    }],
                                    })(
                                    <Input placeholder="请输入买方客户"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.remark}>
                                    {getFieldDecorator('remark', {
                                       initialValue:`${findData.data.remark===undefined?'':findData.data.remark}`,
                                    rules: [{
                                        max:50,
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
                submitting: loading.effects['entrustInfo/add'],
                }))(Form.create()(EntrustInfoForm));
