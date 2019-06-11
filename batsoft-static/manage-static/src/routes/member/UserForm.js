import React from 'react';
import {Card, Button, Form, Icon, Col, Row, Input, Select, Popover, DatePicker, Radio} from 'antd';
import {connect} from 'dva';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import FooterToolbar from '../../components/FooterToolbar';
import styles from '../../assets/style/style.less';
import {Link} from 'dva/router';
import moment from 'moment';
import BaseForm from '../../components/BatSoft/Form';

const {TextArea} = Input;
const {Option} = Select;

const dateFormat = 'YYYY-MM-DD HH:mm:ss';
const fieldLabels = {
  userNick: '用户昵称',
  userName: '用户名',
  password: '登录密码',
  salt: '密码盐',
  realName: '真实姓名',
  areaCode: '手机区域号',
  userMobile: '手机号',
  userEmail: '邮箱',
  status: '状态',
  userCardType: '证件类型',
  userCardNumber: '证件号',
  userBirthday: '生日',
  userAvatar: '头像',
  userQq: 'qq',
  loginNum: '登录次数',
  loginTime: '当前登录时间',
  oldLoginTime: '上次登录时间',
  loginIp: '当前登录ip',
  oldLoginIp: '上次登录ip',
  userCardFront: '证件证明照片',
  userCardBack: '证件反面照片',
  realState: '是否实名',
  tradePassword: '交易密码',
  googleCode: 'google 验证码',
  userCardAll: '手持证件照',
  openGoogleAuth: '开启google认证',
  trustNum: '被信任人数',
  exchangeNum: '交易次数',
  remark: '备注或简介',
  sex: '性别',
  country: '国家或地区',
};

