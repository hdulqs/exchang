/**
 * Copyright:   batsoft.cn
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2016年3月14日 上午11:48:37
 */
package com.batsoft.core.common;

import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

/**
 * <p>mybatis通用查询组件</p>
 * @author:         Bat Admin
 * @Date :          2016年3月14日 上午11:48:37 
 */
public class QueryFilter  implements  Serializable{
	private QueryFilterExample example = null;
	private Criteria criteria = null;
	
	private Integer page=1;// 要查找第几页
	private Integer pageSize=10;// 每页显示多少条
	private HttpServletRequest request;
	
	/**
	 *  私有构造
	 * <p> TODO</p>
	 * @author:         Bat Admin
	 * @param:
	 */
	private QueryFilter(){}
	
	/**
	 * 构造封装 Example 对象
	 * 
	 * QueryFilter 默认Saas模式
	 * <p> TODO</p>
	 * @author:         Bat Admin
	 * @param:    @param entityClass
	 */
	public QueryFilter(Class<?> entityClass){
		//初始化example对象
		example = new QueryFilterExample(entityClass);
		example.selectProperties();
		//创建查询条件对象
		criteria = example.createCriteria();
	}
	
	/**
	 * dataOrderBy
	 * <p> TODO</p>
	 * @author:         Bat Admin
	 * @param:    @param request
	 * @param:    @param name
	 * @param:    @param value
	 * @return: void 
	 * @Date :          2016年4月6日 下午2:59:18   
	 * @throws:
	 */
	private void orderBy(HttpServletRequest request,String name ,String value){
		if(value.contains("end")){
			value=value.replace("end","");
		}
		if(name.contains("sortOrder")==true){
			String data = request.getParameter("sortName");
			if(data!=null&&!"".equals(data)){
				example.setOrderByClause(example.column(data)+" " + value);
			}
		}
	}
	
