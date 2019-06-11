import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 查询列表
 * @param params
 * @returns {Promise<Object>}
    */
    export async function queryFinance(params) {
     return request(config.apiUrl+'/member/finance/list?'+`${stringify(params)}`);

    }

    /**
    * 删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeFinance(params) {
     return request(config.apiUrl+'/member/finance/remove', {
        method: 'POST',
        body: params,
        });
        }

        /**
        * 增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addFinance(params) {
     return request(config.apiUrl+'/member/finance/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * 获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findFinance(params) {
     return request(config.apiUrl+'/member/finance/find/'+`${params.id}`);
                }
