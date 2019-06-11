import request from '../utils/request';
import {stringify} from "qs";
import config from "../common/config";

/**
 * 用户登陆
 * @param params
 * @returns {Promise<Object>}
 */
export async function userLogin(params) {
  return request(config.apiUrl + '/loginCheck?'+`${stringify(params)}`);
}

/**
 * 退出登陆
 * @returns {Promise<Object>}
 */
export async function logout() {
  return request(config.apiUrl + '/logout');
}
