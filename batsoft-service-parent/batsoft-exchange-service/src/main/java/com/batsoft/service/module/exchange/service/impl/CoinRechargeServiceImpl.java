/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-19 17:46:20 
*/
package com.batsoft.service.module.exchange.service.impl;

import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.exchange.CoinRecharge;
import com.batsoft.model.module.exchange.vo.CoinRechargeVo;
import com.batsoft.model.module.member.User;
import com.batsoft.service.module.exchange.dao.CoinRechargeDao;
import com.batsoft.service.module.exchange.service.CoinRechargeService;
import com.batsoft.service.module.member.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* <p> CoinRechargeServiceImpl </p>
* @author: Bat Admin
* @Date :  2018-04-19 17:46:20 
*/
@Service("coinRechargeService")
public class CoinRechargeServiceImpl extends BaseServiceImpl<CoinRecharge, String> implements CoinRechargeService{

@Autowired
private CoinRechargeDao coinRechargeDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserService userService;

    /**
     * c查询用户充值记录
     * @param userId
     * @return
     */
    @Override
    public PageResult findList(Integer page,Integer pageSize,String userId){
        PageResult pageResult = new PageResult();
        int from = pageSize*(page-1);
        Map<String,Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("from", from);
        param.put("pageSize", pageSize);
        List<CoinRechargeVo>  datalist = coinRechargeDao.findList(param);
        Long rows = coinRechargeDao.findPageByUserIdTotal(param);
        pageResult.setRows(datalist);
        pageResult.setPageSize(pageSize);
        pageResult.setPage(page);
        pageResult.setTotal(rows);
        return pageResult;
    }

    @Override
    public JsonResult getTotleRechargesByCode(String type){
        JsonResult jsonResult = new JsonResult();
        try{
            jsonResult = super.createJsonResult(true,null);
            jsonResult.setData(coinRechargeDao.getTotleRechargesByCode(type));
        }catch (Exception e){
            jsonResult = super.createJsonResult(false,null);
            e.printStackTrace();
        }
        return jsonResult;
    }

    @Override
    public JsonResult getRechargesBySort(Integer limit){
        JsonResult jsonResult = super.createJsonResult(false,"");
        try {
            //查询对应币种的充值记录
            List<CoinRechargeVo> coins = coinRechargeDao.getCoins();
            if(coins!=null&&coins.size()>0){
                int i = 0;
                StringBuffer sb = new StringBuffer();
                for(CoinRechargeVo c:coins){
                    String sql = "(SELECT\n" +
                            "\tcoin_code,\n" +
                            "\tSUM(coin_count) AS totleMoney,\n" +
                            "\tuser_name\n" +
                            "FROM\n" +
                            "\texchange_coin_recharge\n" +
                            "where 1=1 and coin_code='"+c.getCoinCode()+"' GROUP BY user_id ORDER BY totleMoney desc limit "+limit+")";
                    if(i==0){
                        sb.append(sql);
                    }else{
                        sb.append(" UNION ").append(sql);
                    }
                    i++;
                }
                //查询对应sql
                if(sb.length()>0){
                    List<CoinRechargeVo> list = jdbcTemplate.query(sb.toString(),new CoinRechargeVoMapper());
                    jsonResult = createJsonResult(true,"");
                    jsonResult.setData(list);
                }
            }
        }catch (Exception e){
            jsonResult = createJsonResult(false,"查询失败-"+e.getMessage());
        }
        return jsonResult;
    }

    @Override
    public void saveOrder(String accountId,String userId,String coinCode,String amount,String fromAddress,String toAddress,String hash) {

        CoinRecharge coinRecharge=new CoinRecharge();

        User user=userService.get(userId);

        coinRecharge.setAccountId(accountId);
        coinRecharge.setCoinCode(coinCode);
        coinRecharge.setCoinCount(new BigDecimal(amount));
        coinRecharge.setFee(new BigDecimal(0));
        coinRecharge.setInAddress(fromAddress);
        coinRecharge.setToAddress(toAddress);
        coinRecharge.setTxOrder(hash);
        coinRecharge.setUserId(userId);

        if(user!=null) {
            coinRecharge.setUserEmail(user.getUserEmail()==null?"":user.getUserEmail());
            coinRecharge.setUserName(user.getUserName()==null?"":user.getUserName());
            coinRecharge.setUserMobile(user.getUserMobile()==null?"":user.getUserMobile());
        }
        coinRecharge.setStatus(CoinRecharge.STATUS1);
        this.save(coinRecharge);

    }

    class CoinRechargeVoMapper implements RowMapper<CoinRechargeVo> {
        public CoinRechargeVo mapRow(ResultSet rs, int rowNum) throws SQLException {
            CoinRechargeVo vo = new CoinRechargeVo();
            vo.setCoinCode(rs.getString("coin_code"));
            vo.setTotleMoney(rs.getBigDecimal("totleMoney"));
            vo.setUserName(rs.getString("user_name"));
            return vo;
        }
    }

}
