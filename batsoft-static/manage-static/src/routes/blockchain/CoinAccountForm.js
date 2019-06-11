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
            walletId: '钱包',
            walletCode: '钱包code',
            coinCode: '币代码',
            coinAddress: '币地址',
            addressPassword: '解锁密码',
            memo: 'memo',
            balance: '币数量',
            status: '状态',
};

@connect(({coinAccount}) => ({
coinAccount,
}))
class CoinAccountForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('coinAccount/find',{id: match.params.id});
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
    const {coinAccount: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          this.add('coinAccount/add',params,this.addCallBack);
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
                                <FormItem {...formItemLayout} label={fieldLabels.walletId}>
                                    {getFieldDecorator('walletId', {
                                        initialValue:`${findData.data.walletId===undefined?'':findData.data.walletId}`,
                                    rules: [{
                                        required: true,
                                        max:32,
                                    message: '请正确填写钱包',
                                    }],
                                    })(
                                    <Input placeholder="请输入钱包"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.walletCode}>
                                    {getFieldDecorator('walletCode', {
                                        initialValue:`${findData.data.walletCode===undefined?'':findData.data.walletCode}`,
                                    rules: [{
                                        required: true,
                                        max:10,
                                    message: '请正确填写钱包code',
                                    }],
                                    })(
                                    <Input placeholder="请输入钱包code"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.coinCode}>
                                    {getFieldDecorator('coinCode', {
                                        initialValue:`${findData.data.coinCode===undefined?'':findData.data.coinCode}`,
                                    rules: [{
                                        required: true,
                                        max:10,
                                    message: '请正确填写币代码',
                                    }],
                                    })(
                                    <Input placeholder="请输入币代码"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.coinAddress}>
                                    {getFieldDecorator('coinAddress', {
                                        initialValue:`${findData.data.coinAddress===undefined?'':findData.data.coinAddress}`,
                                    rules: [{
                                        required: true,
                                        max:255,
                                    message: '请正确填写币地址',
                                    }],
                                    })(
                                    <Input placeholder="请输入币地址"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.addressPassword}>
                                    {getFieldDecorator('addressPassword', {
                                        initialValue:`${findData.data.addressPassword===undefined?'':findData.data.addressPassword}`,
                                    rules: [{
                                        max:255,
                                    message: '请正确填写解锁密码',
                                    }],
                                    })(
                                    <Input placeholder="请输入解锁密码"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.memo}>
                                    {getFieldDecorator('memo', {
                                        initialValue:`${findData.data.memo===undefined?'':findData.data.memo}`,
                                    rules: [{
                                        max:100,
                                    message: '请正确填写memo',
                                    }],
                                    })(
                                    <Input placeholder="请输入memo"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.balance}>
                                    {getFieldDecorator('balance', {
                                       initialValue:`${findData.data.balance===undefined?'0.00000000':findData.data.balance}`,
                                    rules: [{
                                        required: true,
                                        max:20,
                                    message: '请正确填写币数量',
                                    }],
                                    })(
                                    <Input placeholder="请输入币数量"/>
                                    )}
                                </FormItem>


                                 <FormItem {...formItemLayout} label={fieldLabels.status}>
                                     {getFieldDecorator('status', {
                                       initialValue:`${findData.data.status===undefined?'0':findData.data.status}`,
                                     rules: [{
                                        required: true,
                                        max:5,
                                        message: '请正确填写状态',
                                     }],
                                     })(
                                     <Select placeholder="请选择状态">
                                              <Option value="0" >可用</Option>
                                              <Option value="1" >已用</Option>
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
                submitting: loading.effects['coinAccount/add'],
                }))(Form.create()(CoinAccountForm));
