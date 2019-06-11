import {stringify} from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 币种账户查询列表
 * @param params
 * @returns {Promise<Object>}
 */
export async function queryCustomerAccount(params) {
  return request(config.apiUrl + '/exchange/customerAccount/list?' + `${stringify(params)}`);

}
/**
 * 用户列表
 * @param params
 * @returns {Promise<Object>}
 */
export async function queryUserList(params) {
  return request(config.apiUrl + '/exchange/customerAccount/userList?' + `${stringify(params)}`);

}

/**
 * 账户核算
 * @param params
 * @returns {Promise<Object>}
 */
export async function checkAccount(params) {
  return request(config.apiUrl + '/exchange/customerAccount/checkAccount/' + `${params.id}`);
}


/**
 * 币种账户删除
 * @param params
 * @returns {Promise<Object>}
 */
export async function removeCustomerAccount(params) {
  return request(config.apiUrl + '/exchange/customerAccount/remove', {
    method: 'POST',
    body: params,
  });
}

/**
 * 币种账户增加
 * @param params
 * @returns {Promise<Object>}
 */
export async function addCustomerAccount(params) {
  return request(config.apiUrl + '/exchange/customerAccount/save', {
    method: 'POST',
    body: params,
  });
}

/**
 * 币种账户获取
 * @param params
 * @returns {Promise<Object>}
 */
export async function findCustomerAccount(params) {
  return request(config.apiUrl + '/exchange/customerAccount/find/' + `${params.id}`);
}


/**
 * 手动充币
 * @param params
 * @returns {Promise<Object>}
 */
export async function depostSave(params) {
  return request(config.apiUrl + '/exchange/customerAccount/depost', {
    method: 'POST',
    body: params,
  });
}
