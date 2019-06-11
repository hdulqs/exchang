/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-19 10:21:01
 */

package com.batsoft.model.module.cms.vo;

import com.batsoft.model.module.cms.ArticleChannel;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 *
 * <p>ArticleChannelVo</p>
 * @author: Bat Admin
 * @Date :  2018-04-19 10:21:01
 */
@Data
@ToString
public class ArticleChannelVo extends ArticleChannel {

    List<ArticleVo> articleVos;
}
