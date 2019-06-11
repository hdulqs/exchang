import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 查询列表
 * @param params
 * @returns {Promise<Object>}
    */
    export async function queryCoinNotify(params) {
     return request(config.apiUrl+'/blockchain/coinNotify/list?'+`${stringify(params)}`);

    }

    /**
    * 删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeCoinNotify(params) {
     return request(config.apiUrl+'/blockchain/coinNotify/remove', {
        method: 'POST',
        body: params,
        });
        }

        /**
        * 增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addCoinNotify(params) {
     return request(config.apiUrl+'/blockchain/coinNotify/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * 获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findCoinNotify(params) {
     return request(config.apiUrl+'/blockchain/coinNotify/find/'+`${params.id}`);
                }
