import React from 'react';
import {Card, Button, Form, Icon, Col, Row, Input, Select, Popover,DatePicker,Radio} from 'antd';
import {connect} from 'dva';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import FooterToolbar from '../../components/FooterToolbar';
import styles from '../../assets/style/style.less';
import {Link} from 'dva/router';
import BaseForm from '../../components/BatSoft/Form';

const {Option} = Select;

const { TextArea } = Input;
const fieldLabels = {
            toAddress: '提币地址',
            coinCode: '币种代码',
            memo: 'memo币地址',
            coinCount: '提币数量',
            actualCount: '实际到账',
            fee: '手续费',
            status: '状态',
            txOrder: '交易单号',
            userId: '用户id',
            userName: '用户名',
            userMobile: '用户手机号',
            userEmail: '用户邮箱',
            remark: '备注',
};

@connect(({coinWithdraw}) => ({
coinWithdraw,
}))
class CoinWithdrawForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('coinWithdraw/find',{id: match.params.id});
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
    const {coinWithdraw: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          this.add('coinWithdraw/add',params,this.addCallBack);
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
                                <Form.Item label={fieldLabels.toAddress}>
                                    {getFieldDecorator('toAddress', {
                                        initialValue:`${findData.data.toAddress===undefined?'':findData.data.toAddress}`,
                                    rules: [{
                                        required: true,
                                        max:255,
                                    message: '请正确填写提币地址',
                                    }],
                                    })(
                                    <Input placeholder="请输入提币地址"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.coinCode}>
                                    {getFieldDecorator('coinCode', {
                                        initialValue:`${findData.data.coinCode===undefined?'':findData.data.coinCode}`,
                                    rules: [{
                                        required: true,
                                        max:50,
                                    message: '请正确填写币种代码',
                                    }],
                                    })(
                                    <Input placeholder="请输入币种代码"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.memo}>
                                    {getFieldDecorator('memo', {
                                        initialValue:`${findData.data.memo===undefined?'':findData.data.memo}`,
                                    rules: [{
                                        max:255,
                                    message: '请正确填写memo币地址',
                                    }],
                                    })(
                                    <Input placeholder="请输入memo币地址"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.coinCount}>
                                    {getFieldDecorator('coinCount', {
                                       initialValue:`${findData.data.coinCount===undefined?'0.00000000':findData.data.coinCount}`,
                                    rules: [{
                                        max:10,
                                    message: '请正确填写提币数量',
                                    }],
                                    })(
                                    <Input placeholder="请输入提币数量"/>
                                    )}
                                </Form.Item>
                             </Col>
                              </Row>
                             <Row gutter={16}>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.actualCount}>
                                    {getFieldDecorator('actualCount', {
                                        initialValue:`${findData.data.actualCount===undefined?'':findData.data.actualCount}`,
                                    rules: [{
                                        max:10,
                                    message: '请正确填写实际到账',
                                    }],
                                    })(
                                    <Input placeholder="请输入实际到账"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.fee}>
                                    {getFieldDecorator('fee', {
                                        initialValue:`${findData.data.fee===undefined?'':findData.data.fee}`,
                                    rules: [{
                                        max:10,
                                    message: '请正确填写手续费',
                                    }],
                                    })(
                                    <Input placeholder="请输入手续费"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                 <Form.Item label={fieldLabels.status}>
                                     {getFieldDecorator('status', {
                                       initialValue:`${findData.data.status===undefined?'1':findData.data.status}`,
                                     rules: [{
                                        max:2,
                                        message: '请正确填写状态',
                                     }],
                                     })(
                                     <Select placeholder="请选择状态">
                                              <Option value="0" >等待中</Option>
                                              <Option value="1" >成功</Option>
                                              <Option value="2" >失败</Option>
                                     </Select>
                                     )}
                                 </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.txOrder}>
                                    {getFieldDecorator('txOrder', {
                                        initialValue:`${findData.data.txOrder===undefined?'':findData.data.txOrder}`,
                                    rules: [{
                                        required: true,
                                        max:150,
                                    message: '请正确填写交易单号',
                                    }],
                                    })(
                                    <Input placeholder="请输入交易单号"/>
                                    )}
                                </Form.Item>
                             </Col>
                              </Row>
                             <Row gutter={16}>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.userId}>
                                    {getFieldDecorator('userId', {
                                        initialValue:`${findData.data.userId===undefined?'':findData.data.userId}`,
                                    rules: [{
                                        required: true,
                                        max:64,
                                    message: '请正确填写用户id',
                                    }],
                                    })(
                                    <Input placeholder="请输入用户id"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.userName}>
                                    {getFieldDecorator('userName', {
                                        initialValue:`${findData.data.userName===undefined?'':findData.data.userName}`,
                                    rules: [{
                                        max:50,
                                    message: '请正确填写用户名',
                                    }],
                                    })(
                                    <Input placeholder="请输入用户名"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.userMobile}>
                                    {getFieldDecorator('userMobile', {
                                        initialValue:`${findData.data.userMobile===undefined?'':findData.data.userMobile}`,
                                    rules: [{
                                        max:20,
                                    message: '请正确填写用户手机号',
                                    }],
                                    })(
                                    <Input placeholder="请输入用户手机号"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.userEmail}>
                                    {getFieldDecorator('userEmail', {
                                        initialValue:`${findData.data.userEmail===undefined?'':findData.data.userEmail}`,
                                    rules: [{
                                        max:255,
                                    message: '请正确填写用户邮箱',
                                    }],
                                    })(
                                    <Input placeholder="请输入用户邮箱"/>
                                    )}
                                </Form.Item>
                             </Col>
                              </Row>
                             <Row gutter={16}>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.remark}>
                                    {getFieldDecorator('remark', {
                                        initialValue:`${findData.data.remark===undefined?'':findData.data.remark}`,
                                    rules: [{
                                        max:255,
                                    message: '请正确填写备注',
                                    }],
                                    })(
                                    <TextArea style={{ minHeight: 32 }} placeholder="备注" rows={4} />
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
                submitting: loading.effects['coinWithdraw/add'],
                }))(Form.create()(CoinWithdrawForm));
