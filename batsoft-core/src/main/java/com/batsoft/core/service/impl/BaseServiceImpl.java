/**
 * Copyright:   www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2016年12月13日 下午4:57:13
 */
package com.batsoft.core.service.impl;

import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.common.validator.BeanValidators;
import com.batsoft.core.dao.BaseDao;
import com.batsoft.core.model.SaveResult;
import com.batsoft.core.service.BaseService;
import com.batsoft.utils.reflect.ReflectUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> TODO</p>
 * @author: Bat Admin
 * @Date :          2016年12月13日 下午4:57:13 
 */
@Service
@Transactional(rollbackFor = Exception.class)
public abstract class BaseServiceImpl<T extends Serializable, PK extends Serializable> implements BaseService<T, PK> {

    @Autowired
    protected BaseDao<T, PK> dao;

    /**
     * 验证Bean实例对象
     */
    @Autowired
    protected Validator validator;

    @Override
    public SaveResult save(T t) {
        SaveResult saveResult = new SaveResult();
        saveResult.setId(ReflectUtil.save(t));
        saveResult.setExcuse(dao.insertSelective(t));
        return saveResult;
    }

    @Override
    public void saveAll(List<T> l) {
        for (T t : l) {
            ReflectUtil.save(t);
            save(t);
        }
    }

    @Override
    public boolean delete(PK pk) {
        try {
            dao.deleteByPrimaryKey(pk);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean delete(QueryFilter filter) {

        try {
            dao.deleteByExample(filter.getExample());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public int update(T t) {
        ReflectUtil.save(t);
        return this.dao.updateByPrimaryKeySelective(t);
    }

    @Override
    public void updateNull(T t) {
        ReflectUtil.save(t);
        this.dao.updateByPrimaryKey(t);//null值会更新
    }

    @Override
    public T get(PK pk) {
        return this.dao.selectByPrimaryKey(pk);
    }

    @Override
    public T get(QueryFilter filter) {
        try {
            List<T> list = dao.selectByExample(filter.getExample());
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public List<T> findAll() {
        return dao.selectAll();
    }


    @Override
    public List<T> find(QueryFilter filter) {
        return this.dao.selectByExample(filter.getExample());
    }


    @Override
    public Long getCount(T t) {
        int count = this.dao.selectCount(t);
        return Integer.valueOf(count).longValue();
    }


    @Override
    public Long getCount(QueryFilter filter) {
        int count = dao.selectCountByExample(filter.getExample());
        return Integer.valueOf(count).longValue();

    }


    /**  单表分页方法
     * <p> TODO</p>
     * @author: Bat Admin
     * @param:    @param filter
     * @return: void
     * @Date :          2016年3月17日 上午10:46:29
     * @throws:
     */
    @Override
    public PageResult findPage(QueryFilter filter) {
        Page<T> p = null;
        PageResult pageRet = new PageResult();
        if (Integer.valueOf(-1).compareTo(filter.getPageSize()) == 0) {
            //pageSize = -1 时
            //pageHelper传pageSize参数传0查询全部
            p = PageHelper.startPage(filter.getPage(), 0);
        } else {
            p = PageHelper.startPage(filter.getPage(), filter.getPageSize());
        }

        dao.selectByExample(filter.getExample());

        // 设置分页数据
        pageRet.setRows(p.getResult());
        // 设置总记录数
        pageRet.setTotal(p.getTotal());

        pageRet.setPage(filter.getPage());
        pageRet.setPageSize(filter.getPageSize());

        Map<String, Long> map = new HashMap<>();
        map.put("total", p.getTotal());
        map.put("pageSize", Long.valueOf(filter.getPageSize()));
        map.put("current", Long.valueOf(filter.getPage()));

        pageRet.setPagination(map);


        return pageRet;
    }


    /**
     * 服务端参数有效性验证,返回json
     *
     * @param object 验证的实体对象
     * @param groups 验证组
     * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
     */
    protected boolean beanValidatorForJson(Object object, Class<?>... groups) {
        try {
            BeanValidators.validateWithException(validator, object, groups);
        } catch (ConstraintViolationException ex) {
            return false;
        }
        return true;
    }

    @Override
    public JsonResult createJsonResult(boolean flag,String msg){
        JsonResult jsonResult = new JsonResult();
        if(flag){
            jsonResult.setSuccess(true);
            jsonResult.setCode(Constants.SUCCESS);
            if(StringUtil.isNotEmpty(msg)){
                jsonResult.setMsg(msg);
            }else{
                jsonResult.setMsg("成功");
            }
        }else{
            jsonResult.setSuccess(false);
            jsonResult.setCode(Constants.FAILED);
            if(StringUtil.isNotEmpty(msg)){
                jsonResult.setMsg(msg);
            }else{
                jsonResult.setMsg("失败");
            }
        }
        return jsonResult;
    }
}
