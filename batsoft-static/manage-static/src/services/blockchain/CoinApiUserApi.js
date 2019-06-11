import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 钱包API用户查询列表
 * @param params
 * @returns {Promise<Object>}
    */
    export async function queryCoinApiUser(params) {
     return request(config.apiUrl+'/blockchain/coinApiUser/list?'+`${stringify(params)}`);

    }

    /**
    * 钱包API用户删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeCoinApiUser(params) {
     return request(config.apiUrl+'/blockchain/coinApiUser/remove', {
        method: 'POST',
        body: params,
        });
        }

        /**
        * 钱包API用户增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addCoinApiUser(params) {
     return request(config.apiUrl+'/blockchain/coinApiUser/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * 钱包API用户获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findCoinApiUser(params) {
     return request(config.apiUrl+'/blockchain/coinApiUser/find/'+`${params.id}`);
                }
