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
  walletType: '钱包类型',
  coinCode: '币代码',
  walletAddress: '提币地址',
  walletAmount: '币数量',
  fee: '旷工费',
  lockPassword: '解锁密码',
  memo: 'memo标签',
  gasPrice:'gasPrice',
  gasLimit:'gasLimit',
};

@connect(({depositWallet}) => ({
  depositWallet,
}))
class DepositWalletForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('depositWallet/find', {id: match.params.id});
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
    const {depositWallet: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data = {};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          this.add('depositWallet/add', params, this.addCallBack);
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
            <FormItem {...formItemLayout} label={fieldLabels.walletType}>
              {getFieldDecorator('walletType', {
                initialValue: `${findData.data.walletType === undefined ? '' : findData.data.walletType}`,
                rules: [{
                  max: 50,
                  message: '请正确填写钱包类型',
                }],
              })(
                <Input placeholder="请输入钱包类型"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.coinCode}>
              {getFieldDecorator('coinCode', {
                initialValue: `${findData.data.coinCode === undefined ? '' : findData.data.coinCode}`,
                rules: [{
                  max: 50,
                  message: '请正确填写币代码',
                }],
              })(
                <Input placeholder="请输入币代码"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.walletAddress}>
              {getFieldDecorator('walletAddress', {
                initialValue: `${findData.data.walletAddress === undefined ? '' : findData.data.walletAddress}`,
                rules: [{
                  required: true,
                  max: 255,
                  message: '请正确填写提币地址',
                }],
              })(
                <Input placeholder="请输入提币地址"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.walletAmount}>
              {getFieldDecorator('walletAmount', {
                initialValue: `${findData.data.walletAmount === undefined ? '' : findData.data.walletAmount}`,
                rules: [{
                  max: 30,
                  message: '请正确填写币数量',
                }],
              })(
                <Input placeholder="请输入币数量"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.fee}>
              {getFieldDecorator('fee', {
                initialValue: `${findData.data.fee === undefined ? '' : findData.data.fee}`,
                rules: [{
                  required: true,
                  max: 30,
                  message: '请正确填写旷工费',
                }],
              })(
                <Input placeholder="请输入旷工费"/>
              )}
            </FormItem>

            <FormItem {...formItemLayout} label={fieldLabels.gasPrice}>
              {getFieldDecorator('gasPrice', {
                initialValue: `${findData.data.gasPrice === undefined ? '' : findData.data.gasPrice}`,
                rules: [{
                  required: true,
                  max: 30,
                  message: '请正确填写ETH旷工费',
                }],
              })(
                <Input placeholder="请输入ETH旷工费"/>
              )}
            </FormItem>

            <FormItem {...formItemLayout} label={fieldLabels.gasLimit}>
              {getFieldDecorator('gasLimit', {
                initialValue: `${findData.data.gasLimit === undefined ? '' : findData.data.gasLimit}`,
                rules: [{
                  required: true,
                  max: 30,
                  message: '请正确填写ETH旷工Limit',
                }],
              })(
                <Input placeholder="请输入ETH旷工Limit"/>
              )}
            </FormItem>

            <FormItem {...formItemLayout} label={fieldLabels.lockPassword}>
              {getFieldDecorator('lockPassword', {
                initialValue: `${findData.data.lockPassword === undefined ? '' : findData.data.lockPassword}`,
                rules: [{
                  max: 100,
                  message: '请正确填写解锁密码',
                }],
              })(
                <Input placeholder="请输入解锁密码"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.memo}>
              {getFieldDecorator('memo', {
                initialValue: `${findData.data.memo === undefined ? '' : findData.data.memo}`,
                rules: [{
                  max: 255,
                  message: '请正确填写memo标签',
                }],
              })(
                <Input placeholder="请输入memo标签"/>
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
  submitting: loading.effects['depositWallet/add'],
}))(Form.create()(DepositWalletForm));
