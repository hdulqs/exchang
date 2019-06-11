/**
* Copyright:    http://www.batsoft.cn
* @author:      Yuan Zhicheng
* @version:     V1.0
* @Date:        2017-11-28 17:39:53 
*/

package com.batsoft.model.module.otc.vo;

import com.batsoft.model.module.otc.ProjectOrder;
import lombok.Data;
import lombok.ToString;

/**
* 
* <p>ProjectOrder</p>
* @author: Yuan Zhicheng
* @Date :  2017-11-28 17:39:53 
*/
@Data
@ToString
public class ProjectOrderVo extends ProjectOrder {
    /**
     * 项目用户昵称
     */
    private String projectUserNick;
    /**
     * 项目用户名
     */
    private String projectUserName;
    /**
     * 项目用户ID
     */
    private String projectUserId;
}
