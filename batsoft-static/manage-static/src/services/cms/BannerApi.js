import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * banner查询列表
 * @param params
 * @returns {Promise<Object>}
    */
    export async function queryBanner(params) {
     return request(config.apiUrl+'/cms/banner/list?'+`${stringify(params)}`);

    }

    /**
    * banner删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeBanner(params) {
     return request(config.apiUrl+'/cms/banner/remove', {
        method: 'POST',
        body: params,
        });
        }

        /**
        * banner增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addBanner(params) {
     return request(config.apiUrl+'/cms/banner/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * banner获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findBanner(params) {
     return request(config.apiUrl+'/cms/banner/find/'+`${params.id}`);
                }
