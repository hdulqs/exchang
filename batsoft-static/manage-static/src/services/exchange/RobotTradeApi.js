import {stringify} from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 交易机器人查询列表
 * @param params
 * @returns {Promise<Object>}
 */
export async function queryRobotTrade(params) {
  return request(config.apiUrl + '/exchange/robotTrade/list?' + `${stringify(params)}`);

}

/**
 * 交易机器人删除
 * @param params
 * @returns {Promise<Object>}
 */
export async function removeRobotTrade(params) {
  return request(config.apiUrl + '/exchange/robotTrade/remove', {
    method: 'POST',
    body: params,
  });
}

/**
 * 交易机器人开启
 * @param params
 * @returns {Promise<Object>}
 */
export async function openRobotTrade(params) {
  return request(config.apiUrl + '/exchange/robotTrade/open', {
    method: 'POST',
    body: params,
  });
}

/**
 * 交易机器人关闭
 * @param params
 * @returns {Promise<Object>}
 */
export async function stopRobotTrade(params) {
  return request(config.apiUrl + '/exchange/robotTrade/stop', {
    method: 'POST',
    body: params,
  });
}
/**
 * 交易机器人增加
 * @param params
 * @returns {Promise<Object>}
 */
export async function addRobotTrade(params) {
  return request(config.apiUrl + '/exchange/robotTrade/save', {
    method: 'POST',
    body: params,
  });
}

/**
 * 交易机器人获取
 * @param params
 * @returns {Promise<Object>}
 */
export async function findRobotTrade(params) {
  return request(config.apiUrl + '/exchange/robotTrade/find/' + `${params.id}`);
}