@connect(({user}) => ({
  user,
}))
class UserForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('user/find', {id: match.params.id});
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
    const {user: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data = {};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          this.add('user/add', params, this.addCallBack);
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
          <Form layout="vertical" hideRequiredMark>
            <Form.Item label={fieldLabels.id}>
              {getFieldDecorator('id', {
                initialValue: findData.data.id,
              })(
                <Input style={{display: 'none'}}/>
              )}
            </Form.Item>
            <Row gutter={16}>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.userNick}>
                  {getFieldDecorator('userNick', {
                    initialValue: `${findData.data.userNick === undefined ? '' : findData.data.userNick}`,
                    rules: [{
                      max: 30,
                      message: '请正确填写用户昵称',
                    }],
                  })(
                    <Input placeholder="请输入用户昵称"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.userName}>
                  {getFieldDecorator('userName', {
                    initialValue: `${findData.data.userName === undefined ? '' : findData.data.userName}`,
                    rules: [{
                      required: true,
                      max: 50,
                      message: '请正确填写用户名',
                    }],
                  })(
                    <Input placeholder="请输入用户名"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.password}>
                  {getFieldDecorator('password', {
                    initialValue: `${findData.data.password === undefined ? '' : findData.data.password}`,
                    rules: [{
                      required: true,
                      max: 32,
                      message: '请正确填写登录密码',
                    }],
                  })(
                    <Input type={"password"} placeholder="请输入登录密码"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.tradePassword}>
                  {getFieldDecorator('tradePassword', {
                    initialValue: `${findData.data.tradePassword === undefined ? '' : findData.data.tradePassword}`,
                    rules: [{
                      max: 255,
                      message: '请正确填写交易密码',
                    }],
                  })(
                    <Input type={"password"} placeholder="请输入交易密码"/>
                  )}
                </Form.Item>
              </Col>
            </Row>
            <Row gutter={16}>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.realName}>
                  {getFieldDecorator('realName', {
                    initialValue: `${findData.data.realName === undefined ? '' : findData.data.realName}`,
                    rules: [{
                      max: 50,
                      message: '请正确填写真实姓名',
                    }],
                  })(
                    <Input placeholder="请输入真实姓名"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.areaCode}>
                  {getFieldDecorator('areaCode', {
                    initialValue: `${findData.data.areaCode === undefined ? '86' : findData.data.areaCode}`,
                    rules: [{
                      max: 10,
                      message: '请正确填写手机区域号',
                    }],
                  })(
                    <Input placeholder="请输入手机区域号"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.userMobile}>
                  {getFieldDecorator('userMobile', {
                    initialValue: `${findData.data.userMobile === undefined ? '' : findData.data.userMobile}`,
                    rules: [{
                      max: 15,
                      message: '请正确填写手机号',
                    }],
                  })(
                    <Input placeholder="请输入手机号"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.userEmail}>
                  {getFieldDecorator('userEmail', {
                    initialValue: `${findData.data.userEmail === undefined ? '' : findData.data.userEmail}`,
                    rules: [{
                      max: 20,
                      message: '请正确填写邮箱',
                    }],
                  })(
                    <Input placeholder="请输入邮箱"/>
                  )}
                </Form.Item>
              </Col>
            </Row>
            <Row gutter={16}>

              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.userCardType}>
                  {getFieldDecorator('userCardType', {
                    initialValue: `${findData.data.userCardType === undefined ? '' : findData.data.userCardType}`,
                    rules: [{
                      max: 64,
                      message: '请正确填写证件类型',
                    }],
                  })(
                    <Select placeholder="请选择证件类型">
                      <Option value="0">身份证</Option>
                      <Option value="1">护照</Option>
                      <Option value="2">其它</Option>
                    </Select>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.userCardNumber}>
                  {getFieldDecorator('userCardNumber', {
                    initialValue: `${findData.data.userCardNumber === undefined ? '' : findData.data.userCardNumber}`,
                    rules: [{
                      max: 30,
                      message: '请正确填写证件号',
                    }],
                  })(
                    <Input placeholder="请输入证件号"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.userBirthday}>
                  {getFieldDecorator('userBirthday', {
                    initialValue: `${findData.data.userBirthday === undefined ? '' : findData.data.userBirthday}`,
                    rules: [{
                      max: 13,
                      message: '请正确填写生日',
                    }],
                  })(
                    <Input placeholder="请输入生日"/>
                  )}
                </Form.Item>
              </Col>

              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.realState}>
                  {getFieldDecorator('realState', {
                    initialValue: `${findData.data.realState === undefined ? '0' : findData.data.realState}`,
                    rules: [{
                      max: 1,
                      message: '请正确填写是否实名',
                    }],
                  })(
                    <Select placeholder="请选择是否实名">
                      <Option value="0">未实名</Option>
                      <Option value="1">已实名</Option>
                    </Select>
                  )}
                </Form.Item>
              </Col>
            </Row>

            <Row gutter={16}>


              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.country}>
                  {getFieldDecorator('country', {
                    initialValue: `${findData.data.country === undefined ? '' : findData.data.country}`,
                    rules: [{
                      max: 30,
                      message: '请正确填写国家或地区',
                    }],
                  })(
                    <Input placeholder="请输入国家或地区"/>
                  )}
                </Form.Item>
              </Col>



              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.sex}>
                  {getFieldDecorator('sex', {
                    initialValue: `${findData.data.sex === undefined ? '0' : findData.data.sex}`,
                    rules: [{
                      max: 1,
                      message: '请正确填写性别',
                    }],
                  })(
                    <Select placeholder="请选择性别">
                      <Option value="0">男</Option>
                      <Option value="1">女</Option>
                    </Select>
                  )}
                </Form.Item>
              </Col>
            </Row>


            <Row gutter={16}>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.userAvatar}>
                  {getFieldDecorator('userAvatar', {
                    initialValue: `${findData.data.userAvatar === undefined ? '' : findData.data.userAvatar}`,
                    rules: [{
                      max: 255,
                      message: '请正确填写头像',
                    }],
                  })(
                    <Input placeholder="请输入头像"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.userCardFront}>
                  {getFieldDecorator('userCardFront', {
                    initialValue: `${findData.data.userCardFront === undefined ? '' : findData.data.userCardFront}`,
                    rules: [{
                      max: 255,
                      message: '请正确填写证件证明照片',
                    }],
                  })(
                    <Input placeholder="请输入证件证明照片"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.userCardBack}>
                  {getFieldDecorator('userCardBack', {
                    initialValue: `${findData.data.userCardBack === undefined ? '' : findData.data.userCardBack}`,
                    rules: [{
                      max: 255,
                      message: '请正确填写证件反面照片',
                    }],
                  })(
                    <Input placeholder="请输入证件反面照片"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.userCardAll}>
                  {getFieldDecorator('userCardAll', {
                    initialValue: `${findData.data.userCardAll === undefined ? '' : findData.data.userCardAll}`,
                    rules: [{
                      max: 255,
                      message: '请正确填写手持证件照',
                    }],
                  })(
                    <Input placeholder="请输入手持证件照"/>
                  )}
                </Form.Item>
              </Col>

            </Row>
            <Row gutter={16}>
              <Col lg={24} md={12} sm={24}>
                <Form.Item label={fieldLabels.remark}>
                  {getFieldDecorator('remark', {
                    initialValue: `${findData.data.remark === undefined ? '' : findData.data.remark}`,
                    rules: [{
                      max: 255,
                      message: '请正确填写备注或简介',
                    }],
                  })(
                    <TextArea style={{minHeight: 32}} placeholder="请输入备注或简介" rows={4}/>
                  )}
                </Form.Item>
              </Col>
            </Row>
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
  submitting: loading.effects['user/add'],
}))(Form.create()(UserForm));
