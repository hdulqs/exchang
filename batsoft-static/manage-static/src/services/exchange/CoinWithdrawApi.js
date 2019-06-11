import {stringify} from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 提币查询列表
 * @param params
 * @returns {Promise<Object>}
 */
export async function queryCoinWithdraw(params) {
  return request(config.apiUrl + '/exchange/coinWithdraw/list?' + `${stringify(params)}`);

}

/**
 * 提币删除
 * @param params
 * @returns {Promise<Object>}
 */
export async function removeCoinWithdraw(params) {
  return request(config.apiUrl + '/exchange/coinWithdraw/remove', {
    method: 'POST',
    body: params,
  });
}

/**
 * 提币通过
 * @param params
 * @returns {Promise<Object>}
 */
export async function passStatus(params) {
  return request(config.apiUrl + '/exchange/coinWithdraw/passStatus', {
    method: 'POST',
    body: params,
  });
}
/**
 * 提币拒绝
 * @param params
 * @returns {Promise<Object>}
 */
export async function refuseStatus(params) {
  return request(config.apiUrl + '/exchange/coinWithdraw/refuseStatus', {
    method: 'POST',
    body: params,
  });
}
/**
 * 提币增加
 * @param params
 * @returns {Promise<Object>}
 */
export async function addCoinWithdraw(params) {
  return request(config.apiUrl + '/exchange/coinWithdraw/save', {
    method: 'POST',
    body: params,
  });
}

/**
 * 提币获取
 * @param params
 * @returns {Promise<Object>}
 */
export async function findCoinWithdraw(params) {
  return request(config.apiUrl + '/exchange/coinWithdraw/find/' + `${params.id}`);
}
