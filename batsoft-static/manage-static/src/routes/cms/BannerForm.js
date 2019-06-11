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
            name: 'Banner名称',
            url: '链接地址',
            image: 'banner图片',
            display: '是否显示',
            sort: '排序',
};

@connect(({banner}) => ({
banner,
}))
class BannerForm extends BaseForm {
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
      this.find('banner/find',{id: match.params.id});
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
    const {banner: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
            if(this.state.fileList!=null) {
             params.image = this.state.fileList[0].response.result;
          }
          this.add('banner/add',params,this.addCallBack);
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
                                    message: '请正确填写Banner名称',
                                    }],
                                    })(
                                    <Input placeholder="请输入Banner名称"/>
                                    )}
                                </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.url}>
                                    {getFieldDecorator('url', {
                                        initialValue:`${findData.data.url===undefined?'':findData.data.url}`,
                                    rules: [{
                                        max:255,
                                    message: '请正确填写链接地址',
                                    }],
                                    })(
                                    <Input placeholder="请输入链接地址"/>
                                    )}
                                </FormItem>


                                 <FormItem {...formItemLayout} label={fieldLabels.image}>
                                     <FileUpload
                                             callback={this.handleFileChange}>
                                     </FileUpload>
                                 </FormItem>


                                 <FormItem {...formItemLayout} label={fieldLabels.display}>
                                     {getFieldDecorator('display', {
                                       initialValue:`${findData.data.display===undefined?'1':findData.data.display}`,
                                     rules: [{
                                        max:2,
                                        message: '请正确填写是否显示',
                                     }],
                                     })(
                                     <Select placeholder="请选择是否显示">
                                              <Option value="0" >隐藏</Option>
                                              <Option value="1" >显示</Option>
                                     </Select>
                                     )}
                                 </FormItem>


                                <FormItem {...formItemLayout} label={fieldLabels.sort}>
                                    {getFieldDecorator('sort', {
                                        initialValue:`${findData.data.sort===undefined?'':findData.data.sort}`,
                                    rules: [{
                                        max:11,
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
                submitting: loading.effects['banner/add'],
                }))(Form.create()(BannerForm));
