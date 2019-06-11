import {stringify} from 'qs';
import request from '../../../utils/request';
import config from '../../../common/config';

/**
 * 系统参数查询列表
 * @param params
 * @returns {Promise<Object>}
 */
export async function queryAppConfig(params) {
  return request(config.apiUrl + '/system/config/appConfig/list?' + `${stringify(params)}`);

}

/**
 * 系统参数删除
 * @param params
 * @returns {Promise<Object>}
 */
export async function removeAppConfig(params) {
  return request(config.apiUrl + '/system/config/appConfig/remove', {
    method: 'POST',
    body: params,
  });
}

/**
 * 系统参数增加
 * @param params
 * @returns {Promise<Object>}
 */
export async function addAppConfig(params) {
  return request(config.apiUrl + '/system/config/appConfig/save', {
    method: 'POST',
    body: params,
  });
}

/**
 * 系统参数修改
 * @param params
 * @returns {Promise<Object>}
 */
export async function updateConfig(params) {
  return request(config.apiUrl + '/system/config/appConfig/updateConfig', {
    method: 'POST',
    body: params,
  });
}

/**
 * 系统参数获取
 * @param params
 * @returns {Promise<Object>}
 */
export async function findAppConfig(params) {
  return request(config.apiUrl + '/system/config/appConfig/find/' + `${params.id}`);
}

/**
 * 通过配置类型获取配置属性
 * @param params
 * @returns {Promise<Object>}
 */
export async function findAppConfigByTypeKey(params) {
  return request(config.apiUrl + '/system/config/appConfig/listByTypeKey/' + `${params.typeKey}`);
}
