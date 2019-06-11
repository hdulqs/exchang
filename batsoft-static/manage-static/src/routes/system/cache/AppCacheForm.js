import React from 'react';
import {Card, Button, Form, Icon, Col, Row, Input, Select, Popover, DatePicker, Radio} from 'antd';
import {connect} from 'dva';
import PageHeaderLayout from '../../../layouts/PageHeaderLayout';
import FooterToolbar from '../../../components/FooterToolbar';
import styles from '../../../assets/style/style.less';
import {Link} from 'dva/router';
import BaseForm from '../../../components/BatSoft/Form';

const {Option} = Select;
const FormItem = Form.Item;
const fieldLabels = {
  cacheKey: '缓存key',
  cacheValue: 'cache_value',
  cacheType: '缓存类型',
  cacheValueType: '缓存值类型',
  cacheTime: '缓存时间(s)0为永久',
};

@connect(({appCache}) => ({
  appCache,
}))
class AppCacheForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('appCache/find', {id: match.params.id});
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
    const {appCache: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data = {};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          this.add('appCache/add', params, this.addCallBack);
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
            <FormItem {...formItemLayout} label={fieldLabels.cacheKey}>
              {getFieldDecorator('cacheKey', {
                initialValue: `${findData.data.cacheKey === undefined ? '' : findData.data.cacheKey}`,
                rules: [{
                  required: true,
                  max: 100,
                  message: '请正确填写缓存key',
                }],
              })(
                <Input placeholder="请输入缓存key"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.cacheValue}>
              {getFieldDecorator('cacheValue', {
                initialValue: `${findData.data.cacheValue === undefined ? '' : findData.data.cacheValue}`,
                rules: [{
                  message: '请正确填写cache_value',
                }],
              })(
                <Input placeholder="请输入cache_value"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.cacheType}>
              {getFieldDecorator('cacheType', {
                initialValue: `${findData.data.cacheType === undefined ? '1' : findData.data.cacheType}`,
                rules: [{
                  required: true,
                  max: 1,
                  message: '请正确填写缓存类型',
                }],
              })(
                <Select placeholder="请选择缓存类型">
                  <Option value="0">永久缓存</Option>
                  <Option value="1">临时缓存</Option>
                </Select>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.cacheValueType}>
              {getFieldDecorator('cacheValueType', {
                initialValue: `${findData.data.cacheValueType === undefined ? '' : findData.data.cacheValueType}`,
                rules: [{
                  max: 1,
                  message: '请正确填写缓存值类型',
                }],
              })(
                <Select placeholder="请选择缓存值类型">
                  <Option value="0">字符串</Option>
                  <Option value="1">对象</Option>
                </Select>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.cacheTime}>
              {getFieldDecorator('cacheTime', {
                initialValue: `${findData.data.cacheTime === undefined ? '0' : findData.data.cacheTime}`,
                rules: [{
                  max: 10,
                  message: '请正确填写缓存时间(s)0为永久',
                }],
              })(
                <Input placeholder="请输入缓存时间(s)0为永久"/>
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
  submitting: loading.effects['appCache/add'],
}))(Form.create()(AppCacheForm));
