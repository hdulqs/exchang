import React from 'react';
import {Card, Button, Form, Icon, Col, Row, Input, Select, Popover,DatePicker,Radio} from 'antd';
import {connect} from 'dva';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import FooterToolbar from '../../components/FooterToolbar';
import styles from '../../assets/style/style.less';
import {Link} from 'dva/router';
import FileUpload from '../../components/FileUpload';
import BaseForm from '../../components/BatSoft/Form';

const {Option} = Select;
const FormItem = Form.Item;
const fieldLabels = {
            name: '名称',
            type: '链接类型',
            imageUrl: '图片上传',
            sort: '排序',
            linkUrl: '链接地址',
};

@connect(({friend}) => ({
friend,
}))
class FriendForm extends BaseForm {
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
      this.find('friend/find',{id: match.params.id});
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
    const {friend: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
            if(this.state.fileList!=null) {
             params.imageUrl = this.state.fileList[0].response.result;
          }
          this.add('friend/add',params,this.addCallBack);
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
                                <FormItem {...formItemLayout} label={fieldLabels.name}>
                                    {getFieldDecorator('name', {
                                        initialValue:`${findData.data.name===undefined?'':findData.data.name}`,
                                    rules: [{
                                        required: true,
                                        max:100,
                                    message: '请正确填写名称',
                                    }],
                                    })(
                                    <Input placeholder="请输入名称"/>
                                    )}
                                </FormItem>


                                 <FormItem {...formItemLayout} label={fieldLabels.type}>
                                     {getFieldDecorator('type', {
                                       initialValue:`${findData.data.type===undefined?'0':findData.data.type}`,
                                     rules: [{
                                        required: true,
                                        max:6,
                                        message: '请正确填写链接类型',
                                     }],
                                     })(
                                     <Select placeholder="请选择链接类型">
                                              <Option value="0" >logo类型</Option>
                                              <Option value="1" >文字类型</Option>
                                     </Select>
                                     )}
                                 </FormItem>


                                 <FormItem {...formItemLayout} label={fieldLabels.imageUrl}>
                                     <FileUpload
                                             callback={this.handleFileChange}>
                                     </FileUpload>
                                 </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.sort}>
                                    {getFieldDecorator('sort', {
                                       initialValue:`${findData.data.sort===undefined?'0':findData.data.sort}`,
                                    rules: [{
                                        max:10,
                                    message: '请正确填写排序',
                                    }],
                                    })(
                                    <Input placeholder="请输入排序"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.linkUrl}>
                                    {getFieldDecorator('linkUrl', {
                                        initialValue:`${findData.data.linkUrl===undefined?'':findData.data.linkUrl}`,
                                    rules: [{
                                        max:255,
                                    message: '请正确填写链接地址',
                                    }],
                                    })(
                                    <Input placeholder="请输入链接地址"/>
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
                submitting: loading.effects['friend/add'],
                }))(Form.create()(FriendForm));
