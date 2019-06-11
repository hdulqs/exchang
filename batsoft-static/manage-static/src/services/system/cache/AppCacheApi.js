import {stringify} from 'qs';
import request from '../../../utils/request';
import config from '../../../common/config';

/**
 * 缓存查询列表
 * @param params
 * @returns {Promise<Object>}
 */
export async function queryAppCache(params) {
  return request(config.apiUrl + '/system/cache/appCache/list?' + `${stringify(params)}`);

}

/**
 * 缓存删除
 * @param params
 * @returns {Promise<Object>}
 */
export async function removeAppCache(params) {
  return request(config.apiUrl + '/system/cache/appCache/remove', {
    method: 'POST',
    body: params,
  });
}

/**
 * 缓存增加
 * @param params
 * @returns {Promise<Object>}
 */
export async function addAppCache(params) {
  return request(config.apiUrl + '/system/cache/appCache/save', {
    method: 'POST',
    body: params,
  });
}

/**
 * 缓存获取
 * @param params
 * @returns {Promise<Object>}
 */
export async function findAppCache(params) {
  return request(config.apiUrl + '/system/cache/appCache/find/' + `${params.id}`);
}

/**
 * 批量更新缓存
 * @param params
 * @returns {Promise<Object>}
 */
export async function refreshCache() {
  return request(config.apiUrl + '/system/cache/appCache/updateAppConfigCache');
}
