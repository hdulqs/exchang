import {stringify} from 'qs';
import request from '../../../utils/request';
import config from '../../../common/config';

/**
 * 系统参数类型查询列表
 * @param params
 * @returns {Promise<Object>}
 */
export async function queryAppConfigType(params) {
  return request(config.apiUrl + '/system/config/appConfigType/list?' + `${stringify(params)}`);

}
/**
 * 获取配置类型
 * @returns {Promise<Object>}
 */
export async function queryFindTypes() {
  return request(config.apiUrl + '/system/config/appConfigType/findTypes');

}


/**
 * 系统参数类型删除
 * @param params
 * @returns {Promise<Object>}
 */
export async function removeAppConfigType(params) {
  return request(config.apiUrl + '/system/config/appConfigType/remove', {
    method: 'POST',
    body: params,
  });
}

/**
 * 系统参数类型增加
 * @param params
 * @returns {Promise<Object>}
 */
export async function addAppConfigType(params) {
  return request(config.apiUrl + '/system/config/appConfigType/save', {
    method: 'POST',
    body: params,
  });
}

/**
 * 系统参数类型获取
 * @param params
 * @returns {Promise<Object>}
 */
export async function findAppConfigType(params) {
  return request(config.apiUrl + '/system/config/appConfigType/find/' + `${params.id}`);
}
