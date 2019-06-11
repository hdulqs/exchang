import React from 'react';
import {Card, Button, Form, Icon, Col, Row, Input, Select, Popover, DatePicker, Radio} from 'antd';
import {connect} from 'dva';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import FooterToolbar from '../../components/FooterToolbar';
import styles from '../../assets/style/style.less';
import {Link} from 'dva/router';
import FileUpload from '../../components/FileUpload';
import TextEditor from '../../components/TextEditor';
import BaseForm from '../../components/BatSoft/Form';

const {Option} = Select;
const FormItem = Form.Item;
const {TextArea} = Input;
const fieldLabels = {
  typeName: '文章类别',
  title: '标题',
  filePath: '标题图片',
  typeKey: '文章类型',
  author: '作者',
  articleFrom: '来源',
  fromUrl: '来源URL',
  display: '是否显示',
  hits: '点击数',
  shortContent: '简介',
  seoKey: 'seo关键字',
  content: '内容',
};
@connect(({article,articleChannel}) => ({
  article,
  articleChannel,
}))
class ArticleForm extends BaseForm {
  state = {
    fileList: null,
    formType: 'add',
    typeName:'',
  };

  componentDidMount() {
    const {match} = this.props;
    this.find("articleChannel/findChannels");
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('article/find', {id: match.params.id});
    }
  }

  /**
   * 增加、修改回调函数
   * @param data
   */
  addCallBack = data => {
    const {form} = this.props;
    if (this.state.formType === "add") {
      form.resetFields();
    }
  }

  /**
   * 类别选择事件
   * @param e
   */
  handleSelectChange=(value,label)=>{
    this.setState({
      typeName:label.props.children
    })
  }

  render() {
    const formItemLayout = {
      labelCol: {
        xs: {span: 24},
        sm: {span: 7},
      },
      wrapperCol: {
        xs: {span: 24},
        sm: {span: 12},
        md: {span: 10},
      },
    };
    const {article: {findData},articleChannel:{findData:channels}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;

    const children=[];
    if(channels instanceof  Array) {
      channels.forEach((item) => {
        children.push(<Option key={item.typeKey}>{item.name}</Option>);
      });
    }
    if (this.state.formType == "add") {
      findData.data = {};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          if (this.state.fileList != null) {
            params.filePath = this.state.fileList[0].response.result;
          }
          params.content = this.state.htmlContent;
          params.typeName=this.state.typeName;
          this.add('article/add', params, this.addCallBack);
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
          <Form hideRequiredMark>
            <Form.Item {...formItemLayout} label={fieldLabels.id}>
              {getFieldDecorator('id', {
                initialValue: findData.data.id,
              })(
                <Input style={{display: 'none'}}/>
              )}
            </Form.Item>
            <FormItem {...formItemLayout} label={fieldLabels.typeKey}>
              {getFieldDecorator('typeKey', {
                initialValue: `${findData.data.typeKey === undefined ?'' : findData.data.typeKey}`,
                rules: [{
                  required: true,
                  message: '请正确填写文章类型',
                }],
              })(
                <Select placeholder="请选择文章类型" onChange={this.handleSelectChange}>
                  {children}
                </Select>
              )}
            </FormItem>

            <FormItem {...formItemLayout} label={fieldLabels.title}>
              {getFieldDecorator('title', {
                initialValue: `${findData.data.title === undefined ? '' : findData.data.title}`,
                rules: [{
                  required: true,
                  max: 50,
                  message: '请正确填写标题',
                }],
              })(
                <Input placeholder="请输入标题"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.filePath}>
              <FileUpload
                callback={this.handleFileChange}>
              </FileUpload>
            </FormItem>

            <FormItem {...formItemLayout} label={fieldLabels.author}>
              {getFieldDecorator('author', {
                initialValue: `${findData.data.author === undefined ? '' : findData.data.author}`,
                rules: [{
                  max: 50,
                  message: '请正确填写作者',
                }],
              })(
                <Input placeholder="请输入作者"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.articleFrom}>
              {getFieldDecorator('articleFrom', {
                initialValue: `${findData.data.articleFrom === undefined ? '' : findData.data.articleFrom}`,
                rules: [{
                  max: 255,
                  message: '请正确填写来源',
                }],
              })(
                <Input placeholder="请输入来源"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.fromUrl}>
              {getFieldDecorator('fromUrl', {
                initialValue: `${findData.data.fromUrl === undefined ? '' : findData.data.fromUrl}`,
                rules: [{
                  max: 255,
                  message: '请正确填写来源URL',
                }],
              })(
                <Input placeholder="请输入来源URL"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.display}>
              {getFieldDecorator('display', {
                initialValue: `${findData.data.display === undefined ? '1' : findData.data.display}`,
                rules: [{
                  max: 2,
                  message: '请正确填写是否显示',
                }],
              })(
                <Select placeholder="请选择是否显示">
                  <Option value="0">隐藏</Option>
                  <Option value="1">显示</Option>
                </Select>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.hits}>
              {getFieldDecorator('hits', {
                initialValue: `${findData.data.hits === undefined ? '' : findData.data.hits}`,
                rules: [{
                  max: 255,
                  message: '请正确填写点击数',
                }],
              })(
                <Input placeholder="请输入点击数"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.shortContent}>
              {getFieldDecorator('shortContent', {
                initialValue: `${findData.data.shortContent === undefined ? '' : findData.data.shortContent}`,
                rules: [{
                  max: 255,
                  message: '请正确填写简介',
                }],
              })(
                <TextArea style={{minHeight: 32}} placeholder="简介" rows={4}/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.seoKey}>
              {getFieldDecorator('seoKey', {
                initialValue: `${findData.data.seoKey === undefined ? '' : findData.data.seoKey}`,
                rules: [{
                  max: 255,
                  message: '请正确填写seo关键字',
                }],
              })(
                <TextArea style={{minHeight: 32}} placeholder="seo关键字" rows={4}/>
              )}
            </FormItem>


            <FormItem label={fieldLabels.content}>
              {getFieldDecorator('content')(
                <TextEditor callback={this.handleHTMLChange}/>
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
  submitting: loading.effects['article/add'],
}))(Form.create()(ArticleForm));
