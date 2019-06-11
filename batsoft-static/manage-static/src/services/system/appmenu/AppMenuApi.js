import {stringify} from 'qs';
import request from '../../../utils/request';
import config from '../../../common/config';

/**
 * menu获取
 * @param params
 * @returns {Promise<Object>}
 */
export async function findAppMenu(params) {
  return request(config.apiUrl + '/system/appmenu/appMenu/find/' + `${params.id}`);
}

/**
 * 权限树列表
 * @param params
 * @returns {Promise<Object>}
 */
export async function queryAppMenuTreeList(params) {
  return request(config.apiUrl + '/system/appmenu/appMenu/list');

}

/**
 * 权限树
 * @param params
 * @returns {Promise<Object>}
 */
export async function queryAppMenuTree(params) {
  return request(config.apiUrl + '/system/appmenu/appMenu/treeList');

}


/**
 * 权限导航删除
 * @param params
 * @returns {Promise<Object>}
 */
export async function removeAppMenu(params) {
  return request(config.apiUrl + '/system/appmenu/appMenu/remove', {
    method: 'POST',
    body: params,
  });
}

/**
 * 权限导航增加
 * @param params
 * @returns {Promise<Object>}
 */
export async function addAppMenu(params) {
  return request(config.apiUrl + '/system/appmenu/appMenu/save', {
    method: 'POST',
    body: params,
  });
}
