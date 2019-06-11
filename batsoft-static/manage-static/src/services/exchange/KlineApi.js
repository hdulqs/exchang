import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * K线数据查询列表
 * @param params
 * @returns {Promise<Object>}
    */
    export async function queryKline(params) {
     return request(config.apiUrl+'/exchange/kline/list?'+`${stringify(params)}`);

    }

    /**
    * K线数据删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeKline(params) {
     return request(config.apiUrl+'/exchange/kline/remove', {
        method: 'POST',
        body: params,
        });
        }

        /**
        * K线数据增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addKline(params) {
     return request(config.apiUrl+'/exchange/kline/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * K线数据获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findKline(params) {
     return request(config.apiUrl+'/exchange/kline/find/'+`${params.id}`);
                }
