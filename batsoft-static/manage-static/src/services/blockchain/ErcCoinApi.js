import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 代币查询列表
 * @param params
 * @returns {Promise<Object>}
    */
    export async function queryErcCoin(params) {
     return request(config.apiUrl+'/blockchain/ercCoin/list?'+`${stringify(params)}`);

    }

    /**
    * 代币删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeErcCoin(params) {
     return request(config.apiUrl+'/blockchain/ercCoin/remove', {
        method: 'POST',
        body: params,
        });
        }

        /**
        * 代币增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addErcCoin(params) {
     return request(config.apiUrl+'/blockchain/ercCoin/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * 代币获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findErcCoin(params) {
     return request(config.apiUrl+'/blockchain/ercCoin/find/'+`${params.id}`);
                }
