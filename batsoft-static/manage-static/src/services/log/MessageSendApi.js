import { stringify } from 'qs';
import request from '../../utils/request';
import config from '../../common/config';

/**
 * 短信发送日志查询列表
 * @param params
 * @returns {Promise<Object>}
    */
    export async function queryMessageSend(params) {
     return request(config.apiUrl+'/log/messageSend/list?'+`${stringify(params)}`);

    }

    /**
    * 短信发送日志删除
    * @param params
    * @returns {Promise<Object>}
        */
        export async function removeMessageSend(params) {
     return request(config.apiUrl+'/log/messageSend/remove', {
        method: 'POST',
        body: params,
        });
        }

        /**
        * 短信发送日志增加
        * @param params
        * @returns {Promise<Object>}
            */
            export async function addMessageSend(params) {
     return request(config.apiUrl+'/log/messageSend/save', {
            method: 'POST',
            body: params,
            });
            }

            /**
            * 短信发送日志获取
            * @param params
            * @returns {Promise<Object>}
                */
                export async function findMessageSend(params) {
     return request(config.apiUrl+'/log/messageSend/find/'+`${params.id}`);
                }
