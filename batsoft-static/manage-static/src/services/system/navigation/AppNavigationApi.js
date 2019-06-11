import {stringify} from 'qs';
import request from '../../../utils/request';
import config from '../../../common/config';

/**
 * menu获取
 * @param params
 * @returns {Promise<Object>}
 */
export async function findAppMenu(params) {
  return request(config.apiUrl + '/system/navigation/appNavigation/find/' + `${params.id}`);
}

/**
 * 权限树列表
 * @param params
 * @returns {Promise<Object>}
 */
export async function queryAppMenuTreeList(params) {
  return request(config.apiUrl + '/system/navigation/appNavigation/list');

}

/**
 * 权限树
 * @param params
 * @returns {Promise<Object>}
 */
export async function queryAppMenuTree(params) {
  return request(config.apiUrl + '/system/navigation/appNavigation/treeList');

}


/**
 * 权限导航删除
 * @param params
 * @returns {Promise<Object>}
 */
export async function removeAppMenu(params) {
  return request(config.apiUrl + '/system/navigation/appNavigation/remove', {
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
  return request(config.apiUrl + '/system/navigation/appNavigation/save', {
    method: 'POST',
    body: params,
  });
}
