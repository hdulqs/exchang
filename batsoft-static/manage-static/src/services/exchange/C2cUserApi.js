import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * c2c商户管理查询列表
 * @param params
 * @returns {Promise<Object>}
    */
    export async function queryC2cUser(params) {
     return request(config.apiUrl+'/exchange/c2cUser/list?'+`${stringify(params)}`);

    }

    /**
    * c2c商户管理删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeC2cUser(params) {
     return request(config.apiUrl+'/exchange/c2cUser/remove', {
        method: 'POST',
        body: params,
        });
        }

        /**
        * c2c商户管理增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addC2cUser(params) {
     return request(config.apiUrl+'/exchange/c2cUser/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * c2c商户管理获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findC2cUser(params) {
     return request(config.apiUrl+'/exchange/c2cUser/find/'+`${params.id}`);
                }
