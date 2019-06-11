import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 提币地址查询列表
 * @param params
 * @returns {Promise<Object>}
    */
    export async function queryWithdrawAddress(params) {
     return request(config.apiUrl+'/exchange/withdrawAddress/list?'+`${stringify(params)}`);

    }

    /**
    * 提币地址删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeWithdrawAddress(params) {
     return request(config.apiUrl+'/exchange/withdrawAddress/remove', {
        method: 'POST',
        body: params,
        });
        }

        /**
        * 提币地址增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addWithdrawAddress(params) {
     return request(config.apiUrl+'/exchange/withdrawAddress/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * 提币地址获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findWithdrawAddress(params) {
     return request(config.apiUrl+'/exchange/withdrawAddress/find/'+`${params.id}`);
                }
