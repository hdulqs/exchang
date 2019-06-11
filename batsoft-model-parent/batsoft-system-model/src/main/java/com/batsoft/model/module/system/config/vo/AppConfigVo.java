/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-06-26 17:29:20 
*/
package com.batsoft.model.module.system.config.vo;

import com.batsoft.model.module.system.config.AppConfig;
import com.batsoft.model.module.system.config.AppConfigType;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
* <p>AppConfig</p>
* @author: Bat Admin
* @Date :  2017-06-26 17:29:20 
*/

@Data
@ToString
public class AppConfigVo extends AppConfig {

    List<AppConfigTypeVo> types;
}
