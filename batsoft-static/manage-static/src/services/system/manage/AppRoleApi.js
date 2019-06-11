import {stringify} from 'qs';
import request from '../../../utils/request';
import config from '../../../common/config';

/**
 * 系统角色查询列表
 * @param params
 * @returns {Promise<Object>}
 */
export async function queryAppRole(params) {
  return request(config.apiUrl + '/system/manage/appRole/list?' + `${stringify(params)}`);

}

/**
 * 系统角色删除
 * @param params
 * @returns {Promise<Object>}
 */
export async function removeAppRole(params) {
  return request(config.apiUrl + '/system/manage/appRole/remove', {
    method: 'POST',
    body: params,
  });
}

/**
 * 系统角色增加
 * @param params
 * @returns {Promise<Object>}
 */
export async function addAppRole(params) {
  return request(config.apiUrl + '/system/manage/appRole/save', {
    method: 'POST',
    body: params,
  });
}

/**
 * 系统角色获取
 * @param params
 * @returns {Promise<Object>}
 */
export async function findAppRole(params) {
  return request(config.apiUrl + '/system/manage/appRole/find/' + `${params.id}`);
}

/**
 * 获取可用角色
 * @param params
 * @returns {Promise<Object>}
 */
export async function findRoles() {
  return request(config.apiUrl + '/system/manage/appRole/findRoles');
}

/**
 * 获取某角色的已选权限
 * @param params
 * @returns {Promise<Object>}
 */
export async function findMenu(params) {
  return request(config.apiUrl + '/system/manage/appRole/findMenu/' + `${params.roleid}`);
}


/**
 * 授权
 * @param params
 * @returns {Promise<Object>}
 */
export async function addPerm(params) {
  return request(config.apiUrl + '/system/manage/appRole/savePerm', {
    method: 'POST',
    body: params,
  });
}
