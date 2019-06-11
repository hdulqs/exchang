package com.batsoft.core.dao;

import com.batsoft.core.common.JsTree;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */
public interface BaseTreeDao<T,PK> {

    /**
     * <p> 查询子列表</p>
     * @author:         Bat Admin
     * @param:    @param pid
     * @param ： display 是否显示
     * @param:    @return
     * @return: List<T>
     * @Date :          2017年2月23日 下午1:53:06
     * @throws:
     */
    List<T> findChildList(@Param("pid") String pid, @Param("display") Integer display);
    /**
     * 查询树
     * @author:         Bat Admin
     * @param:    @param request
     * @param:    @return
     * @return: List<T>
     * @Date :          2017年2月23日 下午6:22:18
     * @throws:
     */
    List<T> findListTree();

    /**
     * 查询树 返回前端jsTree
     * @return
     */
    List<JsTree> findJsTree();

    /**
     * 查询树 返回前端jsTree
     * 先从 redis查询 redis中如果没有依次从数据库中查询
     * @return
     */
    String findTreeJson();


    /**
     * <p> 查询子列表 返回前端jsTree</p>
     * @author:         Bat Admin
     * @param:    @param id
     * @param : display
     * @param:    @return
     * @return: List<T>
     * @Date :          2017年2月23日 下午1:53:06
     * @throws:
     */
    List<JsTree> findJsTreeChildList(@Param("pid") String pid, @Param("display") Integer display);

    /**
     * 获取父id
     * @param pid
     * @return
     */
    String findParentId(String pid);
    /**
     * 获取父id
     * @param id
     * @return
     */
    String findParentName(String id);
}
