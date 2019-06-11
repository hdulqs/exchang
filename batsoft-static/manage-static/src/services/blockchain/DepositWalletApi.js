import {stringify} from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 提币钱包查询列表
 * @param params
 * @returns {Promise<Object>}
 */
export async function queryDepositWallet(params) {
  return request(config.apiUrl + '/blockchain/depositWallet/list?' + `${stringify(params)}`);

}

/**
 * 提币钱包删除
 * @param params
 * @returns {Promise<Object>}
 */
export async function removeDepositWallet(params) {
  return request(config.apiUrl + '/blockchain/depositWallet/remove', {
    method: 'POST',
    body: params,
  });
}

/**
 * 提币钱包增加
 * @param params
 * @returns {Promise<Object>}
 */
export async function addDepositWallet(params) {
  return request(config.apiUrl + '/blockchain/depositWallet/save', {
    method: 'POST',
    body: params,
  });
}

/**
 * 提币钱包获取
 * @param params
 * @returns {Promise<Object>}
 */
export async function findDepositWallet(params) {
  return request(config.apiUrl + '/blockchain/depositWallet/find/' + `${params.id}`);
}

/**
 * 生成币地址
 * @param params
 * @returns {Promise<Object>}
 */
export async function createAddress(params) {
  return request(config.apiUrl + '/blockchain/depositWallet/createAddress', {
    method: 'POST',
    body: params,
  });
}

