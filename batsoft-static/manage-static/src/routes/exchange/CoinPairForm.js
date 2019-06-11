import React from 'react';
import {Card, Button, Form, Icon, Col, Row, Input, Select, Popover,DatePicker,Radio} from 'antd';
import {connect} from 'dva';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import FooterToolbar from '../../components/FooterToolbar';
import styles from '../../assets/style/style.less';
import {Link} from 'dva/router';
import BaseForm from '../../components/BatSoft/Form';

const {Option} = Select;
const FormItem = Form.Item;
const fieldLabels = {
  tradeCoinCode: '交易币代码',
  pricingCoinCode: '定价币代码',
  tradeCoinLogo: '交易币logo',
  status: '状态',
  recommend: '是否推荐',
  amtDecimal:'挂单最小位数',
  priceDecimal:'单价最小位数',
  amountDecimal:'交易总额最小位数',
  openPrice:'发行价',
  sort: '排序',
};

@connect(({coinPair}) => ({
  coinPair,
}))
class CoinPairForm extends BaseForm {
  state = {
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('coinPair/find',{id: match.params.id});
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
    const formItemLayout = {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 7 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 12 },
        md: { span: 10 },
      },
    };
    const {coinPair: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data={};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          this.add('coinPair/add',params,this.addCallBack);
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
      <Form  hideRequiredMark>
    <Form.Item {...formItemLayout} label={fieldLabels.id}>
    {getFieldDecorator('id', {
      initialValue: findData.data.id,
    })(
    <Input style={{display: 'none'}}/>
    )}
  </Form.Item>
    <FormItem {...formItemLayout} label={fieldLabels.tradeCoinCode}>
    {getFieldDecorator('tradeCoinCode', {
      initialValue:`${findData.data.tradeCoinCode===undefined?'':findData.data.tradeCoinCode}`,
      rules: [{
        required: true,
        max:50,
        message: '请正确填写交易币代码',
      }],
    })(
    <Input placeholder="请输入交易币代码"/>
    )}
  </FormItem>


    <FormItem {...formItemLayout} label={fieldLabels.pricingCoinCode}>
    {getFieldDecorator('pricingCoinCode', {
      initialValue:`${findData.data.pricingCoinCode===undefined?'':findData.data.pricingCoinCode}`,
      rules: [{
        required: true,
        max:50,
        message: '请正确填写定价币代码',
      }],
    })(
    <Input placeholder="请输入定价币代码"/>
    )}
  </FormItem>


    <FormItem {...formItemLayout} label={fieldLabels.tradeCoinLogo}>
    {getFieldDecorator('tradeCoinLogo', {
      initialValue:`${findData.data.tradeCoinLogo===undefined?'':findData.data.tradeCoinLogo}`,
      rules: [{
        required: true,
        max:100,
        message: '请正确填写交易币logo',
      }],
    })(
    <Input placeholder="请输入交易币logo"/>
    )}
  </FormItem>


    <FormItem  {...formItemLayout} label={fieldLabels.status}>
    {getFieldDecorator('status', {
      initialValue:`${findData.data.status===undefined?'1':findData.data.status}`,
      rules: [{
        required: true,
        max:1,
        message: '请正确填写状态',
      }],
    })(
    <Radio.Group>
    <Radio value="0" >禁止交易</Radio>
      <Radio value="1" >开启交易</Radio>
      </Radio.Group>
    )}
  </FormItem>

    <FormItem  {...formItemLayout} label={fieldLabels.recommend}>
    {getFieldDecorator('recommend', {
      initialValue:`${findData.data.recommend===undefined?'0':findData.data.recommend}`,
      rules: [{
        required: true,
        max:1,
        message: '请正确填写',
      }],
    })(
    <Radio.Group>
    <Radio value="0" >否</Radio>
      <Radio value="1" >是</Radio>
      </Radio.Group>
    )}
  </FormItem>
        <FormItem {...formItemLayout} label={fieldLabels.openPrice}>
          {getFieldDecorator('openPrice', {
            initialValue:`${findData.data.openPrice===undefined?'0':findData.data.openPrice}`,
            rules: [{
              max:11,
              message: '请正确填写发行价',
            }],
          })(
            <Input placeholder="请输入发行价"/>
          )}
        </FormItem>
    <FormItem {...formItemLayout} label={fieldLabels.amtDecimal}>
    {getFieldDecorator('amtDecimal', {
      initialValue:`${findData.data.amtDecimal===undefined?'0':findData.data.amtDecimal}`,
      rules: [{
        max:11,
        message: '请正确填写位数',
      }],
    })(
    <Input placeholder="请输入位数"/>
    )}
  </FormItem>

    <FormItem {...formItemLayout} label={fieldLabels.priceDecimal}>
    {getFieldDecorator('priceDecimal', {
      initialValue:`${findData.data.priceDecimal===undefined?'0':findData.data.priceDecimal}`,
      rules: [{
        max:11,
        message: '请正确位数',
      }],
    })(
    <Input placeholder="请输入位数"/>
    )}
  </FormItem>

    <FormItem {...formItemLayout} label={fieldLabels.amountDecimal}>
    {getFieldDecorator('amountDecimal', {
      initialValue:`${findData.data.amountDecimal===undefined?'0':findData.data.amountDecimal}`,
      rules: [{
        max:11,
        message: '请正确填写位数',
      }],
    })(
    <Input placeholder="请输入位数"/>
    )}
  </FormItem>

    <FormItem {...formItemLayout} label={fieldLabels.sort}>
    {getFieldDecorator('sort', {
      initialValue:`${findData.data.sort===undefined?'0':findData.data.sort}`,
      rules: [{
        max:11,
        message: '请正确填写排序',
      }],
    })(
    <Input placeholder="请输入排序"/>
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
  submitting: loading.effects['coinPair/add'],
}))(Form.create()(CoinPairForm));
