import {stringify} from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 交易对查询列表
 * @param params
 * @returns {Promise<Object>}
 */
export async function queryCoinPair(params) {
  return request(config.apiUrl + '/exchange/coinPair/list?' + `${stringify(params)}`);

}

/**
 * 交易对删除
 * @param params
 * @returns {Promise<Object>}
 */
export async function removeCoinPair(params) {
  return request(config.apiUrl + '/exchange/coinPair/remove', {
    method: 'POST',
    body: params,
  });
}

/**
 * 交易对增加
 * @param params
 * @returns {Promise<Object>}
 */
export async function addCoinPair(params) {
  return request(config.apiUrl + '/exchange/coinPair/save', {
    method: 'POST',
    body: params,
  });
}
/**
 * 设置推荐币种
 * @param params
 * @returns {Promise<Object>}
 */
export async function recommendCoin(params) {
  return request(config.apiUrl + '/exchange/coinPair/recommend', {
    method: 'POST',
    body: params,
  });
}
/**
 * 取消推荐币种
 * @param params
 * @returns {Promise<Object>}
 */
export async function unRecommendCoin(params) {
  return request(config.apiUrl + '/exchange/coinPair/unRecommend', {
    method: 'POST',
    body: params,
  });
}

/**
 * 交易对获取
 * @param params
 * @returns {Promise<Object>}
 */
export async function findCoinPair(params) {
  return request(config.apiUrl + '/exchange/coinPair/find/' + `${params.id}`);
}
