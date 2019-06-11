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
const fieldLabels = {
  currencyName: '货币名称',
  currencyCode: '币种类型',
  availableMoney: '可用金额',
  freezeMoney: '冻结金额',
  currencyLogo: '币种logo',
  userId: '用户id',
  userRealName: '用户真实姓名',
  userMobile: '用户手机号',
  status: '状态',
};

@connect(({finance}) => ({
  finance,
}))
class FinanceForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('finance/find', {id: match.params.id});
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
    const {finance: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data = {};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          this.add('finance/add', params, this.addCallBack);
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
            <FormItem {...formItemLayout} label={fieldLabels.currencyName}>
              {getFieldDecorator('currencyName', {
                initialValue: `${findData.data.currencyName === undefined ? '' : findData.data.currencyName}`,
                rules: [{
                  required: true,
                  max: 100,
                  message: '请正确填写货币名称',
                }],
              })(
                <Input placeholder="请输入货币名称"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.currencyCode}>
              {getFieldDecorator('currencyCode', {
                initialValue: `${findData.data.currencyCode === undefined ? '' : findData.data.currencyCode}`,
                rules: [{
                  max: 20,
                  message: '请正确填写币种类型',
                }],
              })(
                <Input placeholder="请输入币种类型"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.availableMoney}>
              {getFieldDecorator('availableMoney', {
                initialValue: `${findData.data.availableMoney === undefined ? '0.0000000000' : findData.data.availableMoney}`,
                rules: [{
                  max: 10,
                  message: '请正确填写可用金额',
                }],
              })(
                <Input placeholder="请输入可用金额"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.freezeMoney}>
              {getFieldDecorator('freezeMoney', {
                initialValue: `${findData.data.freezeMoney === undefined ? '0.0000000000' : findData.data.freezeMoney}`,
                rules: [{
                  max: 10,
                  message: '请正确填写冻结金额',
                }],
              })(
                <Input placeholder="请输入冻结金额"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.currencyLogo}>
              {getFieldDecorator('currencyLogo', {
                initialValue: `${findData.data.currencyLogo === undefined ? '' : findData.data.currencyLogo}`,
                rules: [{
                  max: 255,
                  message: '请正确填写币种logo',
                }],
              })(
                <Input placeholder="请输入币种logo"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.userId}>
              {getFieldDecorator('userId', {
                initialValue: `${findData.data.userId === undefined ? '' : findData.data.userId}`,
                rules: [{
                  required: true,
                  max: 64,
                  message: '请正确填写用户id',
                }],
              })(
                <Input placeholder="请输入用户id"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.userRealName}>
              {getFieldDecorator('userRealName', {
                initialValue: `${findData.data.userRealName === undefined ? '' : findData.data.userRealName}`,
                rules: [{
                  max: 64,
                  message: '请正确填写用户真实姓名',
                }],
              })(
                <Input placeholder="请输入用户真实姓名"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.userMobile}>
              {getFieldDecorator('userMobile', {
                initialValue: `${findData.data.userMobile === undefined ? '' : findData.data.userMobile}`,
                rules: [{
                  max: 64,
                  message: '请正确填写用户手机号',
                }],
              })(
                <Input placeholder="请输入用户手机号"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.status}>
              {getFieldDecorator('status', {
                initialValue: `${findData.data.status === undefined ? '0' : findData.data.status}`,
                rules: [{
                  max: 1,
                  message: '请正确填写状态',
                }],
              })(
                <Select placeholder="请选择状态">
                  <Option value="0">正常</Option>
                  <Option value="1">冻结</Option>
                </Select>
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
  submitting: loading.effects['finance/add'],
}))(Form.create()(FinanceForm));
