import React from 'react';
import {Card, Button, Form, Icon, Col, Row, Input, Select, Popover,DatePicker,Radio} from 'antd';
import {connect} from 'dva';
import PageHeaderLayout from '../../../layouts/PageHeaderLayout';
import FooterToolbar from '../../../components/FooterToolbar';
import styles from '../../../assets/style/style.less';
import {Link} from 'dva/router';
import BaseForm from '../../../components/BatSoft/Form';

const {Option} = Select;
const FormItem = Form.Item;
const { TextArea } = Input;
const fieldLabels = {
            typeName: '类型名称',
            typeKey: '类型key',
            sort: '排序',
            remark: '备注',
};

@connect(({appConfigType}) => ({
appConfigType,
}))
class AppConfigTypeForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('appConfigType/find',{id: match.params.id});
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
    const {appConfigType: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          this.add('appConfigType/add',params,this.addCallBack);
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
                                <FormItem {...formItemLayout} label={fieldLabels.typeName}>
                                    {getFieldDecorator('typeName', {
                                        initialValue:`${findData.data.typeName===undefined?'':findData.data.typeName}`,
                                    rules: [{
                                        required: true,
                                        max:50,
                                    message: '请正确填写类型名称',
                                    }],
                                    })(
                                    <Input placeholder="请输入类型名称"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.typeKey}>
                                    {getFieldDecorator('typeKey', {
                                        initialValue:`${findData.data.typeKey===undefined?'':findData.data.typeKey}`,
                                    rules: [{
                                        required: true,
                                        max:50,
                                    message: '请正确填写类型key',
                                    }],
                                    })(
                                    <Input placeholder="请输入类型key"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.sort}>
                                    {getFieldDecorator('sort', {
                                       initialValue:`${findData.data.sort===undefined?'0':findData.data.sort}`,
                                    rules: [{
                                        required: true,
                                        max:1,
                                    message: '请正确填写排序',
                                    }],
                                    })(
                                    <Input placeholder="请输入排序"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.remark}>
                                    {getFieldDecorator('remark', {
                                        initialValue:`${findData.data.remark===undefined?'':findData.data.remark}`,
                                    rules: [{
                                        max:255,
                                    message: '请正确填写备注',
                                    }],
                                    })(
                                    <TextArea style={{ minHeight: 32 }} placeholder="备注" rows={4} />
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
                submitting: loading.effects['appConfigType/add'],
                }))(Form.create()(AppConfigTypeForm));
