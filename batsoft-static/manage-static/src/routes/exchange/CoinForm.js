import React from 'react';
import {Card, Button, Form, Icon, Col, Row, Input, Select, Popover, DatePicker, Radio} from 'antd';
import {connect} from 'dva';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import FooterToolbar from '../../components/FooterToolbar';
import styles from '../../assets/style/style.less';
import {Link} from 'dva/router';
import FileUpload from '../../components/FileUpload';
import BaseForm from '../../components/BatSoft/Form';

const {Option} = Select;

const {TextArea} = Input;
const fieldLabels = {
  coinName: '币种名称',
  coinCode: '币种代码',
  issuerName: '发行方名称',
  totleMoney: '发行总额',
  totleNumber: '发行总量',
  price: '发行单价',
  coinLogo: 'logo',
  priceCoin: '是否定价币',
  erc20Status: '是否erc20代币',
  calculationLen: '结算位数',
  allowRecharge: '允许充值',
  allowWithdraw: '允许提现',
  chargeRate: '充手续费%',
  withdrawRate: '提手续费%',
  withdrawBaseFee:'基础手续费',
  withdrawFeeType:'手续费类型',
  withdrawMin: '最小提币数量',
  withdrawMax: '单日最大提币数量',
  noAuthWithdrawMax: '未实名单日最大提币数量',
  withdrawAcMaxAmt: '单笔小于多少无需审核',

  sellRate: '卖手续费%',
  buyRate: '买手续费%',
  sort: '排序',
  coinConfirm: '充值节点确认个数',
  maxNum: '最大成交量',
  minNum: '最小成交量',
  prove: '证明方式',
  minBlock: '区块最小值',
  maxBlock: '区块最大值',
  prodSpeed: '区块多少秒产生一个',
  tokenUrl: '钱包地址',
  sourceUrl: '源码地址',
  blockBrow: '区块浏览器',
  officialWeb: '官网地址',
  officialForum: '官网论坛',
  miningUrl: '挖矿地址',
  status: '状态',
  algorithm: '算法说明',
  productIntroduction: '产品介绍',
};

@connect(({coin}) => ({
  coin,
}))
class CoinForm extends BaseForm {
  state = {
    fileList: null,
    formType: 'add',
  };

