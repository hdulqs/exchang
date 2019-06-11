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
const { TextArea } = Input;
const dateFormat = 'YYYY-MM-DD HH:mm:ss';
const fieldLabels = {
            smsProvider: '短信方',
            smsType: '短信类型',
            smsContent: '短信内容',
            sendTime: '发送时间',
            cellphone: '电话号码',
            status: '发送状态',
};

@connect(({messageSend}) => ({
messageSend,
}))
class MessageSendForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('messageSend/find',{id: match.params.id});
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
    const {messageSend: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
            params.sendTime  =params.sendTime .format(dateFormat);
          this.add('messageSend/add',params,this.addCallBack);
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
                                <FormItem {...formItemLayout} label={fieldLabels.smsProvider}>
                                    {getFieldDecorator('smsProvider', {
                                        initialValue:`${findData.data.smsProvider===undefined?'':findData.data.smsProvider}`,
                                    rules: [{
                                        max:100,
                                    message: '请正确填写短信方',
                                    }],
                                    })(
                                    <Input placeholder="请输入短信方"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.smsType}>
                                    {getFieldDecorator('smsType', {
                                        initialValue:`${findData.data.smsType===undefined?'':findData.data.smsType}`,
                                    rules: [{
                                        max:25,
                                    message: '请正确填写短信类型',
                                    }],
                                    })(
                                    <Input placeholder="请输入短信类型"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.smsContent}>
                                    {getFieldDecorator('smsContent', {
                                        initialValue:`${findData.data.smsContent===undefined?'':findData.data.smsContent}`,
                                    rules: [{
                                    message: '请正确填写短信内容',
                                    }],
                                    })(
                                    <TextArea style={{ minHeight: 32 }} placeholder="短信内容" rows={4} />
                                    )}
                                </FormItem>



                                 <FormItem {...formItemLayout} label={fieldLabels.sendTime}>
                                     {getFieldDecorator('sendTime', {
                                     initialValue:findData.data.sendTime==null?'': moment(findData.data.sendTime,dateFormat) ,
                                     rules: [{
                                        required: true,
                                     message: '请正确填写发送时间',
                                     }],
                                     })(
                                     <DatePicker format={dateFormat} placeholder="请输入发送时间"/>
                                     )}
                                 </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.cellphone}>
                                    {getFieldDecorator('cellphone', {
                                        initialValue:`${findData.data.cellphone===undefined?'':findData.data.cellphone}`,
                                    rules: [{
                                        max:255,
                                    message: '请正确填写电话号码',
                                    }],
                                    })(
                                    <Input placeholder="请输入电话号码"/>
                                    )}
                                </FormItem>


                                 <FormItem  {...formItemLayout} label={fieldLabels.status}>
                                     {getFieldDecorator('status', {
                                       initialValue:`${findData.data.status===undefined?'0':findData.data.status}`,
                                     rules: [{
                                        max:5,
                                     message: '请正确填写发送状态',
                                     }],
                                     })(
                                     <Radio.Group>
                                              <Radio value="0" >失败</Radio>
                                              <Radio value="1" >成功</Radio>
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
                submitting: loading.effects['messageSend/add'],
                }))(Form.create()(MessageSendForm));
