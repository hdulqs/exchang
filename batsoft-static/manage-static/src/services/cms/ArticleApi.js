import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 文章查询列表
 * @param params
 * @returns {Promise<Object>}
    */
    export async function queryArticle(params) {
     return request(config.apiUrl+'/cms/article/list?'+`${stringify(params)}`);

    }

    /**
    * 文章删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeArticle(params) {
     return request(config.apiUrl+'/cms/article/remove', {
        method: 'POST',
        body: params,
        });
        }

        /**
        * 文章增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addArticle(params) {
     return request(config.apiUrl+'/cms/article/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * 文章获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findArticle(params) {
     return request(config.apiUrl+'/cms/article/find/'+`${params.id}`);
                }
