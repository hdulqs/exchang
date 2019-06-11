import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 文章单页查询列表
 * @param params
 * @returns {Promise<Object>}
    */
export async function querySingle(params){
  return request(config.apiUrl+'/cms/single/list?'+`${stringify(params)}`);
}

    /**
    * 文章单页删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeSingle(params) {
     return request(config.apiUrl+'/cms/single/remove', {
        method: 'POST',
        body: params,
        });
        }

        /**
        * 文章单页增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addSingle(params) {
     return request(config.apiUrl+'/cms/single/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * 文章单页获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findSingle(params) {
     return request(config.apiUrl+'/cms/single/find/'+`${params.id}`);
                }
