import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 历史委托查询列表
 * @param params
 * @returns {Promise<Object>}
    */
    export async function queryEntrustHistory(params) {
     return request(config.apiUrl+'/exchange/entrustHistory/list?'+`${stringify(params)}`);

    }

    /**
    * 历史委托删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeEntrustHistory(params) {
     return request(config.apiUrl+'/exchange/entrustHistory/remove?date='+`${params.date}`, {
        method: 'POST',
        body: params,
        });
        }

        /**
        * 历史委托增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addEntrustHistory(params) {
     return request(config.apiUrl+'/exchange/entrustHistory/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * 历史委托获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findEntrustHistory(params) {
     return request(config.apiUrl+'/exchange/entrustHistory/find/'+`${params.id}`);
                }
