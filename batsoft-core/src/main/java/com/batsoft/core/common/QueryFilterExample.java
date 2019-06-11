package com.batsoft.core.common;

import tk.mybatis.mapper.entity.Example;

/**
 * Created by Administrator on 2017/5/25.
 */
public class QueryFilterExample extends Example {
    public QueryFilterExample(Class<?> entityClass) {
        super(entityClass);
    }

    /**
     * 获取对应的数据库字段
     * @param property
     * @return
     */
    public String column(String property) {
        if (propertyMap.containsKey(property)) {
            return propertyMap.get(property).getColumn();
        } else if (exists) {
            throw new RuntimeException("当前实体类不包含名为" + property + "的属性!");
        } else {
            return null;
        }
    }

}
