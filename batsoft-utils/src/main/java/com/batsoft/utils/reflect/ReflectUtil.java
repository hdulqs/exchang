
package com.batsoft.utils.reflect;


import com.batsoft.utils.IdGen;
import com.batsoft.utils.StringUtils;
import org.nutz.lang.FailToGetValueException;
import org.nutz.lang.Mirror;

import javax.persistence.Table;

/**
 * <p>反射工具类</p>
 * @author:         Bat Admin
 * @Date :          2015年11月24日 下午7:04:39 
 */
public class ReflectUtil {
	
	
	/**
	 * BaseModel保存和更新时填充的属性
	 * <p> TODO</p>
	 * @author:         Bat Admin
	 * @param:    @param t
	 * @return: void 
	 * @Date :          2016年4月13日 上午10:14:53   
	 * @throws:
	 */
	public static String save(Object t){
		Mirror<?> mirror = Mirror.me(t.getClass());
		String retId=null;
		try {
			String id = (String)mirror.getValue(t, "id");
			if(StringUtils.isEmpty(id)){
				mirror.setValue(t, "createTime",System.currentTimeMillis());
			}

			mirror.setValue(t, "updateTime",System.currentTimeMillis());
		} catch (FailToGetValueException e) {
			System.out.println("not find createTime/updateTime column");
		}
		
		try {
			//如果id存在不用来获取id
			String id = (String)mirror.getValue(t, "id");
			if(StringUtils.isEmpty(id)){
					String key = IdGen.uuid();
				retId=key;
					mirror.setValue(t, "id", key);
			}
		} catch (Exception e) {
			System.out.println("not find id column");
		}
		return retId;
	}
	
	
	/**
	 * 获得model对象的@Table注解的name属性
	 * <p> TODO</p>
	 * @author:         Bat Admin
	 * @param:    @param t
	 * @param:    @return
	 * @return: String 
	 * @Date :          2016年4月13日 上午10:17:39   
	 * @throws:
	 */
	public static String getTableName(Object t){
		Mirror<?> mirror = Mirror.me(t.getClass());
		Table table = mirror.getAnnotation(Table.class);
		if(table!=null){
			return table.name();
		}else{
			return null;
		}
	}
	
	/**
	 * 获得class对象的@Table注解的name属性
	 * <p> TODO</p>
	 * @author:         Bat Admin
	 * @param:    @param t
	 * @param:    @return
	 * @return: String 
	 * @Date :          2016年4月13日 上午10:17:39   
	 * @throws:
	 */
	public static String getTableName(Class t){
		Mirror<?> mirror = Mirror.me(t);
		Table table = mirror.getAnnotation(Table.class);
		if(table!=null){
			return table.name();
		}else{
			return null;
		}
	}
	
	
	public static Class getClass(Object t){
		Mirror<?> mirror = Mirror.me(t.getClass());
		return mirror.getClass();
	}
	
	
	
}
