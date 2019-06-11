import React, { PureComponent } from 'react';
import { Link } from 'dva/router';
import { Modal} from 'antd';
import  BaseAction from '../index';
const confirm = Modal.confirm;
class BaseList extends BaseAction {
  state = {
    modalVisible: false,
    expandForm: false,
    selectedRows: [],
    formValues: {},
    hideReason: true,
    reviewobj: {}
  };

  // ====================================Handle====================================================
  /**
   * 展开更多事件
   */
  toggleForm = () => {
    this.setState({
      expandForm: !this.state.expandForm,
    });
  }

  /**
   * 打开新建页面的弹出层事件
   * @param flag
   */
  handleModalVisible = (flag) => {
    this.setState({
      modalVisible: !!flag,
    });
  }

  /**
   * 单选多选 单列、多列 选中列表 事件
   * @param rows
   */
  handleSelectRows = (rows) => {
    this.setState({
      selectedRows: rows,
    });
  }

  //=============================Render==============================================

  handleStandardTableChangeParams = (pagination, filtersArg, sorter) => {
    const {formValues} = this.state;
    const filters = Object.keys(filtersArg).reduce((obj, key) => {
      const newObj = {...obj};
      // newObj[key] = getValue(filtersArg[key]);
      return newObj;
    }, {});

    const params = {
      page: pagination.current,
      pageSize: pagination.pageSize,
      ...formValues,
      ...filters,
    };
    if (sorter.field) {
      params.sortName = `${sorter.field}`;
      params.sortOrder = `${sorter.order}`;
    }
    return params;
  }
  /**
   * 重置查询事件
   */
  handleFormReset = (type) => {
    const {form} = this.props;
    form.resetFields();
    this.setState({
      formValues: {},
    });
    this.list(type);
  }
  /**
   * 简单搜索框
   */
  renderSimpleForm() {}

  /**
   * 复杂搜索框
   */
  renderAdvancedForm(){}
  /**
   * 渲染搜索框
   * @returns {*}
   */
  renderForm() {
    return this.state.expandForm ? this.renderAdvancedForm() : this.renderSimpleForm();
  }
}
export default BaseList;
