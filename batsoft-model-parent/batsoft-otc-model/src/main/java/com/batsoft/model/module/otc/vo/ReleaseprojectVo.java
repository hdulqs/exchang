/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Yuan Zhicheng
 * @version: V1.0
 * @Date: 2017-11-19 12:42:46
 */

package com.batsoft.model.module.otc.vo;

import com.batsoft.model.module.otc.Releaseproject;
import lombok.Data;
import lombok.ToString;

/**
 *
 * <p>Releaseproject</p>
 * @author: Yuan Zhicheng
 * @Date :  2017-11-19 12:42:46
 */
@Data
@ToString
public class ReleaseprojectVo extends Releaseproject {
    /**
     * 用户昵称
     */
    private String userNick;
    /**
     * 用户头像
     */
    private String userAvatar;
    /**
     * 被信任人数
     */
    private Integer trustNum;
    /**
     * 累计交易笔数
     */
    private Integer exchangeNum;

}
