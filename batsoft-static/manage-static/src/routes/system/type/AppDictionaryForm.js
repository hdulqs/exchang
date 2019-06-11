import React from 'react';
import {Card, Button, Form, Icon, Col, Row, Input, Select, Popover, TreeSelect , Radio} from 'antd';
import {connect} from 'dva';
import PageHeaderLayout from '../../../layouts/PageHeaderLayout';
import FooterToolbar from '../../../components/FooterToolbar';
import styles from '../../../assets/style/style.less';
import {Link} from 'dva/router';
import BaseForm from '../../../components/BatSoft/Form';
const { TextArea } = Input;
const {Option} = Select;
const FormItem = Form.Item;
const fieldLabels = {
  name: '名称',
  icon: '图标',
  dicKey: '字典KEY',
  sort:'排序',
  description:'description',
  display:'是否显示',
};

@connect(({appDictionary}) => ({
  appDictionary,
}))
class AppDictionaryForm extends BaseForm {
  state = {
    formType: 'add',
    dicSelectValue: undefined,
    pid:0,
  };
  // 下拉树选择
  onChange = (value) => {
    this.setState({
      dicSelectValue:value,
    });
  }
  componentDidMount() {
    const {match} = this.props;
    this.find('appDictionary/dicTree');
    if (match.params.id !== undefined) {
      this.setState({
        formType: "edit",
      })
      this.find('appDictionary/findDic', {id: match.params.id});
    }

    if (match.params.pid !== undefined) {
      this.setState({
        pid: match.params.pid,
      })
    }
  }

  /**
   * 增加、修改回调函数
   * @param data
   */
  addCallBack = data => {
    const {form} = this.props;
    if(data.success){
      this.find('appDictionary/dicTree');
    }
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
    const {appDictionary: {findData},appDictionary: {dicTreeData}, form, dispatch, submitting} = this.props;
    if(dicTreeData===undefined) dicTreeData=[];
    const {getFieldDecorator, validateFieldsAndScroll, getFieldsError} = form;

    if (this.state.formType == "add") {
      findData.data = {};
      if(this.state.pid!=undefined){
        findData.data.parentId=this.state.pid;
      }
    }
    const validate = () => {
      validateFieldsAndScroll((error, params) => {
        if (!error) {
          this.add('appDictionary/add', params, this.addCallBack);
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

            <FormItem {...formItemLayout} label="父级字典">

              {getFieldDecorator('parentId', {
                initialValue: `${findData.data.parentId === undefined ? '0' : findData.data.parentId}`,
                rules: [{
                  required: true,
                  message: '请选择父字典',
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

            </FormItem>

            <FormItem {...formItemLayout} label={fieldLabels.name}>
              {getFieldDecorator('name', {
                initialValue: `${findData.data.name === undefined ? '' : findData.data.name}`,
                rules: [{
                  required: true,
                  max: 100,
                  message: '请正确填写字典名称',
                }],
              })(
                <Input placeholder="请输入字典名称"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.icon}>
              {getFieldDecorator('icon', {
                initialValue: `${findData.data.icon === undefined ? '' : findData.data.icon}`,
                rules: [],
              })(
                <Input placeholder="请输入图标"/>
              )}
            </FormItem>


            <FormItem {...formItemLayout} label={fieldLabels.dicKey}>
              {getFieldDecorator('dicKey', {
                initialValue: `${findData.data.dicKey === undefined ? '' : findData.data.dicKey}`,
                rules: [{
                  required: true,
                  message: '请正确填写字典Key',
                }],
              })(
                <Input placeholder="请输入字典Key"/>
              )}
            </FormItem>

            <FormItem {...formItemLayout} label={fieldLabels.sort}>
              {getFieldDecorator('sort', {
                initialValue: `${findData.data.sort === undefined ? '1' : findData.data.sort}`,
                rules: [{
                  required: true,
                  message: '请正确填写排序值',
                }],
              })(
                <Input placeholder="请输入排序值"/>
              )}
            </FormItem>
            <FormItem {...formItemLayout} label={fieldLabels.display}>
              {getFieldDecorator('display', {
                initialValue:`${findData.data.display===undefined?'1':findData.data.display}`,
                rules: [],
              })(
                <Radio.Group>
                  <Radio value="1" >是</Radio>
                  <Radio value="0" >否</Radio>
                </Radio.Group>
              )}
            </FormItem>

            <FormItem {...formItemLayout} label={fieldLabels.description}>
              {getFieldDecorator('description', {
                initialValue: `${findData.data.description === undefined ? '' : findData.data.description}`,
                rules: [],
              })(
                <TextArea style={{ minHeight: 32 }} placeholder="备注" rows={4} />
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
  submitting: loading.effects['appDictionary/add'],
}))(Form.create()(AppDictionaryForm));
