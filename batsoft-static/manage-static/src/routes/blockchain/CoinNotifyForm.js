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
            notifyUrl: '回调地址',
            notifyUser: '回调用户',
            notifyMethod: '回调方法',
            notifyParameter: '回调参数',
            notifyType: '回调类型POST/GET',
            notifyCount: '回调次数',
            notifyState: '回调状态',
};

@connect(({coinNotify}) => ({
coinNotify,
}))
class CoinNotifyForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('coinNotify/find',{id: match.params.id});
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
    const {coinNotify: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          this.add('coinNotify/add',params,this.addCallBack);
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
                                <FormItem {...formItemLayout} label={fieldLabels.notifyUrl}>
                                    {getFieldDecorator('notifyUrl', {
                                        initialValue:`${findData.data.notifyUrl===undefined?'':findData.data.notifyUrl}`,
                                    rules: [{
                                        required: true,
                                        max:16,
                                    message: '请正确填写回调地址',
                                    }],
                                    })(
                                    <Input placeholder="请输入回调地址"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.notifyUser}>
                                    {getFieldDecorator('notifyUser', {
                                        initialValue:`${findData.data.notifyUser===undefined?'':findData.data.notifyUser}`,
                                    rules: [{
                                        required: true,
                                        max:30,
                                    message: '请正确填写回调用户',
                                    }],
                                    })(
                                    <Input placeholder="请输入回调用户"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.notifyMethod}>
                                    {getFieldDecorator('notifyMethod', {
                                        initialValue:`${findData.data.notifyMethod===undefined?'':findData.data.notifyMethod}`,
                                    rules: [{
                                        required: true,
                                        max:20,
                                    message: '请正确填写回调方法',
                                    }],
                                    })(
                                    <Input placeholder="请输入回调方法"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.notifyParameter}>
                                    {getFieldDecorator('notifyParameter', {
                                        initialValue:`${findData.data.notifyParameter===undefined?'':findData.data.notifyParameter}`,
                                    rules: [{
                                    message: '请正确填写回调参数',
                                    }],
                                    })(
                                    <Input placeholder="请输入回调参数"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.notifyType}>
                                    {getFieldDecorator('notifyType', {
                                        initialValue:`${findData.data.notifyType===undefined?'':findData.data.notifyType}`,
                                    rules: [{
                                        max:20,
                                    message: '请正确填写回调类型POST/GET',
                                    }],
                                    })(
                                    <Input placeholder="请输入回调类型POST/GET"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.notifyCount}>
                                    {getFieldDecorator('notifyCount', {
                                        initialValue:`${findData.data.notifyCount===undefined?'':findData.data.notifyCount}`,
                                    rules: [{
                                        max:100,
                                    message: '请正确填写回调次数',
                                    }],
                                    })(
                                    <Input placeholder="请输入回调次数"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.notifyState}>
                                    {getFieldDecorator('notifyState', {
                                        initialValue:`${findData.data.notifyState===undefined?'':findData.data.notifyState}`,
                                    rules: [{
                                        max:20,
                                    message: '请正确填写回调状态',
                                    }],
                                    })(
                                    <Input placeholder="请输入回调状态"/>
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
                submitting: loading.effects['coinNotify/add'],
                }))(Form.create()(CoinNotifyForm));
