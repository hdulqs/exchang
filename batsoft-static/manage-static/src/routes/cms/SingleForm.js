import React from 'react';
import {Card, Button, Form, Icon, Col, Row, Input, Select, Popover,DatePicker,Radio} from 'antd';
import {connect} from 'dva';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import FooterToolbar from '../../components/FooterToolbar';
import styles from '../../assets/style/style.less';
import {Link} from 'dva/router';
import TextEditor from '../../components/TextEditor';
import FileUpload from '../../components/FileUpload';
import BaseForm from '../../components/BatSoft/Form';

const {Option} = Select;
const FormItem = Form.Item;
const fieldLabels = {
            nav: '简标题',
            title: '文章题目',
            singleForm: '文章内容',
            singleKey: '文章标识',
            status: '是否显示',
            logoPath: 'logo',
};

@connect(({single}) => ({
single,
}))
class SingleForm extends BaseForm {
  state = {
fileList: null,
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('single/find',{id: match.params.id});
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
    const {single: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
params.singleForm = this.state.htmlContent;
            if(this.state.fileList!=null) {
             params.logoPath = this.state.fileList[0].response.result;
          }
          this.add('single/add',params,this.addCallBack);
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
                                <FormItem {...formItemLayout} label={fieldLabels.nav}>
                                    {getFieldDecorator('nav', {
                                        initialValue:`${findData.data.nav===undefined?'':findData.data.nav}`,
                                    rules: [{
                                        required: true,
                                        max:50,
                                    message: '请正确填写简标题',
                                    }],
                                    })(
                                    <Input placeholder="请输入简标题"/>
                                    )}
                                </FormItem>

                                <FormItem {...formItemLayout} label={fieldLabels.singleKey}>
                                  {getFieldDecorator('singleKey', {
                                    initialValue:`${findData.data.singleKey===undefined?'':findData.data.singleKey}`,
                                    rules: [{
                                      required: true,
                                      max:50,
                                      message: '请正确填写标识',
                                    }],
                                  })(
                                    <Input placeholder="请输入文章标识"/>
                                  )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.title}>
                                    {getFieldDecorator('title', {
                                        initialValue:`${findData.data.title===undefined?'':findData.data.title}`,
                                    rules: [{
                                        max:100,
                                    message: '请正确填写文章题目',
                                    }],
                                    })(
                                    <Input placeholder="请输入文章题目"/>
                                    )}
                                </FormItem>


                                 <FormItem {...formItemLayout} label={fieldLabels.singleForm}>
                                     {getFieldDecorator('singleForm')(
                                     <TextEditor callback={this.handleHTMLChange}/>
                                         )}
                                 </FormItem>



                                 <FormItem  {...formItemLayout} label={fieldLabels.status}>
                                     {getFieldDecorator('status', {
                                       initialValue:`${findData.data.status===undefined?'1':findData.data.status}`,
                                     rules: [{
                                        max:5,
                                     message: '请正确填写是否显示',
                                     }],
                                     })(
                                     <Radio.Group>
                                              <Radio value="0" >隐藏</Radio>
                                              <Radio value="1" >显示</Radio>
                                     </Radio.Group>
                                     )}
                                 </FormItem>


                                 <FormItem {...formItemLayout} label={fieldLabels.logoPath}>
                                     <FileUpload
                                             callback={this.handleFileChange}>
                                     </FileUpload>
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
                submitting: loading.effects['single/add'],
                }))(Form.create()(SingleForm));
