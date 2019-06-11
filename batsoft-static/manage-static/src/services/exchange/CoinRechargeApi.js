import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 充币查询列表
 * @param params
 * @returns {Promise<Object>}
    */
    export async function queryCoinRecharge(params) {
     return request(config.apiUrl+'/exchange/coinRecharge/list?'+`${stringify(params)}`);

    }

    /**
    * 充币删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeCoinRecharge(params) {
     return request(config.apiUrl+'/exchange/coinRecharge/remove', {
        method: 'POST',
        body: params,
        });
        }

        /**
        * 充币增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addCoinRecharge(params) {
     return request(config.apiUrl+'/exchange/coinRecharge/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * 充币获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findCoinRecharge(params) {
     return request(config.apiUrl+'/exchange/coinRecharge/find/'+`${params.id}`);
                }
