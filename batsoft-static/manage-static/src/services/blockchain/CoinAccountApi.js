import {stringify} from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 币地址查询列表
 * @param params
 * @returns {Promise<Object>}
 */
export async function queryCoinAccount(params) {
  return request(config.apiUrl + '/blockchain/coinAccount/list?' + `${stringify(params)}`);

}

/**
 * 币地址删除
 * @param params
 * @returns {Promise<Object>}
 */
export async function removeCoinAccount(params) {
  return request(config.apiUrl + '/blockchain/coinAccount/remove', {
    method: 'POST',
    body: params,
  });
}

/**
 * 币地址增加
 * @param params
 * @returns {Promise<Object>}
 */
export async function addCoinAccount(params) {
  return request(config.apiUrl + '/blockchain/coinAccount/save', {
    method: 'POST',
    body: params,
  });
}

/**
 * 生成币地址
 * @param params
 * @returns {Promise<Object>}
 */
export async function createAddress(params) {
  return request(config.apiUrl + '/blockchain/coinAccount/createAddress', {
    method: 'POST',
    body: params,
  });
}



/**
 * 币地址获取
 * @param params
 * @returns {Promise<Object>}
 */
export async function findCoinAccount(params) {
  return request(config.apiUrl + '/blockchain/coinAccount/find/' + `${params.id}`);
}
