import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 委托中查询列表
 * @param params
 * @returns {Promise<Object>}
    */
    export async function queryEntrustIng(params) {
     return request(config.apiUrl+'/exchange/entrustIng/list?'+`${stringify(params)}`);

    }

    /**
    * 委托中删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeEntrustIng(params) {
     return request(config.apiUrl+'/exchange/entrustIng/remove', {
        method: 'POST',
        body: params,
        });
        }

        /**
        * 委托中增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addEntrustIng(params) {
     return request(config.apiUrl+'/exchange/entrustIng/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * 委托中获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findEntrustIng(params) {
     return request(config.apiUrl+'/exchange/entrustIng/find/'+`${params.id}`);
                }

/**
* 委托中撤銷
* @param params
* @returns {Promise<Object>}
*/
export async function revertEntrustIng(params) {
  return request(config.apiUrl+'/exchange/entrustIng/cancel', {
    method: 'POST',
    body: params,
  });
}
