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
            coinPair: '交易对',
            klineTime: '时区',
            klineData: 'K线数据',
};

@connect(({kline}) => ({
kline,
}))
class KlineForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('kline/find',{id: match.params.id});
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
    const {kline: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          this.add('kline/add',params,this.addCallBack);
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
                                <FormItem {...formItemLayout} label={fieldLabels.coinPair}>
                                    {getFieldDecorator('coinPair', {
                                        initialValue:`${findData.data.coinPair===undefined?'':findData.data.coinPair}`,
                                    rules: [{
                                        required: true,
                                        max:10,
                                    message: '请正确填写交易对',
                                    }],
                                    })(
                                    <Input placeholder="请输入交易对"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.klineTime}>
                                    {getFieldDecorator('klineTime', {
                                        initialValue:`${findData.data.klineTime===undefined?'':findData.data.klineTime}`,
                                    rules: [{
                                        required: true,
                                        max:10,
                                    message: '请正确填写时区',
                                    }],
                                    })(
                                    <Input placeholder="请输入时区"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.klineData}>
                                    {getFieldDecorator('klineData', {
                                        initialValue:`${findData.data.klineData===undefined?'':findData.data.klineData}`,
                                    rules: [{
                                    message: '请正确填写K线数据',
                                    }],
                                    })(
                                    <TextArea style={{ minHeight: 32 }} placeholder="K线数据" rows={4} />
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
                submitting: loading.effects['kline/add'],
                }))(Form.create()(KlineForm));
