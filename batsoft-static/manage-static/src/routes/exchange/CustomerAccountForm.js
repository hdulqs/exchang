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
            accountNumber: '账户账号',
            userId: '用户id',
            userName: '用户名',
            coinCode: '币种',
            coinAddress: '币地址',
            memo: '币地址标签',
            available: '可用金额',
            freeze: '冻结金额',
            status: '状态',
};

@connect(({customerAccount}) => ({
customerAccount,
}))
class CustomerAccountForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('customerAccount/find',{id: match.params.id});
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
    const {customerAccount: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          this.add('customerAccount/add',params,this.addCallBack);
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
                                        required: true,
                                        max:50,
                                    message: '请正确填写用户名',
                                    }],
                                    })(
                                    <Input placeholder="请输入用户名"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.coinCode}>
                                    {getFieldDecorator('coinCode', {
                                        initialValue:`${findData.data.coinCode===undefined?'':findData.data.coinCode}`,
                                    rules: [{
                                        required: true,
                                        max:50,
                                    message: '请正确填写币种',
                                    }],
                                    })(
                                    <Input placeholder="请输入币种"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.coinAddress}>
                                    {getFieldDecorator('coinAddress', {
                                        initialValue:`${findData.data.coinAddress===undefined?'':findData.data.coinAddress}`,
                                    rules: [{
                                        max:255,
                                    message: '请正确填写币地址',
                                    }],
                                    })(
                                    <Input placeholder="请输入币地址"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.memo}>
                                    {getFieldDecorator('memo', {
                                        initialValue:`${findData.data.memo===undefined?'':findData.data.memo}`,
                                    rules: [{
                                        max:255,
                                    message: '请正确填写币地址标签',
                                    }],
                                    })(
                                    <Input placeholder="请输入币地址标签"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.available}>
                                    {getFieldDecorator('available', {
                                       initialValue:`${findData.data.available===undefined?'0.00000000':findData.data.available}`,
                                    rules: [{
                                        max:20,
                                    message: '请正确填写可用金额',
                                    }],
                                    })(
                                    <Input placeholder="请输入可用金额"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.freeze}>
                                    {getFieldDecorator('freeze', {
                                       initialValue:`${findData.data.freeze===undefined?'0.00000000':findData.data.freeze}`,
                                    rules: [{
                                        max:20,
                                    message: '请正确填写冻结金额',
                                    }],
                                    })(
                                    <Input placeholder="请输入冻结金额"/>
                                    )}
                                </FormItem>


                                 <FormItem  {...formItemLayout} label={fieldLabels.status}>
                                     {getFieldDecorator('status', {
                                       initialValue:`${findData.data.status===undefined?'0':findData.data.status}`,
                                     rules: [{
                                        max:1,
                                     message: '请正确填写状态',
                                     }],
                                     })(
                                     <Radio.Group>
                                              <Radio value="0" >正常</Radio>
                                              <Radio value="1" >冻结</Radio>
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
                submitting: loading.effects['customerAccount/add'],
                }))(Form.create()(CustomerAccountForm));
