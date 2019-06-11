import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 请求日志查询列表
 * @param params
 * @returns {Promise<Object>}
    */
    export async function queryCoinApiLog(params) {
     return request(config.apiUrl+'/blockchain/coinApiLog/list?'+`${stringify(params)}`);

    }

    /**
    * 请求日志删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeCoinApiLog(params) {
     return request(config.apiUrl+'/blockchain/coinApiLog/remove', {
        method: 'POST',
        body: params,
        });
        }

        /**
        * 请求日志增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addCoinApiLog(params) {
     return request(config.apiUrl+'/blockchain/coinApiLog/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * 请求日志获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findCoinApiLog(params) {
     return request(config.apiUrl+'/blockchain/coinApiLog/find/'+`${params.id}`);
                }
