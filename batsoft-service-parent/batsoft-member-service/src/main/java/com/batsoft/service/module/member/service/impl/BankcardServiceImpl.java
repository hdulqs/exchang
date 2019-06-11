/**
* Copyright:    http://www.batsoft.cn
* @author:      LouSir
* @version:     V1.0
* @Date:        2018-05-07 09:22:51 
*/
package com.batsoft.service.module.member.service.impl;

import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.common.i18n.Language;
import com.batsoft.model.module.member.Bankcard;
import com.batsoft.model.module.member.User;
import com.batsoft.model.module.member.vo.BankCardVo;
import com.batsoft.service.module.member.dao.BankcardDao;
import com.batsoft.service.module.member.service.BankcardService;

import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
* <p> BankcardServiceImpl </p>
* @author: LouSir
* @Date :  2018-05-07 09:22:51 
*/
@Service("bankcardService")
public class BankcardServiceImpl extends BaseServiceImpl<Bankcard, String> implements BankcardService{

@Autowired
private BankcardDao bankcardDao;
    /**
     * 绑定用户银行卡
     *
     * @return
     */
    @Override
    public JsonResult addBankCard(Bankcard bankcard) {
        JsonResult jsonResult = new JsonResult();
        try {
            User user = UserUtils.getUser();
            bankcard.setUserId(user.getId());
            bankcard.setUserName(user.getUserName());

            //数据校验
            if (!beanValidatorForJson(bankcard)) {

                jsonResult.setSuccess(false);
                jsonResult.setMsg(Language.L("msg_data_error"));
                return jsonResult;
            }

            QueryFilter queryFilter = new QueryFilter(Bankcard.class);
            queryFilter.addFilter("user_id=", user.getId());
            queryFilter.addFilter("status=", Bankcard.STATUS1);
            List<Bankcard> list = this.find(queryFilter);
            /*if (list != null && list.size() > 0) {
                jsonResult.setSuccess(false);
                jsonResult.setCode(Constants.FAILED);
                jsonResult.setMsg(Language.L("msg_card_exists"));
            } else {*/
            if(list!=null&&list.size()>0) {
                for(Bankcard card:list){
                    card.setStatus(Bankcard.STATUS0);
                    this.update(card);
                }
            }
            bankcard.setStatus(Bankcard.STATUS1);
            this.save(bankcard);
            jsonResult.setSuccess(true);
            jsonResult.setCode(Constants.SUCCESS);
            jsonResult.setMsg(Language.L_Success(""));
//            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setSuccess(false);
            jsonResult.setCode(Constants.FAILED);
            jsonResult.setMsg(Language.L("msg_system_error"));
        }
        return jsonResult;
    }

    /**
     * 通过用户ID查询对应的银行卡信息
     * @param
     * @return
     */
    @Override
    public JsonResult findByUserId(){
        User user = UserUtils.getUser();
        JsonResult jsonResult = new JsonResult();
        try{
            if(user!=null){
                Bankcard bankcard = bankcardDao.findByUserId(user.getId());
                if(bankcard!=null){
                    BankCardVo cardVo = new BankCardVo();
                    String bankCardStr = bankcard.getBankCard();
                    if(StringUtils.isNotEmpty(bankcard.getBankCard())&&bankcard.getBankCard().length()>10){
                        bankCardStr = bankcard.getBankCard().substring(0,4)+"******"+bankcard.getBankCard().substring(10,bankcard.getBankCard().length()-1);
                    }
                    cardVo.setBankCardStr(bankCardStr);
                    cardVo.setUserName(bankcard.getUserName());
                    cardVo.setBankName(bankcard.getBankName());
                    jsonResult.setSuccess(true);
                    jsonResult.setCode(Constants.SUCCESS);
                    jsonResult.setData(cardVo);
                    jsonResult.setMsg(Language.L(""));
                }else{
                    jsonResult.setSuccess(true);
                    jsonResult.setCode(Constants.SUCCESS);
                    jsonResult.setMsg(Language.L(""));
                }
            }else{
                jsonResult.setSuccess(false);
                jsonResult.setCode(Constants.FAILED);
                jsonResult.setMsg(Language.L("msg_user_no_exists"));
            }
        }catch (Exception e){
            e.printStackTrace();
            jsonResult.setSuccess(false);
            jsonResult.setCode(Constants.FAILED);
            jsonResult.setMsg(Language.L("msg_system_error"));
        }
        return jsonResult;
    }

}
