import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 用户银行卡管理查询列表
 * @param params
 * @returns {Promise<Object>}
    */
    export async function queryBankcard(params) {
     return request(config.apiUrl+'/member/bankcard/list?'+`${stringify(params)}`);

    }

    /**
    * 用户银行卡管理删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeBankcard(params) {
     return request(config.apiUrl+'/member/bankcard/remove', {
        method: 'POST',
        body: params,
        });
        }

        /**
        * 用户银行卡管理增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addBankcard(params) {
     return request(config.apiUrl+'/member/bankcard/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * 用户银行卡管理获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findBankcard(params) {
     return request(config.apiUrl+'/member/bankcard/find/'+`${params.id}`);
                }
