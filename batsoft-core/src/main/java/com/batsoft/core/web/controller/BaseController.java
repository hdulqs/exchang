/**
 * Copyright:   www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017年1月9日 下午3:08:32
 */
package com.batsoft.core.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> BaseController 基类</p>
 * @author: Bat Admin
 * @Date :          2017年1月9日 下午3:08:32 
 */
@Slf4j
public abstract class BaseController<T extends Serializable, PK extends Serializable> extends GenericController {
    
	private Class<T> entityClass = null;
    
    protected BaseService<T, PK> service;


    public abstract void setService(BaseService<T, PK> service);

    @SuppressWarnings("unchecked")
    public BaseController() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * save or update
     * @param t
     * @return
     */
    protected JsonResult save(T t) {
        JsonResult result = new JsonResult();
        JSONObject retData=new JSONObject();
        Object pk = null;

        try {
            //反射获取主键值
            Field filed =  this.entityClass.getDeclaredField("id");
            filed.setAccessible(true);
            pk=filed.get(t);
            //敏感词过滤 格式校验
            if (!beanValidatorForJson(t)) {
                return this.baseResult;
            }
            if (null==pk) {

                retData.put("type",Constants.SAVE);
                //新增
                service.save(t);
                result.setSuccess(true);
                result.setMsg("保存成功！");
                result.setCode(Constants.SUCCESS);
                result.setData(retData);

            } else {
                //修改
                retData.put("type",Constants.UPDATE);
                int ret = service.update(t);
                result.setSuccess(true);
                result.setMsg("修改成功！");
                result.setCode(Constants.SUCCESS);
                result.setData(retData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("添加/修改失败：" + e.getMessage());
            result.setSuccess(false);
            result.setMsg("操作失败！");
            result.setCode(Constants.FAILED);
        }
        return result;
    }

    /**
     * Base 分页list
     * @param request
     * @return
     */
    protected PageResult pageList(HttpServletRequest request) {
        try {
            QueryFilter filter = new QueryFilter(this.entityClass, request);
//            filter.orderBy("sort asc");
            return service.findPage(filter);
        } catch (Exception e) {
            log.info(this.entityClass.getClass().getName() + " pageList error：" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取数据
     * @param PK
     * @return
     */
    protected JsonResult find(@RequestParam String PK) {
        JsonResult result = new JsonResult();
        try {
            result.setData(service.get((PK) PK));
            result.setSuccess(true);
            result.setMsg("加载成功");
            result.setCode(Constants.SUCCESS);
        } catch (Exception e) {
            log.info(this.entityClass.getClass().getName() + " find error：" + e.getMessage());
            result.setSuccess(false);
            result.setMsg("加载失败,error:" + e.getMessage());
            result.setCode(Constants.FAILED);
        }
        return result;
    }

    /**
     * Base remove
     * @param PKS
     * @return
     */
    protected JsonResult remove(String[] PKS) {
        JsonResult result = new JsonResult();
        List<String> data = new ArrayList<String>();
        try {
            for (String PK : PKS) {
                service.delete((PK) PK);
                data.add(PK);
            }
            result.setSuccess(true);
            result.setMsg("删除成功");
            result.setCode(Constants.SUCCESS);
            result.setData(data);
        } catch (Exception e) {
            log.info("删除失败：" + e.getMessage());
            result.setSuccess(false);
            result.setMsg("删除失败");
            result.setCode(Constants.FAILED);
        }
        return result;
    }

    /**
     * 通过主键获取对象
     * @param PK
     * @return
     */
    protected T findById(String PK){
        return service.get((PK) PK);
    }


}
