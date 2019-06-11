import React from 'react';
import {Card, Button, Form, Icon, Col, Tabs, Input, Select, Popover, DatePicker, Radio} from 'antd';
import {connect} from 'dva';
import PageHeaderLayout from '../../../layouts/PageHeaderLayout';
import FooterToolbar from '../../../components/FooterToolbar';
import styles from '../../../assets/style/style.less';
import {Link} from 'dva/router';
import BaseForm from '../../../components/BatSoft/Form';

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
const TabPane = Tabs.TabPane;
const {Option} = Select;
const FormItem = Form.Item;
const {TextArea} = Input;

const typeTab = [];
@connect(({appConfig, appConfigType}) => ({
  appConfig,
  appConfigType,
}))
class AppConfigSetForm extends BaseForm {
  state = {
    ...this.state,
    initTab:false,
    formType: 'add',
  };

  componentDidMount() {
    this.find('appConfigType/findTypes');
  }

  /**
   * 增加、修改回调函数
   * @param data
   */
  addCallBack = data => {
    const {form} = this.props;
    form.resetFields();
  }
  tabChange = (key, e) => {
    console.log(key);
  }


  handleSubmit = (e) => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      debugger
      if (!err) {
        console.log('Received values of form: ', values);
      }
    });
  }

  render() {
    const {appConfigType:{findData:typeData}, submitting,form} = this.props;
    const { validateFieldsAndScroll, getFieldsError,getFieldProps ,getFieldsValue} = form;
    const validate = () => {
      alert(this.props.form.getFieldValue('smsPassword'));
      getFieldsValue((k,v)=>{
      })

      validateFieldsAndScroll((error, params) => {
        const _data = [];
        Object.keys(params).map((key,val)=>{
          _data.push({"key": key, "value": params[key]});
        })

        if (!error) {
          this.add('appConfig/update',{data:_data}, this.addCallBack);
        }
      });
    };
    const errors = getFieldsError();
    if(!this.state.initTab) {
      if (typeData.data instanceof Array) {
        typeData.data.forEach((item) => {
          const content = [];
          typeTab.push(<TabPane tab={item.typeName} key={item.typeKey}>
            <Form hideRequiredMark >
              {
                item.children.forEach((data) => {
                    content.push(<FormItem {...formItemLayout} label={data.confName}>

                      <Input placeholder="请输入配置key" id={data.confKey} defaultValue={data.confValue}/>

                    </FormItem>)
                  }
                )
              }
              {content}
            </Form>
          </TabPane>);
        });
        this.setState({
          initTab: true,
        })
      }
    }
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
            {/*<div className={styles.errorField}>{fieldLabels[key]}</div>*/}
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

          <Tabs defaultActiveKey="1" onChange={this.tabChange}>
            {typeTab}

          </Tabs>
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
;
export default connect(({global, loading}) => ({
  collapsed: global.collapsed,
  submitting: loading.effects['appConfig/update'],
}))(Form.create()(AppConfigSetForm));
