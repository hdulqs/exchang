import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 订单查询列表
 * @param params
 * @returns {Promise<Object>}
    */
    export async function queryCustomerAccountOrder(params) {
     return request(config.apiUrl+'/exchange/customerAccountOrder/list?'+`${stringify(params)}`);

    }

    /**
    * 订单删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeCustomerAccountOrder(params) {
     return request(config.apiUrl+'/exchange/customerAccountOrder/remove', {
        method: 'POST',
        body: params,
        });
        }

        /**
        * 订单增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addCustomerAccountOrder(params) {
     return request(config.apiUrl+'/exchange/customerAccountOrder/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * 订单获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findCustomerAccountOrder(params) {
     return request(config.apiUrl+'/exchange/customerAccountOrder/find/'+`${params.id}`);
                }
