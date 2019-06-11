import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 友情链接查询列表
 * @param params
 * @returns {Promise<Object>}
    */
    export async function queryFriend(params) {
     return request(config.apiUrl+'/cms/friend/list?'+`${stringify(params)}`);

    }

    /**
    * 友情链接删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeFriend(params) {
     return request(config.apiUrl+'/cms/friend/remove', {
        method: 'POST',
        body: params,
        });
        }

        /**
        * 友情链接增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addFriend(params) {
     return request(config.apiUrl+'/cms/friend/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * 友情链接获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findFriend(params) {
     return request(config.apiUrl+'/cms/friend/find/'+`${params.id}`);
                }