  componentDidMount() {
    const {match} = this.props;
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('coin/find', {id: match.params.id});
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
    const {coin: {findData}, form, dispatch, submitting} = this.props;
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;
    if (this.state.formType == "add") {
      findData.data = {};
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          if (this.state.fileList != null) {
            params.coinLogo = this.state.fileList[0].response.result;
          }
          this.add('coin/add', params, this.addCallBack);
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
                <Form.Item label={fieldLabels.coinName}>
                  {getFieldDecorator('coinName', {
                    initialValue: `${findData.data.coinName === undefined ? '' : findData.data.coinName}`,
                    rules: [{
                      required: true,
                      max: 50,
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
                    initialValue: `${findData.data.coinCode === undefined ? '' : findData.data.coinCode}`,
                    rules: [{
                      required: true,
                      max: 50,
                      message: '请正确填写币种代码',
                    }],
                  })(
                    <Input placeholder="请输入币种代码"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.issuerName}>
                  {getFieldDecorator('issuerName', {
                    initialValue: `${findData.data.issuerName === undefined ? '' : findData.data.issuerName}`,
                    rules: [{
                      max: 100,
                      message: '请正确填写发行方名称',
                    }],
                  })(
                    <Input placeholder="请输入发行方名称"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.totleMoney}>
                  {getFieldDecorator('totleMoney', {
                    initialValue: `${findData.data.totleMoney === undefined ? '0.00000000' : findData.data.totleMoney}`,
                    rules: [{
                      required: true,
                      max: 20,
                      message: '请正确填写发行总额',
                    }],
                  })(
                    <Input placeholder="请输入发行总额"/>
                  )}
                </Form.Item>
              </Col>
            </Row>
            <Row gutter={16}>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.totleNumber}>
                  {getFieldDecorator('totleNumber', {
                    initialValue: `${findData.data.totleNumber === undefined ? '1' : findData.data.totleNumber}`,
                    rules: [{
                      required: true,
                      max: 11,
                      message: '请正确填写发行总量',
                    }],
                  })(
                    <Input placeholder="请输入发行总量"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.price}>
                  {getFieldDecorator('price', {
                    initialValue: `${findData.data.price === undefined ? '0.00000000' : findData.data.price}`,
                    rules: [{
                      required: true,
                      max: 10,
                      message: '请正确填写发行单价',
                    }],
                  })(
                    <Input placeholder="请输入发行单价"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.coinLogo}>
                  <FileUpload
                    callback={this.handleFileChange}>
                  </FileUpload>
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.priceCoin}>
                  {getFieldDecorator('priceCoin', {
                    initialValue: `${findData.data.priceCoin === undefined ? '0' : findData.data.priceCoin}`,
                    rules: [{
                      max: 1,
                      message: '请正确填写是否定价币',
                    }],
                  })(
                    <Select placeholder="请选择是否定价币">
                      <Option value="0">否</Option>
                      <Option value="1">是</Option>
                    </Select>
                  )}
                </Form.Item>
              </Col>
            </Row>
            <Row gutter={16}>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.calculationLen}>
                  {getFieldDecorator('calculationLen', {
                    initialValue: `${findData.data.calculationLen === undefined ? '2' : findData.data.calculationLen}`,
                    rules: [{
                      max: 2,
                      message: '请正确填写结算位数',
                    }],
                  })(
                    <Input placeholder="请输入结算位数"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.allowRecharge}>
                  {getFieldDecorator('allowRecharge', {
                    initialValue: `${findData.data.allowRecharge === undefined ? '1' : findData.data.allowRecharge}`,
                    rules: [{
                      max: 1,
                      message: '请正确填写允许充值',
                    }],
                  })(
                    <Select placeholder="请选择允许充值">
                      <Option value="0">否</Option>
                      <Option value="1">是</Option>
                    </Select>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.allowWithdraw}>
                  {getFieldDecorator('allowWithdraw', {
                    initialValue: `${findData.data.allowWithdraw === undefined ? '1' : findData.data.allowWithdraw}`,
                    rules: [{
                      max: 1,
                      message: '请正确填写允许提现',
                    }],
                  })(
                    <Select placeholder="请选择允许提现">
                      <Option value="0">否</Option>
                      <Option value="1">是</Option>
                    </Select>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.chargeRate}>
                  {getFieldDecorator('chargeRate', {
                    initialValue: `${findData.data.chargeRate === undefined ? '0.00000000' : findData.data.chargeRate}`,
                    rules: [{
                      max: 11,
                      message: '请正确填写充手续费%',
                    }],
                  })(
                    <Input placeholder="请输入充手续费%"/>
                  )}
                </Form.Item>
              </Col>
            </Row>

            <Row gutter={16}>
              <Form.Item label={fieldLabels.withdrawFeeType}>
                {getFieldDecorator('withdrawFeeType', {
                  initialValue: `${findData.data.withdrawFeeType === undefined ? '0' : findData.data.withdrawFeeType}`,
                  rules: [],
                })(
                  <Select placeholder="手续费类型">
                    <Option value="0">固定手续费</Option>
                    <Option value="1">百分比收取</Option>
                  </Select>
                )}
              </Form.Item>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.withdrawBaseFee}>
                  {getFieldDecorator('withdrawBaseFee', {
                    initialValue: `${findData.data.withdrawBaseFee === undefined ? '0.00000000' : findData.data.withdrawBaseFee}`,
                    rules: [],
                  })(
                    <Input placeholder="请输入基础手续费"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.erc20Status}>
                  {getFieldDecorator('erc20Status', {
                    initialValue: `${findData.data.erc20Status === undefined ? '0' : findData.data.erc20Status}`,
                    rules: [{
                      max: 1,
                      message: '请正确填写是否erc20代币',
                    }],
                  })(
                    <Select placeholder="请选择是否erc20代币">
                      <Option value="0">否</Option>
                      <Option value="1">是</Option>
                    </Select>
                  )}
                </Form.Item>
              </Col>
            </Row>
            <Row gutter={16}>

              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.withdrawMin}>
                  {getFieldDecorator('withdrawMin', {
                    initialValue: `${findData.data.withdrawMin === undefined ? '0.00000000' : findData.data.withdrawMin}`,
                    rules: [],
                  })(
                    <Input placeholder="请输入最小提币数量"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.withdrawMax}>
                  {getFieldDecorator('withdrawMax', {
                    initialValue: `${findData.data.withdrawMax === undefined ? '0.00000000' : findData.data.withdrawMax}`,
                    rules: [],
                  })(
                    <Input placeholder="请输入单日最大提币数量"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.noAuthWithdrawMax}>
                  {getFieldDecorator('noAuthWithdrawMax', {
                    initialValue: `${findData.data.noAuthWithdrawMax === undefined ? '0.00000000' : findData.data.noAuthWithdrawMax}`,
                    rules: [],
                  })(
                    <Input placeholder="请输入未实名单日最大提币数量"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.withdrawAcMaxAmt}>
                  {getFieldDecorator('withdrawAcMaxAmt', {
                    initialValue: `${findData.data.withdrawAcMaxAmt === undefined ? '0.00000000' : findData.data.withdrawAcMaxAmt}`,
                    rules: [],
                  })(
                    <Input placeholder="请输入单笔小于多少无需审核"/>
                  )}
                </Form.Item>
              </Col>
            </Row>


            <Row gutter={16}>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.withdrawRate}>
                  {getFieldDecorator('withdrawRate', {
                    initialValue: `${findData.data.withdrawRate === undefined ? '0.00000000' : findData.data.withdrawRate}`,
                    rules: [{
                      max: 11,
                      message: '请正确填写提手续费%',
                    }],
                  })(
                    <Input placeholder="请输入提手续费%"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.sellRate}>
                  {getFieldDecorator('sellRate', {
                    initialValue: `${findData.data.sellRate === undefined ? '0.00000000' : findData.data.sellRate}`,
                    rules: [{
                      max: 11,
                      message: '请正确填写卖手续费%',
                    }],
                  })(
                    <Input placeholder="请输入卖手续费%"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.buyRate}>
                  {getFieldDecorator('buyRate', {
                    initialValue: `${findData.data.buyRate === undefined ? '0.00000000' : findData.data.buyRate}`,
                    rules: [{
                      max: 11,
                      message: '请正确填写买手续费%',
                    }],
                  })(
                    <Input placeholder="请输入买手续费%"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.sort}>
                  {getFieldDecorator('sort', {
                    initialValue: `${findData.data.sort === undefined ? '0' : findData.data.sort}`,
                    rules: [{
                      max: 11,
                      message: '请正确填写排序',
                    }],
                  })(
                    <Input placeholder="请输入排序"/>
                  )}
                </Form.Item>
              </Col>
            </Row>
            <Row gutter={16}>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.coinConfirm}>
                  {getFieldDecorator('coinConfirm', {
                    initialValue: `${findData.data.coinConfirm === undefined ? '3' : findData.data.coinConfirm}`,
                    rules: [{
                      required: true,
                      max: 5,
                      message: '请正确填写充值节点确认个数',
                    }],
                  })(
                    <Input placeholder="请输入充值节点确认个数"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.maxNum}>
                  {getFieldDecorator('maxNum', {
                    initialValue: `${findData.data.maxNum === undefined ? '1.00000000' : findData.data.maxNum}`,
                    rules: [{
                      max: 11,
                      message: '请正确填写最大成交量',
                    }],
                  })(
                    <Input placeholder="请输入最大成交量"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.minNum}>
                  {getFieldDecorator('minNum', {
                    initialValue: `${findData.data.minNum === undefined ? '1.00000000' : findData.data.minNum}`,
                    rules: [{
                      max: 11,
                      message: '请正确填写最小成交量',
                    }],
                  })(
                    <Input placeholder="请输入最小成交量"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.prove}>
                  {getFieldDecorator('prove', {
                    initialValue: `${findData.data.prove === undefined ? '' : findData.data.prove}`,
                    rules: [{
                      max: 255,
                      message: '请正确填写证明方式',
                    }],
                  })(
                    <Input placeholder="请输入证明方式"/>
                  )}
                </Form.Item>
              </Col>
            </Row>
            <Row gutter={16}>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.minBlock}>
                  {getFieldDecorator('minBlock', {
                    initialValue: `${findData.data.minBlock === undefined ? '0.00000000' : findData.data.minBlock}`,
                    rules: [{
                      max: 20,
                      message: '请正确填写区块最小值',
                    }],
                  })(
                    <Input placeholder="请输入区块最小值"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.maxBlock}>
                  {getFieldDecorator('maxBlock', {
                    initialValue: `${findData.data.maxBlock === undefined ? '0.00000000' : findData.data.maxBlock}`,
                    rules: [{
                      max: 20,
                      message: '请正确填写区块最大值',
                    }],
                  })(
                    <Input placeholder="请输入区块最大值"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.prodSpeed}>
                  {getFieldDecorator('prodSpeed', {
                    initialValue: `${findData.data.prodSpeed === undefined ? '0.00000000' : findData.data.prodSpeed}`,
                    rules: [{
                      max: 10,
                      message: '请正确填写区块多少秒产生一个',
                    }],
                  })(
                    <Input placeholder="请输入区块多少秒产生一个"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.tokenUrl}>
                  {getFieldDecorator('tokenUrl', {
                    initialValue: `${findData.data.tokenUrl === undefined ? '' : findData.data.tokenUrl}`,
                    rules: [{
                      max: 255,
                      message: '请正确填写钱包地址',
                    }],
                  })(
                    <Input placeholder="请输入钱包地址"/>
                  )}
                </Form.Item>
              </Col>
            </Row>
            <Row gutter={16}>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.sourceUrl}>
                  {getFieldDecorator('sourceUrl', {
                    initialValue: `${findData.data.sourceUrl === undefined ? '' : findData.data.sourceUrl}`,
                    rules: [{
                      max: 255,
                      message: '请正确填写源码地址',
                    }],
                  })(
                    <Input placeholder="请输入源码地址"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.blockBrow}>
                  {getFieldDecorator('blockBrow', {
                    initialValue: `${findData.data.blockBrow === undefined ? '' : findData.data.blockBrow}`,
                    rules: [{
                      max: 255,
                      message: '请正确填写区块浏览器',
                    }],
                  })(
                    <Input placeholder="请输入区块浏览器"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.officialWeb}>
                  {getFieldDecorator('officialWeb', {
                    initialValue: `${findData.data.officialWeb === undefined ? '' : findData.data.officialWeb}`,
                    rules: [{
                      max: 255,
                      message: '请正确填写官网地址',
                    }],
                  })(
                    <Input placeholder="请输入官网地址"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.officialForum}>
                  {getFieldDecorator('officialForum', {
                    initialValue: `${findData.data.officialForum === undefined ? '' : findData.data.officialForum}`,
                    rules: [{
                      max: 255,
                      message: '请正确填写官网论坛',
                    }],
                  })(
                    <Input placeholder="请输入官网论坛"/>
                  )}
                </Form.Item>
              </Col>
            </Row>
            <Row gutter={16}>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.miningUrl}>
                  {getFieldDecorator('miningUrl', {
                    initialValue: `${findData.data.miningUrl === undefined ? '' : findData.data.miningUrl}`,
                    rules: [{
                      max: 255,
                      message: '请正确填写挖矿地址',
                    }],
                  })(
                    <Input placeholder="请输入挖矿地址"/>
                  )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.status}>
                  {getFieldDecorator('status', {
                    initialValue: `${findData.data.status === undefined ? '0' : findData.data.status}`,
                    rules: [{
                      required: true,
                      message: '请正确填写状态',
                  }],
                  })(
                    <Select placeholder="请选择状态">
                    <Option value="0">关闭</Option>
                    <Option value="1">开启</Option>
                    <Option value="2">下架</Option>
                    </Select>
                    )}
                </Form.Item>
              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.algorithm}>
                  {getFieldDecorator('algorithm', {
                    initialValue: `${findData.data.algorithm === undefined ? '' : findData.data.algorithm}`,
                    rules: [{
                      max: 255,
                      message: '请正确填写算法说明',
                    }],
                  })(
                    <TextArea style={{minHeight: 32}} placeholder="算法说明" rows={4}/>
                  )}
                </Form.Item>

              </Col>
              <Col lg={6} md={12} sm={24}>
                <Form.Item label={fieldLabels.productIntroduction}>
                  {getFieldDecorator('productIntroduction', {
                    initialValue: `${findData.data.productIntroduction === undefined ? '' : findData.data.productIntroduction}`,
                    rules: [{
                      max: 255,
                      message: '请正确填写产品介绍',
                    }],
                  })(
                    <TextArea style={{minHeight: 32}} placeholder="产品介绍" rows={4}/>
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
  submitting: loading.effects['coin/add'],
}))(Form.create()(CoinForm));
