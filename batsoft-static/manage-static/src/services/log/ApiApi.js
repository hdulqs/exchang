import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * API日志查询列表
 * @param params
 * @returns {Promise<Object>}
    */
    export async function queryApi(params) {
     return request(config.apiUrl+'/log/api/list?'+`${stringify(params)}`);

    }

    /**
    * API日志删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeApi(params) {
     return request(config.apiUrl+'/log/api/remove', {
        method: 'POST',
        body: params,
        });
        }

        /**
        * API日志增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addApi(params) {
     return request(config.apiUrl+'/log/api/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * API日志获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findApi(params) {
     return request(config.apiUrl+'/log/api/find/'+`${params.id}`);
                }
