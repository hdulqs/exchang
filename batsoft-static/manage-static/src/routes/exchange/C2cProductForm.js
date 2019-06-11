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
            coinCode: '币种编码',
            minNumber: '最小交易金额',
            maxNumber: '最大交易金额',
            buyPrice: '购买单价',
            sellPrice: '卖出单价',
            status: '是否有效',
            sort: '排序',
};

@connect(({c2cProduct}) => ({
c2cProduct,
}))
class C2cProductForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('c2cProduct/find',{id: match.params.id});
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
    const {c2cProduct: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          this.add('c2cProduct/add',params,this.addCallBack);
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
                                        required: true,
                                        max:30,
                                    message: '请正确填写币种编码',
                                    }],
                                    })(
                                    <Input placeholder="请输入币种编码"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.minNumber}>
                                    {getFieldDecorator('minNumber', {
                                       initialValue:`${findData.data.minNumber===undefined?'0.00000000':findData.data.minNumber}`,
                                    rules: [{
                                        required: true,
                                        max:15,
                                    message: '请正确填写最小交易金额',
                                    }],
                                    })(
                                    <Input placeholder="请输入最小交易金额"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.maxNumber}>
                                    {getFieldDecorator('maxNumber', {
                                       initialValue:`${findData.data.maxNumber===undefined?'0.00000000':findData.data.maxNumber}`,
                                    rules: [{
                                        required: true,
                                        max:15,
                                    message: '请正确填写最大交易金额',
                                    }],
                                    })(
                                    <Input placeholder="请输入最大交易金额"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.buyPrice}>
                                    {getFieldDecorator('buyPrice', {
                                        initialValue:`${findData.data.buyPrice===undefined?'':findData.data.buyPrice}`,
                                    rules: [{
                                        required: true,
                                        max:10,
                                    message: '请正确填写购买单价',
                                    }],
                                    })(
                                    <Input placeholder="请输入购买单价"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.sellPrice}>
                                    {getFieldDecorator('sellPrice', {
                                        initialValue:`${findData.data.sellPrice===undefined?'':findData.data.sellPrice}`,
                                    rules: [{
                                        required: true,
                                        max:10,
                                    message: '请正确填写卖出单价',
                                    }],
                                    })(
                                    <Input placeholder="请输入卖出单价"/>
                                    )}
                                </FormItem>


                                 <FormItem  {...formItemLayout} label={fieldLabels.status}>
                                     {getFieldDecorator('status', {
                                        initialValue:`${findData.data.status===undefined?'':findData.data.status}`,
                                     rules: [{
                                        max:5,
                                     message: '请正确填写是否有效',
                                     }],
                                     })(
                                     <Radio.Group>
                                              <Radio value="0" >否</Radio>
                                              <Radio value="1" >是</Radio>
                                     </Radio.Group>
                                     )}
                                 </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.sort}>
                                    {getFieldDecorator('sort', {
                                        initialValue:`${findData.data.sort===undefined?'':findData.data.sort}`,
                                    rules: [{
                                        max:5,
                                    message: '请正确填写排序',
                                    }],
                                    })(
                                    <Input placeholder="请输入排序"/>
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
                submitting: loading.effects['c2cProduct/add'],
                }))(Form.create()(C2cProductForm));
