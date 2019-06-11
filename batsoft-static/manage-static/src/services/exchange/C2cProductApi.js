import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * c2c代币管理查询列表
 * @param params
 * @returns {Promise<Object>}
    */
    export async function queryC2cProduct(params) {
     return request(config.apiUrl+'/exchange/c2cProduct/list?'+`${stringify(params)}`);

    }

    /**
    * c2c代币管理删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeC2cProduct(params) {
     return request(config.apiUrl+'/exchange/c2cProduct/remove', {
        method: 'POST',
        body: params,
        });
        }

        /**
        * c2c代币管理增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addC2cProduct(params) {
     return request(config.apiUrl+'/exchange/c2cProduct/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * c2c代币管理获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findC2cProduct(params) {
     return request(config.apiUrl+'/exchange/c2cProduct/find/'+`${params.id}`);
                }
