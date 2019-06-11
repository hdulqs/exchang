import {stringify} from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * c2c交易订单表查询列表
 * @param params
 * @returns {Promise<Object>}
 */
export async function queryC2cOrder(params) {
  return request(config.apiUrl + '/exchange/c2cOrder/list?' + `${stringify(params)}`);

}

/**
 * c2c交易订单表删除
 * @param params
 * @returns {Promise<Object>}
 */
export async function removeC2cOrder(params) {
  return request(config.apiUrl + '/exchange/c2cOrder/remove', {
    method: 'POST',
    body: params,
  });
}

/**
 * c2c交易订单表增加
 * @param params
 * @returns {Promise<Object>}
 */
export async function addC2cOrder(params) {
  return request(config.apiUrl + '/exchange/c2cOrder/save', {
    method: 'POST',
    body: params,
  });
}

/**
 * c2c交易订单表获取
 * @param params
 * @returns {Promise<Object>}
 */
export async function findC2cOrder(params) {
  return request(config.apiUrl + '/exchange/c2cOrder/find/' + `${params.id}`);
}

/**
 * c2c 訂單通過
 * @param params
 * @returns {Promise<Object>}
 */
export async function c2cOrderPass(params) {
  return request(config.apiUrl + '/exchange/c2cOrder/passStatus', {
    method: 'POST',
    body: params,
  });

}
/**
 * c2c 訂單拒绝
 * @param params
 * @returns {Promise<Object>}
 */
export async function c2cRefuseOrder(params) {
  return request(config.apiUrl + '/exchange/c2cOrder/refuseStatus', {
    method: 'POST',
    body: params,
  });
}

