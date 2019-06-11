import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 委托详情查询列表
 * @param params
 * @returns {Promise<Object>}
    */
    export async function queryEntrustInfo(params) {
     return request(config.apiUrl+'/exchange/entrustInfo/list?'+`${stringify(params)}`);

    }

    /**
    * 委托详情删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeEntrustInfo(params) {
     return request(config.apiUrl+'/exchange/entrustInfo/remove?date='+`${params.date}`, {
        method: 'POST',
        body: params,
        });
        }

        /**
        * 委托详情增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addEntrustInfo(params) {
     return request(config.apiUrl+'/exchange/entrustInfo/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * 委托详情获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findEntrustInfo(params) {
     return request(config.apiUrl+'/exchange/entrustInfo/find/'+`${params.id}`);
                }


