import { stringify } from 'qs';
import request from '../utils/request';
import config from '../common/config';

// 查询左侧导航栏
export async function getMenuDate() {
  return request(config.apiUrl+ '/system/appmenu/appMenu/menu');
}

//查詢系統全局信息
export async function queryGlobInfo() {
  return request(config.apiUrl+ '/globInfo');
}
