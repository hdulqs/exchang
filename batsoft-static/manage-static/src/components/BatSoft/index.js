import React, { PureComponent } from 'react';
import { Link } from 'dva/router';
import { Modal} from 'antd';
import {message} from "antd/lib/index";
import {hasPermissions} from '../../utils/authority';
const confirm = Modal.confirm;
/**
 * BaseAction 执行 CRUD
 */
class BaseAction extends PureComponent {
  state = {
  };

  /**
   * 校验权限
   * @param str
   * @returns {*}
   */
  hasPermissions=(str)=>{
    return hasPermissions(str);
  }
  /**
   * 查询对象
   * @param type
   * @param params
   */
  find = (type, params) => {
    params= params === undefined?{}:params;
    const { dispatch } = this.props;
    dispatch({
      type: type,
      payload: {
        ...params,
      },
    });
  }
  /**
   * 查询操作
   * @param params 参数
   * @type 请求类型
   */
  list = (type, params) => {
    params= params === undefined?{}:params;
    const { dispatch } = this.props;
    dispatch({
      type: type,
      payload: {
        ...params,
      },
    });
  }

  /**
   * 新增操作
   * @param params 参数
   * @type 请求类型
   */
  add = (type,params,callback) => {
    const {dispatch} = this.props;
    dispatch({
      type: type,
      payload: {
        ...params,
      },
      callback: data => {
        if (data.success) {
          message.success(data.msg);
          callback(data);
        } else {
          message.error(data.msg);
        }
      },
    });
  }

  /**
   * 删除操作
   * @param type
   * @param params remove 参数 一般为 ids: selectedRows.map(row => row.id).join(','),
   * @param callback
   */
  remove = (type,params,callback) => {
    confirm({
      title: '温馨提醒！',
      content: '是否确认删除？',
      onOk: () => {
        const {dispatch} = this.props;
        dispatch({
          type: type,
          payload: {
            ...params,
          },
          callback: () => {
            callback();
          },
        });
      },
      onCancel() {
        console.log('Cancel');
      },
    });
  }
  /**
   * 撤销操作
   * @param type
   * @param params remove 参数 一般为 ids: selectedRows.map(row => row.id).join(','),
   * @param callback
   */
  revert = (type,params,callback) => {
    confirm({
      title: '温馨提醒！',
      content: '是否确认撤销？',
      onOk: () => {
        const {dispatch} = this.props;
        dispatch({
          type: type,
          payload: {
            ...params,
          },
          callback: () => {
            callback();
          },
        });
      },
      onCancel() {
        console.log('Cancel');
      },
    });
  }
}
export default BaseAction;
