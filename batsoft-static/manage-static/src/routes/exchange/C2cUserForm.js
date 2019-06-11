import React from 'react';
import {Card, Button, Form, Icon, Col, Row, Input, Select, Popover,DatePicker,TreeSelect,Radio} from 'antd';
import {connect} from 'dva';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import FooterToolbar from '../../components/FooterToolbar';
import styles from '../../assets/style/style.less';
import {Link} from 'dva/router';
import BaseForm from '../../components/BatSoft/Form';

const {Option} = Select;

const fieldLabels = {
            userName: '商户号',
            trueName: '银行开户名',
            telephone: '手机号码',
            userCard: '商户证件号',
            bankId: '银行名称',
            // bankName: '银行名称',
            branchName: '支行名称',
            cardNumber: '银行卡号',
            coinName: '币种名称',
            coinCode: '币种代码',
            coinId: '币种ID',
            coinNumber: '保证金',
            coinProportion: '对人民币折算比例',
            type:'类型',
            status: '是否可用',
            remark: '备注',
            sort: '排序',
};

@connect(({c2cUser}) => ({
c2cUser,
}))
@connect(({appDictionary}) => ({
  appDictionary,
}))
class C2cUserForm extends BaseForm {
  state = {
    formType: 'add',
  };

  // 下拉树选择
  onChange = (value,label) => {
    this.setState({
      dicSelectValue:label[0],
    });
  }

