/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-09 19:15:36 
*/

package com.batsoft.model.module.cms.vo;

import com.batsoft.model.module.cms.Article;
import com.batsoft.utils.Encodes;
import lombok.ToString;

/**
* 
* <p>ArticleVo</p>
* @author: Bat Admin
* @Date :  2018-04-09 19:15:36 
*/
@ToString
public class ArticleVo extends Article {

    private String content;
    private String shortContent;

    /**
     * 首页文章数据条数
     */
    public static final Integer INDEX_PAGE_SIZE=5;


    @Override
    public String getShortContent() {
        return shortContent;
    }

    @Override
    public void setShortContent(String shortContent) {
        this.shortContent = Encodes.unescapeHtml(shortContent);
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = Encodes.unescapeHtml(content);
    }
}
