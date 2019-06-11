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
            coinCode: '币代码',
            fromAddress: 'from',
            toAddress: 'to_address',
            amount: '交易金额',
            fee: '手续费',
            txHash: '区块链流水号',
            txType: '交易类型',
            blockNumber: 'block_number',
            transactionIndex: 'transaction_index',
            blockHash: 'block_hash',
            gas: 'gas',
            gasPrice: 'gas_price',
            consume: '是否确认',
};

@connect(({coinOrder}) => ({
coinOrder,
}))
class CoinOrderForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('coinOrder/find',{id: match.params.id});
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
    const {coinOrder: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          this.add('coinOrder/add',params,this.addCallBack);
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
                                <Form.Item label={fieldLabels.coinCode}>
                                    {getFieldDecorator('coinCode', {
                                        initialValue:`${findData.data.coinCode===undefined?'':findData.data.coinCode}`,
                                    rules: [{
                                        max:20,
                                    message: '请正确填写币代码',
                                    }],
                                    })(
                                    <Input placeholder="请输入币代码"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.fromAddress}>
                                    {getFieldDecorator('fromAddress', {
                                        initialValue:`${findData.data.fromAddress===undefined?'':findData.data.fromAddress}`,
                                    rules: [{
                                        required: true,
                                        max:255,
                                    message: '请正确填写from',
                                    }],
                                    })(
                                    <Input placeholder="请输入from"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.toAddress}>
                                    {getFieldDecorator('toAddress', {
                                        initialValue:`${findData.data.toAddress===undefined?'':findData.data.toAddress}`,
                                    rules: [{
                                        required: true,
                                        max:255,
                                    message: '请正确填写to_address',
                                    }],
                                    })(
                                    <Input placeholder="请输入to_address"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.amount}>
                                    {getFieldDecorator('amount', {
                                       initialValue:`${findData.data.amount===undefined?'0.00000000':findData.data.amount}`,
                                    rules: [{
                                        required: true,
                                        max:20,
                                    message: '请正确填写交易金额',
                                    }],
                                    })(
                                    <Input placeholder="请输入交易金额"/>
                                    )}
                                </Form.Item>
                             </Col>
                              </Row>
                             <Row gutter={16}>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.fee}>
                                    {getFieldDecorator('fee', {
                                       initialValue:`${findData.data.fee===undefined?'0.00000000':findData.data.fee}`,
                                    rules: [{
                                        required: true,
                                        max:20,
                                    message: '请正确填写手续费',
                                    }],
                                    })(
                                    <Input placeholder="请输入手续费"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.txHash}>
                                    {getFieldDecorator('txHash', {
                                        initialValue:`${findData.data.txHash===undefined?'':findData.data.txHash}`,
                                    rules: [{
                                        max:200,
                                    message: '请正确填写区块链流水号',
                                    }],
                                    })(
                                    <Input placeholder="请输入区块链流水号"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                 <Form.Item label={fieldLabels.txType}>
                                     {getFieldDecorator('txType', {
                                       initialValue:`${findData.data.txType===undefined?'0':findData.data.txType}`,
                                     rules: [{
                                        required: true,
                                        max:1,
                                        message: '请正确填写交易类型',
                                     }],
                                     })(
                                     <Select placeholder="请选择交易类型">
                                              <Option value="0" >系统外</Option>
                                              <Option value="1" >系统内</Option>
                                     </Select>
                                     )}
                                 </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.blockNumber}>
                                    {getFieldDecorator('blockNumber', {
                                        initialValue:`${findData.data.blockNumber===undefined?'':findData.data.blockNumber}`,
                                    rules: [{
                                        max:50,
                                    message: '请正确填写block_number',
                                    }],
                                    })(
                                    <Input placeholder="请输入block_number"/>
                                    )}
                                </Form.Item>
                             </Col>
                              </Row>
                             <Row gutter={16}>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.transactionIndex}>
                                    {getFieldDecorator('transactionIndex', {
                                        initialValue:`${findData.data.transactionIndex===undefined?'':findData.data.transactionIndex}`,
                                    rules: [{
                                        max:50,
                                    message: '请正确填写transaction_index',
                                    }],
                                    })(
                                    <Input placeholder="请输入transaction_index"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.blockHash}>
                                    {getFieldDecorator('blockHash', {
                                        initialValue:`${findData.data.blockHash===undefined?'':findData.data.blockHash}`,
                                    rules: [{
                                        max:200,
                                    message: '请正确填写block_hash',
                                    }],
                                    })(
                                    <Input placeholder="请输入block_hash"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.gas}>
                                    {getFieldDecorator('gas', {
                                        initialValue:`${findData.data.gas===undefined?'':findData.data.gas}`,
                                    rules: [{
                                        max:20,
                                    message: '请正确填写gas',
                                    }],
                                    })(
                                    <Input placeholder="请输入gas"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.gasPrice}>
                                    {getFieldDecorator('gasPrice', {
                                        initialValue:`${findData.data.gasPrice===undefined?'':findData.data.gasPrice}`,
                                    rules: [{
                                        max:20,
                                    message: '请正确填写gas_price',
                                    }],
                                    })(
                                    <Input placeholder="请输入gas_price"/>
                                    )}
                                </Form.Item>
                             </Col>
                              </Row>
                             <Row gutter={16}>
                                <Col lg={6} md={12} sm={24}>
                                 <Form.Item label={fieldLabels.consume}>
                                     {getFieldDecorator('consume', {
                                        initialValue:`${findData.data.consume===undefined?'':findData.data.consume}`,
                                     rules: [{
                                        max:2,
                                        message: '请正确填写是否确认',
                                     }],
                                     })(
                                     <Select placeholder="请选择是否确认">
                                              <Option value="0" >否</Option>
                                              <Option value="1" >是</Option>
                                     </Select>
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
                submitting: loading.effects['coinOrder/add'],
                }))(Form.create()(CoinOrderForm));