  // 初始化方法
  componentDidMount() {
    const {match} = this.props;
    //加载数据字典数据
    this.find('appDictionary/findByKey',{key:"root_bank"});
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('c2cUser/find',{id: match.params.id});
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
    const {c2cUser: {findData},appDictionary: {dicTreeData}, form, dispatch, submitting} = this.props;
    if(dicTreeData===undefined) dicTreeData=[];
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          params.bankName=this.state.dicSelectValue;
          this.add('c2cUser/add',params,this.addCallBack);
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
                                <Form.Item label={fieldLabels.userName}>
                                    {getFieldDecorator('userName', {
                                        initialValue:`${findData.data.userName===undefined?'':findData.data.userName}`,
                                    rules: [{
                                        required: true,
                                        max:50,
                                    message: '请正确填写商户号',
                                    }],
                                    })(
                                    <Input placeholder="请输入商户号"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.trueName}>
                                    {getFieldDecorator('trueName', {
                                        initialValue:`${findData.data.trueName===undefined?'':findData.data.trueName}`,
                                    rules: [{
                                        max:255,
                                    message: '请正确填写银行开户名',
                                    }],
                                    })(
                                    <Input placeholder="请输入银行开户名"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.telephone}>
                                    {getFieldDecorator('telephone', {
                                        initialValue:`${findData.data.telephone===undefined?'':findData.data.telephone}`,
                                    rules: [{
                                        max:30,
                                    message: '请正确填写手机号码',
                                    }],
                                    })(
                                    <Input placeholder="请输入手机号码"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.userCard}>
                                    {getFieldDecorator('userCard', {
                                        initialValue:`${findData.data.userCard===undefined?'':findData.data.userCard}`,
                                    rules: [{
                                        max:50,
                                    message: '请正确填写商户证件号',
                                    }],
                                    })(
                                    <Input placeholder="请输入商户证件号"/>
                                    )}
                                </Form.Item>
                             </Col>
                              </Row>
                             <Row gutter={16}>
                                <Col lg={6} md={12} sm={24}>

                                  <Form.Item label={fieldLabels.bankId}>

                                    {getFieldDecorator('bankId', {
                                      initialValue: `${findData.data.bankId === undefined ? 'Please select' : findData.data.bankId}`,
                                      rules: [{
                                        required: true,
                                        message: '请选择银行',
                                      }],
                                    })(
                                      <TreeSelect
                                        //  value={this.state.dicSelectValue}
                                        dropdownStyle={{ maxHeight: 400, overflow: 'auto' }}
                                        treeData={Array.from(dicTreeData)}
                                        placeholder="Please select"
                                        onChange={this.onChange}
                                      />
                                    )}

                                  </Form.Item>
                             </Col>

                                <Col lg={12} md={12} sm={24}>
                                <Form.Item label={fieldLabels.branchName}>
                                    {getFieldDecorator('branchName', {
                                        initialValue:`${findData.data.branchName===undefined?'':findData.data.branchName}`,
                                    rules: [{
                                        max:255,
                                    message: '请正确填写支行名称',
                                    }],
                                    })(
                                    <Input placeholder="请输入支行名称"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.cardNumber}>
                                    {getFieldDecorator('cardNumber', {
                                        initialValue:`${findData.data.cardNumber===undefined?'':findData.data.cardNumber}`,
                                    rules: [{
                                        required: true,
                                        max:50,
                                    message: '请正确填写银行卡号',
                                    }],
                                    })(
                                    <Input placeholder="请输入银行卡号"/>
                                    )}
                                </Form.Item>
                             </Col>
                              </Row>
                             <Row gutter={16}>
                               <Col lg={6} md={12} sm={24}>
                                 <Form.Item label={fieldLabels.type}>
                                   {getFieldDecorator('type', {
                                     initialValue:`${findData.data.type===undefined?'':findData.data.type}`,
                                     rules: [{
                                       max:5,
                                       message: '请正确填写类型',
                                     }],
                                   })(
                                     <Select placeholder="请选择类型">
                                       <Option value="A" >A类</Option>
                                       <Option value="B" >B类</Option>
                                       <Option value="C" >C类</Option>
                                     </Select>
                                   )}
                                 </Form.Item>
                               </Col>
                                {/*<Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.coinName}>
                                    {getFieldDecorator('coinName', {
                                        initialValue:`${findData.data.coinName===undefined?'':findData.data.coinName}`,
                                    rules: [{
                                        required: true,
                                        max:30,
                                    message: '请正确填写币种名称',
                                    }],
                                    })(
                                    <Input placeholder="请输入币种名称"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.coinCode}>
                                    {getFieldDecorator('coinCode', {
                                        initialValue:`${findData.data.coinCode===undefined?'':findData.data.coinCode}`,
                                    rules: [{
                                        required: true,
                                        max:30,
                                    message: '请正确填写币种代码',
                                    }],
                                    })(
                                    <Input placeholder="请输入币种代码"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.coinId}>
                                    {getFieldDecorator('coinId', {
                                        initialValue:`${findData.data.coinId===undefined?'':findData.data.coinId}`,
                                    rules: [{
                                        max:64,
                                    message: '请正确填写币种ID',
                                    }],
                                    })(
                                    <Input placeholder="请输入币种ID"/>
                                    )}
                                </Form.Item>
                             </Col>*/}
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.coinNumber}>
                                    {getFieldDecorator('coinNumber', {
                                        initialValue:`${findData.data.coinNumber===undefined?'':findData.data.coinNumber}`,
                                    rules: [{
                                        required: true,
                                        max:10,
                                    message: '请正确填写保证金',
                                    }],
                                    })(
                                    <Input placeholder="请输入商户缴纳保证金"/>
                                    )}
                                </Form.Item>
                             </Col>
                              </Row>
                             <Row gutter={16}>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.coinProportion}>
                                    {getFieldDecorator('coinProportion', {
                                       initialValue:`${findData.data.coinProportion===undefined?'1.00000000':findData.data.coinProportion}`,
                                    rules: [{
                                        max:10,
                                    message: '请正确填写对人民币折算比例',
                                    }],
                                    })(
                                    <Input placeholder="请输入对人民币折算比例"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                 <Form.Item label={fieldLabels.status}>
                                     {getFieldDecorator('status', {
                                        initialValue:`${findData.data.status===undefined?'':findData.data.status}`,
                                     rules: [{
                                        max:5,
                                        message: '请正确填写是否可用',
                                     }],
                                     })(
                                     <Select placeholder="请选择是否可用">
                                              <Option value="0" >否</Option>
                                              <Option value="1" >是</Option>
                                     </Select>
                                     )}
                                 </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.remark}>
                                    {getFieldDecorator('remark', {
                                        initialValue:`${findData.data.remark===undefined?'':findData.data.remark}`,
                                    rules: [{
                                        max:255,
                                    message: '请正确填写备注',
                                    }],
                                    })(
                                    <Input placeholder="请输入备注"/>
                                    )}
                                </Form.Item>
                             </Col>
                                <Col lg={6} md={12} sm={24}>
                                <Form.Item label={fieldLabels.sort}>
                                    {getFieldDecorator('sort', {
                                        initialValue:`${findData.data.sort===undefined?'':findData.data.sort}`,
                                    rules: [{
                                        max:5,
                                    message: '请正确填写排序',
                                    }],
                                    })(
                                    <Input placeholder="请输入排序"/>
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
                submitting: loading.effects['c2cUser/add'],
                }))(Form.create()(C2cUserForm));
