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
  coinCode: '代币Code',
  coinAddress: '合约地址',
  coinDecimals: '代币精度',
  block:'链类型',
};

@connect(({ercCoin}) => ({
  ercCoin,
}))
class ErcCoinForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('ercCoin/find', {id: match.params.id});
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
    const {ercCoin: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data = {};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          this.add('ercCoin/add', params, this.addCallBack);
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

            <FormItem {...formItemLayout} label={fieldLabels.block}>
              {getFieldDecorator('block', {
                initialValue: `${findData.data.block === undefined ? '' : findData.data.block}`,
                rules: [{
                  required: true,
                  max: 20,
                  message: '请正确填写区块链类型',
                }],
              })(
                <Input placeholder="请输入区块链类型"/>
              )}
            </FormItem>

            <FormItem {...formItemLayout} label={fieldLabels.coinCode}>
              {getFieldDecorator('coinCode', {
                initialValue: `${findData.data.coinCode === undefined ? '' : findData.data.coinCode}`,
                rules: [{
                  required: true,
                  max: 20,
                  message: '请正确填写代币Code',
                }],
              })(
                <Input placeholder="请输入代币Code"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.coinAddress}>
              {getFieldDecorator('coinAddress', {
                initialValue: `${findData.data.coinAddress === undefined ? '' : findData.data.coinAddress}`,
                rules: [{
                  required: true,
                  max: 255,
                  message: '请正确填写合约地址',
                }],
              })(
                <Input placeholder="请输入合约地址"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.coinDecimals}>
              {getFieldDecorator('coinDecimals', {
                initialValue: `${findData.data.coinDecimals === undefined ? '' : findData.data.coinDecimals}`,
                rules: [{
                  max: 10,
                  message: '请正确填写代币精度',
                }],
              })(
                <Select placeholder="请选择代币精度">
                  <Option value="ether">18</Option>
                  <Option value="kwei">3</Option>
                  <Option value="mwei">6</Option>
                  <Option value="qwei">9</Option>
                  <Option value="eight">8</Option>
                  <Option value="szabo">12</Option>
                  <Option value="finney">15</Option>
                  <Option value="kether">21</Option>
                  <Option value="mether">24</Option>
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
  submitting: loading.effects['ercCoin/add'],
}))(Form.create()(ErcCoinForm));
