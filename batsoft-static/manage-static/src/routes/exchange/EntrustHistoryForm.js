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
            accountId: '账户id',
            customerId: '客户id',
            coinCode: '币代码',
            entrustPrice: '委托价格',
            entrustAmout: '委托数量',
            entrustTime: '委托时间',
            entrustState: '委托状态',
            tradedCoins: '已成交',
            totalMoney: '成交总额',
            tradedAvg: '成交均价',
            category: '委托类别',
            orderFrom: '来源',
            remark: '订单说明',
};

@connect(({entrustHistory}) => ({
entrustHistory,
}))
class EntrustHistoryForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('entrustHistory/find',{id: match.params.id});
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
    const {entrustHistory: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
            params.entrustTime  =params.entrustTime .format(dateFormat);
          this.add('entrustHistory/add',params,this.addCallBack);
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
                                        max:50,
                                    message: '请正确填写账户id',
                                    }],
                                    })(
                                    <Input placeholder="请输入账户id"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.customerId}>
                                    {getFieldDecorator('customerId', {
                                       initialValue:`${findData.data.customerId===undefined?'':findData.data.customerId}`,
                                    rules: [{
                                        max:50,
                                    message: '请正确填写客户id',
                                    }],
                                    })(
                                    <Input placeholder="请输入客户id"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.coinCode}>
                                    {getFieldDecorator('coinCode', {
                                       initialValue:`${findData.data.coinCode===undefined?'':findData.data.coinCode}`,
                                    rules: [{
                                        max:50,
                                    message: '请正确填写币代码',
                                    }],
                                    })(
                                    <Input placeholder="请输入币代码"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.entrustPrice}>
                                    {getFieldDecorator('entrustPrice', {
                                       initialValue:`${findData.data.entrustPrice===undefined?'0.0000000000':findData.data.entrustPrice}`,
                                    rules: [{
                                        max:25,
                                    message: '请正确填写委托价格',
                                    }],
                                    })(
                                    <Input placeholder="请输入委托价格"/>
                                    )}
                                </Form.Item>
                             </Col>
                              </Row>
                             <Row gutter={16}>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.entrustAmout}>
                                    {getFieldDecorator('entrustAmout', {
                                       initialValue:`${findData.data.entrustAmout===undefined?'0.0000000000':findData.data.entrustAmout}`,
                                    rules: [{
                                        max:25,
                                    message: '请正确填写委托数量',
                                    }],
                                    })(
                                    <Input placeholder="请输入委托数量"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                 <Form.Item label={fieldLabels.entrustTime}>
                                     {getFieldDecorator('entrustTime', {
                                     initialValue:findData.data.entrustTime==null?'': moment(findData.data.entrustTime,dateFormat) ,
                                     rules: [{
                                     message: '请正确填写委托时间',
                                     }],
                                     })(
                                     <DatePicker format={dateFormat} placeholder="请输入委托时间"/>
                                     )}
                                 </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                 <Form.Item label={fieldLabels.entrustState}>
                                     {getFieldDecorator('entrustState', {
                                       initialValue:`${findData.data.entrustState===undefined?'0':findData.data.entrustState}`,
                                     rules: [{
                                        max:1,
                                        message: '请正确填写委托状态',
                                     }],
                                     })(
                                     <Select placeholder="请选择委托状态">
                                              <Option value="0" >未成交</Option>
                                              <Option value="1" >部分成交</Option>
                                              <Option value="2" >全部成交</Option>
                                              <Option value="3" >撤销</Option>
                                     </Select>
                                     )}
                                 </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.tradedCoins}>
                                    {getFieldDecorator('tradedCoins', {
                                       initialValue:`${findData.data.tradedCoins===undefined?'0.00000000':findData.data.tradedCoins}`,
                                    rules: [{
                                        max:20,
                                    message: '请正确填写已成交',
                                    }],
                                    })(
                                    <Input placeholder="请输入已成交"/>
                                    )}
                                </Form.Item>
                             </Col>
                              </Row>
                             <Row gutter={16}>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.totalMoney}>
                                    {getFieldDecorator('totalMoney', {
                                       initialValue:`${findData.data.totalMoney===undefined?'0.00000000':findData.data.totalMoney}`,
                                    rules: [{
                                        max:20,
                                    message: '请正确填写成交总额',
                                    }],
                                    })(
                                    <Input placeholder="请输入成交总额"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.tradedAvg}>
                                    {getFieldDecorator('tradedAvg', {
                                       initialValue:`${findData.data.tradedAvg===undefined?'0.00000000':findData.data.tradedAvg}`,
                                    rules: [{
                                        max:20,
                                    message: '请正确填写成交均价',
                                    }],
                                    })(
                                    <Input placeholder="请输入成交均价"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                 <Form.Item label={fieldLabels.category}>
                                     {getFieldDecorator('category', {
                                       initialValue:`${findData.data.category===undefined?'0':findData.data.category}`,
                                     rules: [{
                                        required: true,
                                        max:5,
                                        message: '请正确填写委托类别',
                                     }],
                                     })(
                                     <Select placeholder="请选择委托类别">
                                              <Option value="0" >限价委托</Option>
                                              <Option value="1" >计划委托</Option>
                                     </Select>
                                     )}
                                 </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                 <Form.Item label={fieldLabels.orderFrom}>
                                     {getFieldDecorator('orderFrom', {
                                        initialValue:`${findData.data.orderFrom===undefined?'':findData.data.orderFrom}`,
                                     rules: [{
                                        max:1,
                                        message: '请正确填写来源',
                                     }],
                                     })(
                                     <Select placeholder="请选择来源">
                                              <Option value="0" >网页</Option>
                                              <Option value="1" >移动端</Option>
                                     </Select>
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
                submitting: loading.effects['entrustHistory/add'],
                }))(Form.create()(EntrustHistoryForm));
