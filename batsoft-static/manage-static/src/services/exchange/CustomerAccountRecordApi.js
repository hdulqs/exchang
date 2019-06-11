import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 成交明细查询列表
 * @param params
 * @returns {Promise<Object>}
    */
    export async function queryCustomerAccountRecord(params) {
     return request(config.apiUrl+'/exchange/customerAccountRecord/list?'+`${stringify(params)}`);

    }

    /**
    * 成交明细删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeCustomerAccountRecord(params) {
     return request(config.apiUrl+'/exchange/customerAccountRecord/remove?date='+`${params.date}`, {
        method: 'POST',
        body: params,
        });
        }

        /**
        * 成交明细增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addCustomerAccountRecord(params) {
     return request(config.apiUrl+'/exchange/customerAccountRecord/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * 成交明细获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findCustomerAccountRecord(params) {
     return request(config.apiUrl+'/exchange/customerAccountRecord/find/'+`${params.id}`);
                }
