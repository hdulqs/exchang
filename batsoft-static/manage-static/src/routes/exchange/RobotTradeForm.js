import React from 'react';
import {Card, Button, Form, Icon, Col, Row, Input, Select, Popover,DatePicker,Radio} from 'antd';
import {connect} from 'dva';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import FooterToolbar from '../../components/FooterToolbar';
import styles from '../../assets/style/style.less';
import {Link} from 'dva/router';
import BaseForm from '../../components/BatSoft/Form';

const {Option} = Select;

const fieldLabels = {
            coinPair: '交易对',
            userName: '下单用户',
            entrustNumMax: '最大下单数量',
            entrustNumMin: '最小下单数量',
            entrustTimeMax: '下单时间最大',
            entrustTimeMin: '下单时间最小',
            entrustPriceMax: '最大下单价格',
            entrustPriceMin: '最小下单价格',
            basePrice: '步长',
            fromThird: '是否三方数据',
            thirdApi: 'api地址',
            status: '运行状态',
};

@connect(({robotTrade}) => ({
robotTrade,
}))
class RobotTradeForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('robotTrade/find',{id: match.params.id});
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
    const {robotTrade: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          this.add('robotTrade/add',params,this.addCallBack);
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
                                <Form.Item label={fieldLabels.coinPair}>
                                    {getFieldDecorator('coinPair', {
                                        initialValue:`${findData.data.coinPair===undefined?'':findData.data.coinPair}`,
                                    rules: [{
                                        required: true,
                                        max:100,
                                    message: '请正确填写交易对',
                                    }],
                                    })(
                                    <Input placeholder="请输入交易对"/>
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
                                    message: '请正确填写下单用户',
                                    }],
                                    })(
                                    <Input placeholder="请输入下单用户"/>
                                    )}
                                </Form.Item>
                             </Col>
                               <Col lg={6} md={12} sm={24}>
                                 <Form.Item label={fieldLabels.status}>
                                   {getFieldDecorator('status', {
                                     initialValue:`${findData.data.status===undefined?'0':findData.data.status}`,
                                     rules: [{
                                       max:1,
                                       message: '请正确填写运行状态',
                                     }],
                                   })(
                                     <Select placeholder="请选择运行状态">
                                       <Option value="0" >停止</Option>
                                       <Option value="1" >启动</Option>
                                     </Select>
                                   )}
                                 </Form.Item>
                               </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.entrustNumMax}>
                                    {getFieldDecorator('entrustNumMax', {
                                       initialValue:`${findData.data.entrustNumMax===undefined?'10.00000000':findData.data.entrustNumMax}`,
                                    rules: [{
                                        max:20,
                                    message: '请正确填写最大下单数量',
                                    }],
                                    })(
                                    <Input placeholder="请输入最大下单数量"/>
                                    )}
                                </Form.Item>
                             </Col>
                              </Row>
                             <Row gutter={16}>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.entrustNumMin}>
                                    {getFieldDecorator('entrustNumMin', {
                                       initialValue:`${findData.data.entrustNumMin===undefined?'1.00000000':findData.data.entrustNumMin}`,
                                    rules: [{
                                        max:20,
                                    message: '请正确填写最小下单数量',
                                    }],
                                    })(
                                    <Input placeholder="请输入最小下单数量"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.entrustTimeMax}>
                                    {getFieldDecorator('entrustTimeMax', {
                                       initialValue:`${findData.data.entrustTimeMax===undefined?'10':findData.data.entrustTimeMax}`,
                                    rules: [{
                                        max:10,
                                    message: '请正确填写下单时间最大',
                                    }],
                                    })(
                                    <Input placeholder="请输入下单时间最大"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.entrustTimeMin}>
                                    {getFieldDecorator('entrustTimeMin', {
                                       initialValue:`${findData.data.entrustTimeMin===undefined?'1':findData.data.entrustTimeMin}`,
                                    rules: [{
                                        max:10,
                                    message: '请正确填写下单时间最小',
                                    }],
                                    })(
                                    <Input placeholder="请输入下单时间最小"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.entrustPriceMax}>
                                    {getFieldDecorator('entrustPriceMax', {
                                       initialValue:`${findData.data.entrustPriceMax===undefined?'10.00000000':findData.data.entrustPriceMax}`,
                                    rules: [{
                                        max:20,
                                    message: '请正确填写最大下单价格',
                                    }],
                                    })(
                                    <Input placeholder="请输入最大下单价格"/>
                                    )}
                                </Form.Item>
                             </Col>
                              </Row>
                             <Row gutter={16}>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.entrustPriceMin}>
                                    {getFieldDecorator('entrustPriceMin', {
                                       initialValue:`${findData.data.entrustPriceMin===undefined?'1.00000000':findData.data.entrustPriceMin}`,
                                    rules: [{
                                        max:20,
                                    message: '请正确填写最小下单价格',
                                    }],
                                    })(
                                    <Input placeholder="请输入最小下单价格"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.basePrice}>
                                    {getFieldDecorator('basePrice', {
                                       initialValue:`${findData.data.basePrice===undefined?'1.00000000':findData.data.basePrice}`,
                                    rules: [{
                                        max:20,
                                    message: '请正确填写基础价格',
                                    }],
                                    })(
                                    <Input placeholder="请输入基础价格"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                 <Form.Item label={fieldLabels.fromThird}>
                                     {getFieldDecorator('fromThird', {
                                       initialValue:`${findData.data.fromThird===undefined?'0':findData.data.fromThird}`,
                                     rules: [{
                                        max:1,
                                        message: '请正确填写是否三方数据',
                                     }],
                                     })(
                                     <Select placeholder="请选择是否三方数据">
                                              <Option value="0" >否</Option>
                                              <Option value="1" >是</Option>
                                     </Select>
                                     )}
                                 </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.thirdApi}>
                                    {getFieldDecorator('thirdApi', {
                                        initialValue:`${findData.data.thirdApi===undefined?'':findData.data.thirdApi}`,
                                    rules: [{
                                        max:100,
                                    message: '请正确填写api地址',
                                    }],
                                    })(
                                    <Input placeholder="请输入api地址"/>
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
                submitting: loading.effects['robotTrade/add'],
                }))(Form.create()(RobotTradeForm));
