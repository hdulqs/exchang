import {stringify} from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 会员查询列表
 * @param params
 * @returns {Promise<Object>}
 */
export async function queryUser(params) {
  return request(config.apiUrl + '/member/user/list?' + `${stringify(params)}`);

}

/**
 * 会员删除
 * @param params
 * @returns {Promise<Object>}
 */
export async function removeUser(params) {
  return request(config.apiUrl + '/member/user/remove', {
    method: 'POST',
    body: params,
  });
}
/**
 * 身份审核通过
 * @param params
 * @returns {Promise<Object>}
 */
export async function review(params) {
  return request(config.apiUrl + '/member/user/review', {
    method: 'POST',
    body: params,
  });
}

/**
 * 会员增加
 * @param params
 * @returns {Promise<Object>}
 */
export async function addUser(params) {
  return request(config.apiUrl + '/member/user/save', {
    method: 'POST',
    body: params,
  });
}

/**
 * 会员获取
 * @param params
 * @returns {Promise<Object>}
 */
export async function findUser(params) {
  return request(config.apiUrl + '/member/user/find/' + `${params.id}`);
}
