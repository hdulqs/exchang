import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 交易流水查询列表
 * @param params
 * @returns {Promise<Object>}
    */
    export async function queryCoinOrder(params) {
     return request(config.apiUrl+'/blockchain/coinOrder/list?'+`${stringify(params)}`);

    }

    /**
    * 交易流水删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeCoinOrder(params) {
     return request(config.apiUrl+'/blockchain/coinOrder/remove', {
        method: 'POST',
        body: params,
        });
        }

        /**
        * 交易流水增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addCoinOrder(params) {
     return request(config.apiUrl+'/blockchain/coinOrder/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * 交易流水获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findCoinOrder(params) {
     return request(config.apiUrl+'/blockchain/coinOrder/find/'+`${params.id}`);
                }
