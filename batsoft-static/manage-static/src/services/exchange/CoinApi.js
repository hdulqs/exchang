import {stringify} from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 币种查询列表
 * @param params
 * @returns {Promise<Object>}
 */
export async function queryCoin(params) {
  return request(config.apiUrl + '/exchange/coin/list?' + `${stringify(params)}`);

}

/**
 * 币种删除
 * @param params
 * @returns {Promise<Object>}
 */
export async function removeCoin(params) {
  return request(config.apiUrl + '/exchange/coin/remove', {
    method: 'POST',
    body: params,
  });
}

/**
 * 修改状态
 * @param params
 * @returns {Promise<Object>}
 */
export async function updateStatus(params) {
  return request(config.apiUrl + '/exchange/coin/updateStatus', {
    method: 'POST',
    body: params,
  });
}
/**
 * 修改状态
 * @param params
 * @returns {Promise<Object>}
 */
export async function closeStatus(params) {
  return request(config.apiUrl + '/exchange/coin/closeStatus', {
    method: 'POST',
    body: params,
  });
}
/**
 * 设置定价币
 * @param params
 * @returns {Promise<Object>}
 */
export async function setPriceCoin(params) {
  return request(config.apiUrl + '/exchange/coin/priceCoin', {
    method: 'POST',
    body: params,
  });
}

/**
 * 允许充币
 * @param params
 * @returns {Promise<Object>}
 */
export async function allowRecharge(params) {
  return request(config.apiUrl + '/exchange/coin/allowRecharge', {
    method: 'POST',
    body: params,
  });
}

/**
 * 允许提币
 * @param params
 * @returns {Promise<Object>}
 */
export async function allowDeposit(params) {
  return request(config.apiUrl + '/exchange/coin/allowDeposit', {
    method: 'POST',
    body: params,
  });
}

/**
 * 设置交易对
 * @param params
 * @returns {Promise<Object>}
 */
export async function setTrade(params) {
  return request(config.apiUrl + '/exchange/coin/setTrade', {
    method: 'POST',
    body: params,
  });
}


/**
 * 获取定价币
 * @param params
 * @returns {Promise<Object>}
 */
export async function findPriceCoins(params) {
  return request(config.apiUrl + '/exchange/coin/priceCoins');
}


/**
 * 币种增加
 * @param params
 * @returns {Promise<Object>}
 */
export async function addCoin(params) {
  return request(config.apiUrl + '/exchange/coin/save', {
    method: 'POST',
    body: params,
  });
}

/**
 * 币种获取
 * @param params
 * @returns {Promise<Object>}
 */
export async function findCoin(params) {
  return request(config.apiUrl + '/exchange/coin/find/' + `${params.id}`);
}
