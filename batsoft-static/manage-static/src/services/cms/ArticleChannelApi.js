import {stringify} from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 文章类别查询列表
 * @param params
 * @returns {Promise<Object>}
 */
export async function queryArticleChannel(params) {
  return request(config.apiUrl + '/cms/articleChannel/list?' + `${stringify(params)}`);

}

/**
 * 文章类别删除
 * @param params
 * @returns {Promise<Object>}
 */
export async function removeArticleChannel(params) {
  return request(config.apiUrl + '/cms/articleChannel/remove', {
    method: 'POST',
    body: params,
  });
}

/**
 * 文章类别增加
 * @param params
 * @returns {Promise<Object>}
 */
export async function addArticleChannel(params) {
  return request(config.apiUrl + '/cms/articleChannel/save', {
    method: 'POST',
    body: params,
  });
}

/**
 * 文章类别获取
 * @param params
 * @returns {Promise<Object>}
 */
export async function findArticleChannel(params) {
  return request(config.apiUrl + '/cms/articleChannel/find/' + `${params.id}`);
}

/**
 * 文章类别获取
 * @param params
 * @returns {Promise<Object>}
 */
export async function findChannels() {
  return request(config.apiUrl + '/cms/articleChannel/findChannels');
}
