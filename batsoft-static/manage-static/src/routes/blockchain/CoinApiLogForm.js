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
            requestIp: '请求ip',
            requestUser: '请求用户',
            requestMethod: '请求方法',
            requestParameter: '请求参数',
            requestState: '返回状态',
};

@connect(({coinApiLog}) => ({
coinApiLog,
}))
class CoinApiLogForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('coinApiLog/find',{id: match.params.id});
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
    const {coinApiLog: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          this.add('coinApiLog/add',params,this.addCallBack);
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
                                <FormItem {...formItemLayout} label={fieldLabels.requestIp}>
                                    {getFieldDecorator('requestIp', {
                                        initialValue:`${findData.data.requestIp===undefined?'':findData.data.requestIp}`,
                                    rules: [{
                                        required: true,
                                        max:16,
                                    message: '请正确填写请求ip',
                                    }],
                                    })(
                                    <Input placeholder="请输入请求ip"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.requestUser}>
                                    {getFieldDecorator('requestUser', {
                                        initialValue:`${findData.data.requestUser===undefined?'':findData.data.requestUser}`,
                                    rules: [{
                                        required: true,
                                        max:30,
                                    message: '请正确填写请求用户',
                                    }],
                                    })(
                                    <Input placeholder="请输入请求用户"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.requestMethod}>
                                    {getFieldDecorator('requestMethod', {
                                        initialValue:`${findData.data.requestMethod===undefined?'':findData.data.requestMethod}`,
                                    rules: [{
                                        required: true,
                                        max:20,
                                    message: '请正确填写请求方法',
                                    }],
                                    })(
                                    <Input placeholder="请输入请求方法"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.requestParameter}>
                                    {getFieldDecorator('requestParameter', {
                                        initialValue:`${findData.data.requestParameter===undefined?'':findData.data.requestParameter}`,
                                    rules: [{
                                    message: '请正确填写请求参数',
                                    }],
                                    })(
                                    <Input placeholder="请输入请求参数"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.requestState}>
                                    {getFieldDecorator('requestState', {
                                        initialValue:`${findData.data.requestState===undefined?'':findData.data.requestState}`,
                                    rules: [{
                                        max:20,
                                    message: '请正确填写返回状态',
                                    }],
                                    })(
                                    <Input placeholder="请输入返回状态"/>
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
                submitting: loading.effects['coinApiLog/add'],
                }))(Form.create()(CoinApiLogForm));
