import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 钱包查询列表
 * @param params
 * @returns {Promise<Object>}
    */
    export async function queryWallet(params) {
     return request(config.apiUrl+'/blockchain/wallet/list?'+`${stringify(params)}`);

    }

    /**
    * 钱包删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeWallet(params) {
     return request(config.apiUrl+'/blockchain/wallet/remove', {
        method: 'POST',
        body: params,
        });
        }

        /**
        * 钱包增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addWallet(params) {
     return request(config.apiUrl+'/blockchain/wallet/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * 钱包获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findWallet(params) {
     return request(config.apiUrl+'/blockchain/wallet/find/'+`${params.id}`);
                }
