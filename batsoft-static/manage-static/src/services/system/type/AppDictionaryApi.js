import {stringify} from 'qs';
import request from '../../../utils/request';
import config from '../../../common/config';

/**
 * 数据字典查询列表
 * @param params
 * @returns {Promise<Object>}
 */
export async function queryAppDictionary(params) {
  return request(config.apiUrl + '/system/type/appDictionary/list?' + `${stringify(params)}`);

}

/**
 * 数据字典删除
 * @param params
 * @returns {Promise<Object>}
 */
export async function removeAppDictionary(params) {
  return request(config.apiUrl + '/system/type/appDictionary/remove', {
    method: 'POST',
    body: params,
  });
}

/**
 * 数据字典增加
 * @param params
 * @returns {Promise<Object>}
 */
export async function addAppDictionary(params) {
  return request(config.apiUrl + '/system/type/appDictionary/save', {
    method: 'POST',
    body: params,
  });
}

/**
 * 数据字典获取
 * @param params
 * @returns {Promise<Object>}
 */
export async function findAppDictionary(params) {
  return request(config.apiUrl + '/system/type/appDictionary/find/' + `${params.id}`);
}

/**
 *数据字典列表
 * @param params
 * @returns {Promise<Object>}
 */
export async function queryAppDictionaryTreeList(params) {
  return request(config.apiUrl + '/system/type/appDictionary/list');

}


/**
 * 数据字典树
 * @param params
 * @returns {Promise<Object>}
 */
export async function queryAppDictionaryTree(params) {
  return request(config.apiUrl + '/system/type/appDictionary/treeList');

}


/**
 * 通过key 查询字典树
 * @param params
 * @returns {Promise<Object>}
 */
export  async function findByKey(params) {
  return request(config.apiUrl + '/system/type/appDictionary/findByKey/' + `${params.key}`);
}
