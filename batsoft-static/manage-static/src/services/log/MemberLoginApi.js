import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 用户登陆日志查询列表
 * @param params
 * @returns {Promise<Object>}
    */
    export async function queryMemberLogin(params) {
     return request(config.apiUrl+'/log/memberLogin/list?'+`${stringify(params)}`);

    }

    /**
    * 用户登陆日志删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeMemberLogin(params) {
     return request(config.apiUrl+'/log/memberLogin/remove', {
        method: 'POST',
        body: params,
        });
        }

        /**
        * 用户登陆日志增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addMemberLogin(params) {
     return request(config.apiUrl+'/log/memberLogin/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * 用户登陆日志获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findMemberLogin(params) {
     return request(config.apiUrl+'/log/memberLogin/find/'+`${params.id}`);
                }
