import React from 'react';
import {Card, Button, Form, Icon, Col, Row, Input, Select, Popover,DatePicker,Radio} from 'antd';
import {connect} from 'dva';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import FooterToolbar from '../../components/FooterToolbar';
import styles from '../../assets/style/style.less';
import {Link} from 'dva/router';
import moment from 'moment';
import BaseForm from '../../components/BatSoft/Form';

const {Option} = Select;

const dateFormat = 'YYYY-MM-DD HH:mm:ss';
const { TextArea } = Input;
const fieldLabels = {
            accountId: '账户ID',
            userId: '用户id',
            userName: '用户名',
            coinCode: '币种',
            direction: '方向',
            operationType: '操作类型',
            opeationMoney: '订单金额',
            tradeTime: '交易时间',
            opeationState: '订单状态',
            transactionId: '资金转入方ID',
            traBankname: '银行名称',
            traBankcard: 'tra_bankcard',
            traNumber: '交易流水号',
            remark: '订单说明',
};

@connect(({c2cOrder}) => ({
c2cOrder,
}))
class C2cOrderForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('c2cOrder/find',{id: match.params.id});
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
    const {c2cOrder: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
            params.tradeTime  =params.tradeTime .format(dateFormat);
          this.add('c2cOrder/add',params,this.addCallBack);
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
                                <Form.Item label={fieldLabels.accountId}>
                                    {getFieldDecorator('accountId', {
                                       initialValue:`${findData.data.accountId===undefined?'':findData.data.accountId}`,
                                    rules: [{
                                        required: true,
                                        max:64,
                                    message: '请正确填写账户ID',
                                    }],
                                    })(
                                    <Input placeholder="请输入账户ID"/>
                                    )}
                                </Form.Item>
                             </Col>
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
                                        required: true,
                                        max:50,
                                    message: '请正确填写用户名',
                                    }],
                                    })(
                                    <Input placeholder="请输入用户名"/>
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
                                    message: '请正确填写币种',
                                    }],
                                    })(
                                    <Input placeholder="请输入币种"/>
                                    )}
                                </Form.Item>
                             </Col>
                              </Row>
                             <Row gutter={16}>
                                <Col lg={6} md={12} sm={24}>
                                 <Form.Item label={fieldLabels.direction}>
                                     {getFieldDecorator('direction', {
                                        initialValue:`${findData.data.direction===undefined?'':findData.data.direction}`,
                                     rules: [{
                                        max:1,
                                        message: '请正确填写方向',
                                     }],
                                     })(
                                     <Select placeholder="请选择方向">
                                              <Option value="0" >支出</Option>
                                              <Option value="1" >收入</Option>
                                     </Select>
                                     )}
                                 </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                 <Form.Item label={fieldLabels.operationType}>
                                     {getFieldDecorator('operationType', {
                                        initialValue:`${findData.data.operationType===undefined?'':findData.data.operationType}`,
                                     rules: [{
                                        max:5,
                                        message: '请正确填写操作类型',
                                     }],
                                     })(
                                     <Select placeholder="请选择操作类型">
                                              <Option value="1" >买入</Option>
                                              <Option value="2" >卖出</Option>
                                     </Select>
                                     )}
                                 </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.opeationMoney}>
                                    {getFieldDecorator('opeationMoney', {
                                       initialValue:`${findData.data.opeationMoney===undefined?'0.0000000000':findData.data.opeationMoney}`,
                                    rules: [{
                                        max:25,
                                    message: '请正确填写订单金额',
                                    }],
                                    })(
                                    <Input placeholder="请输入订单金额"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                 <Form.Item label={fieldLabels.tradeTime}>
                                     {getFieldDecorator('tradeTime', {
                                     initialValue:findData.data.tradeTime==null?'': moment(findData.data.tradeTime,dateFormat) ,
                                     rules: [{
                                     message: '请正确填写交易时间',
                                     }],
                                     })(
                                     <DatePicker format={dateFormat} placeholder="请输入交易时间"/>
                                     )}
                                 </Form.Item>
                             </Col>
                              </Row>
                             <Row gutter={16}>
                                <Col lg={6} md={12} sm={24}>
                                 <Form.Item label={fieldLabels.opeationState}>
                                     {getFieldDecorator('opeationState', {
                                       initialValue:`${findData.data.opeationState===undefined?'0':findData.data.opeationState}`,
                                     rules: [{
                                        max:1,
                                        message: '请正确填写订单状态',
                                     }],
                                     })(
                                     <Select placeholder="请选择订单状态">
                                              <Option value="0" >进行中</Option>
                                              <Option value="1" >已完成</Option>
                                              <Option value="2" >失败</Option>
                                     </Select>
                                     )}
                                 </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.transactionId}>
                                    {getFieldDecorator('transactionId', {
                                        initialValue:`${findData.data.transactionId===undefined?'':findData.data.transactionId}`,
                                    rules: [{
                                        max:64,
                                    message: '请正确填写资金转入方ID',
                                    }],
                                    })(
                                    <Input placeholder="请输入资金转入方ID"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.traBankname}>
                                    {getFieldDecorator('traBankname', {
                                        initialValue:`${findData.data.traBankname===undefined?'':findData.data.traBankname}`,
                                    rules: [{
                                        max:50,
                                    message: '请正确填写银行名称',
                                    }],
                                    })(
                                    <Input placeholder="请输入银行名称"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.traBankcard}>
                                    {getFieldDecorator('traBankcard', {
                                        initialValue:`${findData.data.traBankcard===undefined?'':findData.data.traBankcard}`,
                                    rules: [{
                                        max:50,
                                    message: '请正确填写tra_bankcard',
                                    }],
                                    })(
                                    <Input placeholder="请输入tra_bankcard"/>
                                    )}
                                </Form.Item>
                             </Col>
                              </Row>
                             <Row gutter={16}>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.traNumber}>
                                    {getFieldDecorator('traNumber', {
                                        initialValue:`${findData.data.traNumber===undefined?'':findData.data.traNumber}`,
                                    rules: [{
                                        max:50,
                                    message: '请正确填写交易流水号',
                                    }],
                                    })(
                                    <Input placeholder="请输入交易流水号"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.remark}>
                                    {getFieldDecorator('remark', {
                                       initialValue:`${findData.data.remark===undefined?'':findData.data.remark}`,
                                    rules: [{
                                        max:50,
                                    message: '请正确填写订单说明',
                                    }],
                                    })(
                                    <TextArea style={{ minHeight: 32 }} placeholder="订单说明" rows={4} />
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
                submitting: loading.effects['c2cOrder/add'],
                }))(Form.create()(C2cOrderForm));
