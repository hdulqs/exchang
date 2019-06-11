import React from 'react';
import {Card, Button, Form, Icon, Col, Row, Input, Select, Popover, DatePicker, Radio} from 'antd';
import {connect} from 'dva';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import FooterToolbar from '../../components/FooterToolbar';
import styles from '../../assets/style/style.less';
import {Link} from 'dva/router';
import BaseForm from '../../components/BatSoft/Form';

const {Option} = Select;
const FormItem = Form.Item;
const {TextArea} = Input;
const fieldLabels = {
  name: '文章类别',
  typeKey: '类型Key',
  indexShow: '是否首页展示',
  parentId: '所属父类',
  status: '状态',
  description: '分类描述',
  icon: '图标',
  sort: '排序',
};

@connect(({articleChannel}) => ({
  articleChannel,
}))
class ArticleChannelForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('articleChannel/find', {id: match.params.id});
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
    const {articleChannel: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data = {};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          this.add('articleChannel/add', params, this.addCallBack);
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
            <FormItem {...formItemLayout} label={fieldLabels.name}>
              {getFieldDecorator('name', {
                initialValue: `${findData.data.name === undefined ? '' : findData.data.name}`,
                rules: [{
                  required: true,
                  max: 100,
                  message: '请正确填写文章类别',
                }],
              })(
                <Input placeholder="请输入文章类别"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.typeKey}>
              {getFieldDecorator('typeKey', {
                initialValue: `${findData.data.typeKey === undefined ? '' : findData.data.typeKey}`,
                rules: [{
                  required: true,
                  max: 50,
                  message: '请正确填写类型Key',
                }],
              })(
                <Input placeholder="请输入类型Key"/>
              )}
            </FormItem>


            <FormItem  {...formItemLayout} label={fieldLabels.indexShow}>
              {getFieldDecorator('indexShow', {
                initialValue: `${findData.data.indexShow === undefined ? '0' : findData.data.indexShow}`,
                rules: [{
                  required: true,
                  max: 1,
                  message: '请正确填写是否首页展示',
                }],
              })(
                <Radio.Group>
                  <Radio value="0">否</Radio>
                  <Radio value="1">是</Radio>
                </Radio.Group>
              )}
            </FormItem>


           {/* <FormItem {...formItemLayout} label={fieldLabels.parentId}>
              {getFieldDecorator('parentId', {
                initialValue: `${findData.data.parentId === undefined ? '0' : findData.data.parentId}`,
                rules: [{
                  max: 64,
                  message: '请正确填写所属父类',
                }],
              })(
                <Input style={{display: 'none'}} placeholder="请输入所属父类"/>
              )}
            </FormItem>*/}


            <FormItem  {...formItemLayout} label={fieldLabels.status}>
              {getFieldDecorator('status', {
                initialValue: `${findData.data.status === undefined ? '1' : findData.data.status}`,
                rules: [{
                  required: true,
                  max: 1,
                  message: '请正确填写状态',
                }],
              })(
                <Radio.Group>
                  <Radio value="0">禁用</Radio>
                  <Radio value="1">可用</Radio>
                </Radio.Group>
              )}
            </FormItem>





            <FormItem {...formItemLayout} label={fieldLabels.icon}>
              {getFieldDecorator('icon', {
                initialValue: `${findData.data.icon === undefined ? '' : findData.data.icon}`,
                rules: [{
                  max: 100,
                  message: '请正确填写图标路径',
                }],
              })(
                <Input placeholder="请输入图标路径"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.sort}>
              {getFieldDecorator('sort', {
                initialValue: `${findData.data.sort === undefined ? '' : findData.data.sort}`,
                rules: [{
                  max: 6,
                  message: '请正确填写排序',
                }],
              })(
                <Input placeholder="请输入排序"/>
              )}
            </FormItem>

            <FormItem {...formItemLayout} label={fieldLabels.description}>
              {getFieldDecorator('description', {
                initialValue: `${findData.data.description === undefined ? '' : findData.data.description}`,
                rules: [{
                  message: '请正确填写分类描述',
                }],
              })(
                <TextArea style={{minHeight: 32}} placeholder="分类描述" rows={4}/>
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
  submitting: loading.effects['articleChannel/add'],
}))(Form.create()(ArticleChannelForm));
