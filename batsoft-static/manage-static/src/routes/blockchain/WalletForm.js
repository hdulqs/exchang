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
            walletType: '钱包类型',
            walletName: '钱包名称',
            walletCode: '钱包代码',
            rpcProtocol: '协议',
            rpcIp: '钱包ip',
            rpcAccount: '钱包账号',
            rpcPassword: '钱包密码',
            rpcPort: '钱包端口',
            walletRemark: '钱包描述',
            status: '状态',
            blockHigh: '区块高度',
};

@connect(({wallet}) => ({
wallet,
}))
class WalletForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('wallet/find',{id: match.params.id});
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
    const {wallet: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          this.add('wallet/add',params,this.addCallBack);
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
                                 <FormItem {...formItemLayout} label={fieldLabels.walletType}>
                                     {getFieldDecorator('walletType', {
                                        initialValue:`${findData.data.walletType===undefined?'':findData.data.walletType}`,
                                     rules: [{
                                        max:255,
                                        message: '请正确填写钱包类型',
                                     }],
                                     })(
                                     <Select placeholder="请选择钱包类型">
                                              <Option value="BTC" >BTC</Option>
                                              <Option value="ETH" >ETH</Option>
                                              <Option value="EOS" >EOS</Option>
                                     </Select>
                                     )}
                                 </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.walletName}>
                                    {getFieldDecorator('walletName', {
                                        initialValue:`${findData.data.walletName===undefined?'':findData.data.walletName}`,
                                    rules: [{
                                        required: true,
                                        max:20,
                                    message: '请正确填写钱包名称',
                                    }],
                                    })(
                                    <Input placeholder="请输入钱包名称"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.walletCode}>
                                    {getFieldDecorator('walletCode', {
                                        initialValue:`${findData.data.walletCode===undefined?'':findData.data.walletCode}`,
                                    rules: [{
                                        required: true,
                                        max:20,
                                    message: '请正确填写钱包代码',
                                    }],
                                    })(
                                    <Input placeholder="请输入钱包代码"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.rpcProtocol}>
                                    {getFieldDecorator('rpcProtocol', {
                                       initialValue:`${findData.data.rpcProtocol===undefined?'':findData.data.rpcProtocol}`,
                                    rules: [{
                                        max:20,
                                    message: '请正确填写协议',
                                    }],
                                    })(
                                    <Input placeholder="请输入协议"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.rpcIp}>
                                    {getFieldDecorator('rpcIp', {
                                        initialValue:`${findData.data.rpcIp===undefined?'':findData.data.rpcIp}`,
                                    rules: [{
                                        required: true,
                                        max:15,
                                    message: '请正确填写钱包ip',
                                    }],
                                    })(
                                    <Input placeholder="请输入钱包ip"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.rpcAccount}>
                                    {getFieldDecorator('rpcAccount', {
                                        initialValue:`${findData.data.rpcAccount===undefined?'':findData.data.rpcAccount}`,
                                    rules: [{
                                        max:50,
                                    message: '请正确填写钱包账号',
                                    }],
                                    })(
                                    <Input placeholder="请输入钱包账号"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.rpcPassword}>
                                    {getFieldDecorator('rpcPassword', {
                                        initialValue:`${findData.data.rpcPassword===undefined?'':findData.data.rpcPassword}`,
                                    rules: [{
                                        max:50,
                                    message: '请正确填写钱包密码',
                                    }],
                                    })(
                                    <Input placeholder="请输入钱包密码"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.rpcPort}>
                                    {getFieldDecorator('rpcPort', {
                                        initialValue:`${findData.data.rpcPort===undefined?'':findData.data.rpcPort}`,
                                    rules: [{
                                        required: true,
                                        max:10,
                                    message: '请正确填写钱包端口',
                                    }],
                                    })(
                                    <Input placeholder="请输入钱包端口"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.walletRemark}>
                                    {getFieldDecorator('walletRemark', {
                                        initialValue:`${findData.data.walletRemark===undefined?'':findData.data.walletRemark}`,
                                    rules: [{
                                        max:255,
                                    message: '请正确填写钱包描述',
                                    }],
                                    })(
                                    <Input placeholder="请输入钱包描述"/>
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


                                <FormItem {...formItemLayout} label={fieldLabels.blockHigh}>
                                    {getFieldDecorator('blockHigh', {
                                        initialValue:`${findData.data.blockHigh===undefined?'':findData.data.blockHigh}`,
                                    rules: [{
                                        max:20,
                                    message: '请正确填写区块高度',
                                    }],
                                    })(
                                    <Input placeholder="请输入区块高度"/>
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
                submitting: loading.effects['wallet/add'],
                }))(Form.create()(WalletForm));
