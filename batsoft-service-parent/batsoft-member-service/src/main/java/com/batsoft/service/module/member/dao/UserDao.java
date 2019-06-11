/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-07-06 17:45:44 
*/
package com.batsoft.service.module.member.dao;

import com.batsoft.core.dao.BaseDao;
import com.batsoft.model.module.member.User;
import com.batsoft.model.module.member.vo.UserVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
* <p>UserDao</p>
* @author: Bat Admin
* @Date :  2017-07-06 17:45:44 
*/
public interface UserDao extends  BaseDao<User, String> {

    User findUser(String userName);

    User findUserByPromotionCode(String promotionCode);

    UserVo findUserInfo(String userName);

    UserVo findUserById(String id);

    List<User> findUnKYCUser(HashMap map);

    List<User> findUserHavePromotionAward(HashMap map);

    Long findUserHavePromotionAwardSum(HashMap map);

    Long findUnKYCUserTotal(HashMap map);
    /**
     * 查询注册人数
     * @return
     */
    Integer findUserNum();

    int updateUserByUserIdSetRealStateSuccess(ArrayList<String> array );

    int updateUserByUserIdSetRealStateFail(ArrayList<String> array );
    /**
     * 未审核人数
     * @return
     */
    Integer findUnauditedUserNum();

    Long findUserByCard(HashMap map);
}
