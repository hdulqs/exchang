/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-06-26 15:11:40 
*/
package com.batsoft.model.module.system.config.vo;

import com.batsoft.model.module.system.config.AppConfig;
import com.batsoft.model.module.system.config.AppConfigType;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
* <p>AppConfigTypeVo</p>
* @author: Bat Admin
* @Date :  2017-06-26 15:11:40 
*/
@Data
@ToString
public class AppConfigTypeVo extends AppConfigType {
 List<AppConfig> children;
}
