import React from 'react';
import {Card, Button, Form, Icon, Col, Row, Input, Select, Popover, DatePicker, Radio} from 'antd';
import {connect} from 'dva';
import PageHeaderLayout from '../../../layouts/PageHeaderLayout';
import FooterToolbar from '../../../components/FooterToolbar';
import styles from '../../../assets/style/style.less';
import {Link} from 'dva/router';
import BaseForm from '../../../components/BatSoft/Form';
import {md5} from "../../../utils/utils";

const {Option} = Select;
const FormItem = Form.Item;
const {TextArea} = Input;
const fieldLabels = {
  officeId: '工号',
  userName: '用户名',
  password: '登录密码',
  salt: '密码盐',
  realName: '真实姓名',
  mobile: '手机号',
  phone: '固定电话',
  email: '邮箱',
  superUser: '是否超级管理员',
  available: '是否禁用',
  remark: '备注',
  rolesIds: '角色',
};

@connect(({appUser, appRole}) => ({
  appUser,
  appRole,
}))
class AppUserForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    this.find('appRole/findRoles');
    if (match.params.id !== undefined) {
      this.setState({
        formType: "update",
      })
      this.find('appUser/find', {id: match.params.id});
    }
  }

  /**
   * 增加、修改回调函数
   * @param data
   */
  addCallBack = data => {
    const {form} = this.props;
    form.resetFields();
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
    const {appUser: {findData}, appRole: {roleData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data = {};
    }
    debugger
    const children = [];
    roleData.forEach((item) => {
      children.push(<Option key={item.id}>{item.name}</Option>);
    });

    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          params.password = md5(params.password);
          this.add('appUser/add', params, this.addCallBack);
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

            <FormItem {...formItemLayout} label="请选择角色">
              {getFieldDecorator('rolesIds', {
                initialValue: findData.data.rolesIds || [],
                rules: [{
                  required: true,
                  message: '请选择角色',
                }],
              })(
                <Select
                  mode="multiple"
                  placeholder="Please select"
                  //onChange={handleChange}
                >
                  {children}
                </Select>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.officeId}>
              {getFieldDecorator('officeId', {
                initialValue: findData.data.officeId,
                rules: [{
                  required: true,
                  max: 100,
                  message: '请正确填写工号',
                }],
              })(
                <Input placeholder="请输入工号"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.userName}>
              {getFieldDecorator('userName', {
                initialValue: findData.data.userName,
                rules: [{
                  required: true,
                  max: 50,
                  message: '请正确填写用户名',
                }],
              })(
                <Input placeholder="请输入用户名"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.password}>
              {getFieldDecorator('password', {
                initialValue: findData.data.password,
                rules: [{
                  required: true,
                  max: 32,
                  message: '请正确填写登录密码',
                }],
              })(
                <Input type="password" placeholder="请输入登录密码"/>
              )}
            </FormItem>

            <FormItem {...formItemLayout} label={fieldLabels.realName}>
              {getFieldDecorator('realName', {
                initialValue: findData.data.realName,
                rules: [{
                  required: true,
                  max: 50,
                  message: '请正确填写真实姓名',
                }],
              })(
                <Input placeholder="请输入真实姓名"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.mobile}>
              {getFieldDecorator('mobile', {
                initialValue: findData.data.mobile,
                rules: [{
                  max: 15,
                  message: '请正确填写手机号',
                }],
              })(
                <Input placeholder="请输入手机号"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.phone}>
              {getFieldDecorator('phone', {
                initialValue: findData.data.phone,
                rules: [{
                  max: 20,
                  message: '请正确填写固定电话',
                }],
              })(
                <Input placeholder="请输入固定电话"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.email}>
              {getFieldDecorator('email', {
                initialValue: findData.data.email,
                rules: [{
                  max: 20,
                  message: '请正确填写邮箱',
                }],
              })(
                <Input placeholder="请输入邮箱"/>
              )}
            </FormItem>


            <FormItem  {...formItemLayout} label={fieldLabels.superUser}>
              {getFieldDecorator('superUser', {
                initialValue: `${findData.data.superUser === undefined ? '0' : findData.data.superUser}`,
                rules: [{
                  required: true,
                  max: 1,
                  message: '请正确填写是否超级管理员',
                }],
              })(
                <Radio.Group>
                  <Radio value="0">否</Radio>
                  <Radio value="1">是</Radio>
                </Radio.Group>
              )}
            </FormItem>


            <FormItem  {...formItemLayout} label={fieldLabels.available}>
              {getFieldDecorator('available', {
                initialValue: `${findData.data.available === undefined ? '0' : findData.data.available}`,
                rules: [{
                  required: true,
                  max: 1,
                  message: '请正确填写是否禁用',
                }],
              })(
                <Radio.Group>
                  <Radio value="0">否</Radio>
                  <Radio value="1">是</Radio>
                </Radio.Group>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.remark}>
              {getFieldDecorator('remark', {
                initialValue: findData.data.remark,
                rules: [{
                  max: 255,
                  message: '请正确填写备注',
                }],
              })(
                <TextArea style={{minHeight: 32}} placeholder="备注" rows={4}/>
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
  submitting: loading.effects['appUser/add'],
}))(Form.create()(AppUserForm));
