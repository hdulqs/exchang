import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 冻结明细查询列表
 * @param params
 * @returns {Promise<Object>}
    */
    export async function queryCustomerAccountFreeze(params) {
     return request(config.apiUrl+'/exchange/customerAccountFreeze/list?'+`${stringify(params)}`);

    }

    /**
    * 冻结明细删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeCustomerAccountFreeze(params) {
     return request(config.apiUrl+'/exchange/customerAccountFreeze/remove?date='+`${params.date}`, {
        method: 'POST',
        body: params,
        });
        }

        /**
        * 冻结明细增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addCustomerAccountFreeze(params) {
     return request(config.apiUrl+'/exchange/customerAccountFreeze/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * 冻结明细获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findCustomerAccountFreeze(params) {
     return request(config.apiUrl+'/exchange/customerAccountFreeze/find/'+`${params.id}`);
                }
