import {stringify} from 'qs';
import request from '../../../utils/request';
import config from '../../../common/config';

/**
 * 查询列表
 * @param params
 * @returns {Promise<Object>}
 */
export async function queryAppUser(params) {
  return request(config.apiUrl + '/system/manage/appUser/list?' + `${stringify(params)}`);

}

/**
 * 删除
 * @param params
 * @returns {Promise<Object>}
 */
export async function removeAppUser(params) {
  return request(config.apiUrl + '/system/manage/appUser/remove', {
    method: 'POST',
    body: params,
  });
}

/**
 * 增加
 * @param params
 * @returns {Promise<Object>}
 */
export async function addAppUser(params) {
  return request(config.apiUrl + '/system/manage/appUser/save', {
    method: 'POST',
    body: params,
  });
}

/**
 * 获取
 * @param params
 * @returns {Promise<Object>}
 */
export async function findAppUser(params) {
  return request(config.apiUrl + '/system/manage/appUser/find/' + `${params.id}`);
}

/**
 * 获取当前用户
 * @param params
 * @returns {Promise<Object>}
 */
export async function findCurrent() {
  return request(config.apiUrl + '/system/manage/appUser/current');
}