	/**
	 * or
	 * <p> TODO</p>
	 * @author:         Bat Admin
	 * @param:    @param condition
	 * @param:    @param value
	 * @param:    @return
	 * @return: QueryFilter 
	 * @Date :          2016年4月18日 下午7:33:31   
	 * @throws:
	 */
	public QueryFilter or(String condition, String value){
		Criteria newcriteria = example.createCriteria();
		example.or(addCriteria(condition, value,newcriteria));
		return this;
	}
	
	
	/**
	 * Remote调用时封装的QueryFilter
	 * 
	 * 主要自动封装dataTable参数
	 * <p> TODO</p>
	 * @author:         Bat Admin
	 * @param:    @param entityClass
	 * @param:    @param request
	 */
	public QueryFilter(Class<?> entityClass, HashMap<String, String> requestMap){
		//初始化example对象
		this.example = new QueryFilterExample(entityClass);
		//创建查询条件对象
		this.criteria = example.createCriteria();
		
		if(requestMap!=null&&requestMap.size()>0){
			
			Set<Entry<String, String>> entrySet = requestMap.entrySet();
			Iterator<Entry<String, String>> iterator = entrySet.iterator();
			
			while (iterator.hasNext()) {
				Entry<String, String> next = iterator.next();
				String name = next.getKey();
				String value = next.getValue();
				
				if (StringUtils.isNotBlank(value)) {

				//	addFilter(name, value);
					
					/**
					 * dataTable分页数据
					 */
					if ("start".equalsIgnoreCase(name)) {
						Integer start=Integer.parseInt(value);
						String length = requestMap.get("length");
						this.setPage(start/Integer.parseInt(length)+1);
					}
					else if ("length".equalsIgnoreCase(name)) {
						this.setPageSize(Integer.parseInt(value));
					}

					//dataTable 自动like查询
					else if(name.contains("_lk")||name.contains("_LK")){
						String[] strArr = name.split("_");
						try {
							criteria.andLike(strArr[0], "%"+value+"%");
						} catch (RuntimeException e) {
						}
					}
					//其它正常处理
					else if(name.contains("_")){
						try {
							addCriteria(name, value,criteria);
						} catch (RuntimeException e) {
						}
					}
					
					
					//dataTable 自动按列排序
				//	orderBy(request, name, value);
					
					
				}
			}
			
		}
	}
	
	
	/**
	 * 带request的QueryFilter
	 * 
	 * 主要自动封装dataTable参数
	 * <p> TODO</p>
	 * @author:         Bat Admin
	 * @param:    @param entityClass
	 * @param:    @param request
	 */
	public QueryFilter(Class<?> entityClass, HttpServletRequest request){
		this.request = request;
		//初始化example对象
		this.example = new QueryFilterExample(entityClass);
		//创建查询条件对象
		this.criteria = example.createCriteria();
		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = StringUtils.trim(request.getParameter(name));
			
			if (StringUtils.isNotBlank(value)) {

			//	addFilter(name, value);
				
				/**
				 * dataTable分页数据
				 */
				if ("page".equalsIgnoreCase(name)) {
					this.setPage(Integer.parseInt(value));
				}
				else if ("pageSize".equalsIgnoreCase(name)) {
					this.setPageSize(Integer.parseInt(value));
				}

				//dataTable 自动like查询
				else if(name.contains("_lk")||name.contains("_LK")){
					String[] strArr = name.split("_");
					try {
						criteria.andLike(strArr[0], "%" + value + "%");
					} catch (RuntimeException e) {
					}
				}
				//其它正常处理
				else if(name.contains("_")){
					try {
						addCriteria(name, value,criteria);
					} catch (RuntimeException e) {
					}
				}

				//sort=name&order=desc
				//dataTable 自动按列排序
				orderBy(request, name, value);
				
				
			}
		}
	
		
	}
	
	
	/**
	 * 添加条件
	 * <p> TODO</p>
	 * @author:         Bat Admin
	 * @param:    @param condition
	 * @param:    @param value
	 * @return: void 
	 * @Date :          2016年4月6日 下午2:57:28   
	 * @throws:
	 */
	private Criteria addCriteria(String condition, Object value,Criteria criteria){
		
		/*-------------------------------------notlike处理-------------------------------------------------*/
		if(condition.contains("_nlk")||condition.contains("_NLK")){
			String[] split = condition.split("_");
			criteria.andNotLike(split[0], value+"");
		}
		/*-------------------------------------like处理-------------------------------------------------*/
		else if(condition.contains("_lk")||condition.contains("_LK")){
			String[] split = condition.split("_");
			criteria.andLike(split[0], value+"");
		}
		/*-------------------------------------in处理-------------------------------------------------*/
		else if(condition.contains("_in")||condition.contains("_IN")){
			String[] split = condition.split("_");
			
			if(value instanceof String){
				String v = ((String) value).trim();
				String[] vArr = v.split(",");
				StringBuffer sb = new StringBuffer();
				for(int i = 0 ; i < vArr.length ; i++ ){
					sb.append("'"+vArr[i]+"'");
					if(i<vArr.length-1){
						sb.append(",");
					}
				}
				criteria.andCondition(" "+split[0]+" "+"in" +" ( " +sb.toString() + " )");
			}else{
				criteria.andIn(split[0], (List)value);
			}
		}/*-------------------------------------not in处理-------------------------------------------------*/
		else if(condition.contains("_notin")||condition.contains("_NOTIN")){
			String[] split = condition.split("_");
			
			if(value instanceof String){
				String v = ((String) value).trim();
				String[] vArr = v.split(",");
				StringBuffer sb = new StringBuffer();
				for(int i = 0 ; i < vArr.length ; i++ ){
					sb.append("'"+vArr[i]+"'");
					if(i<vArr.length-1){
						sb.append(",");
					}
				}
				criteria.andCondition(" "+split[0]+" "+"not in" +" ( " +sb.toString() + " )");
			}else{
				criteria.andNotIn(split[0], (List)value);
			}
		}
		/*-------------------------------------【_EQ】【_GT】【_LT】【_NEQ】处理-------------------------------------------------*/
		/**
		 *        【_EQ】              =
		 *        【_GT】              >
		 *        【_LT】             <
		 *        【_NEQ】          !=
		 */
		else if(condition.contains("_EQ")){
			String[] strArr = condition.split("_");
			criteria.andEqualTo(strArr[0], value);
		}else if(condition.contains("_GT")){
			String[] strArr = condition.split("_");
			criteria.andGreaterThanOrEqualTo(strArr[0], value);
		}else if(condition.contains("_LT")){
			String[] strArr = condition.split("_");
			criteria.andLessThan(strArr[0], value);
		}else if(condition.contains("_NEQ")){
			String[] strArr = condition.split("_");
			criteria.andNotEqualTo(strArr[0], value);
		}
		/*-------------------------------------【=】【>】【<】【!=】处理-------------------------------------------------*/
		else{
			criteria.andCondition(condition, value);
		}
		
		return criteria;
		
	}
	
	
	
	
	/**
	 * 
	 *	手写左边条件，右边用value值
	 *  -------------------------------------------------------- 
	 *  等于 
	 * 	例：select * from app_user where id = 10
	 *  调用： addFilter("id=",10)
	 *  
	 *  -------------------------------------------------------- 
	 *  大于
	 * 	例：select * from app_user where id > 10
	 *  调用： addFilter("id>",10)
	 *  
	 *  -------------------------------------------------------- 
	 *  小于
	 * 	例：select * from app_user where id < 10
	 *  调用： addFilter("id<",10)
	 *  
	 *  -------------------------------------------------------- 
	 *  不等于
	 * 	例：select * from app_user where id != 10
	 *  调用： addFilter("id!=",10)
	 *  
	 *  -------------------------------------------------------- 
	 *  like
	 *  例：select * from app_user where username like '%小明%'
	 *  调用：addFilter("username:_lk",'%小明%');
	 *  调用：addFilter("username:_lk",'%小明');
	 *  调用：addFilter("username:_lk",'小明%');
	 *  
	 *  -------------------------------------------------------- 
	 *  in  两种参数  
	 *  	   一、   "1,2,3"  字符串
	 *  	   二    List<String>  list = new ArrayList<String>()      String  可  换Long,Integer等
	 *  	    list.add("1");
	 *  		list.add("2") 
	 *  例：select * from app_user where id in ("1,2,3")
	 *  调用：addFilter("id_in","1,2,3");
	 *  调用:addFilter("in_in",list);
	 *  
	 *  -------------------------------------------------------- 
	 *  
	 * @author:         Bat Admin
	 * @param:    @return
	 * @return: QueryFilter 
	 * @Date :          2016年3月14日 下午1:21:20   
	 * @throws:
	 */
	public QueryFilter addFilter(String condition, Object value){
		addCriteria(condition,value,criteria);
		return this;
	}

	/**
	 * 排序
	 * ex： value = "sort asc"
	 * @param value
	 */
	public void orderBy(String value){
		example.setOrderByClause(value);
	}
	
	
	/**
	 * <p> TODO</p>
	 * @return:     Integer
	 */
	public Integer getPage() {
		return page;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Integer
	 */
	public void setPage(Integer page) {
		this.page = page;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Integer
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Integer
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}





	/**
	 * <p> TODO</p>
	 * @return:     HttpServletRequest
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/** 
	 * <p> TODO</p>
	 * @return: HttpServletRequest
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Example
	 */
	public Example getExample() {
//     去掉saasId   查询条件   add by Bat Admin 2016/07/04
//		if(this.nosaas){
//			if(saasId!=null&&!"".equals(saasId)){
//				criteria.andCondition("saasId=",saasId);
//			}else{
//				//增加saasId查询条件
//				criteria.andCondition("saasId=", ContextUtil.getSaasId());
//			}
//		}
		return example;
	}
	


	
}
