/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
package com.batsoft.client.otc;

import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.web.controller.GenericController;
import com.batsoft.model.module.member.User;
import com.batsoft.model.module.otc.ExCoin;
import com.batsoft.model.module.otc.ExFinance;
import com.batsoft.model.module.otc.Releaseproject;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.service.module.otc.service.ExCoinService;
import com.batsoft.service.module.otc.service.ExFinanceService;
import com.batsoft.service.module.otc.service.ReleaseprojectService;
import com.batsoft.utils.IdGen;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>OTC项目管理</p>
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
@Api(value = "OtcUserController",description = "用户管理Controller")
@Controller("otcUserController")
@RequestMapping("/otc")
@Slf4j
public class OtcUserController extends GenericController {

    @Autowired
    private ReleaseprojectService releaseprojectService;
    @Autowired
    private ExFinanceService exFinanceService;

    @Autowired
    private ExCoinService exCoinService;

    /**
     * 用户主页
     *
     * @return
     */
    @RequestMapping(value = "/user/{id}",method = RequestMethod.GET)
    public String user (@PathVariable String id,Model model) {
        model.addAttribute(id,id);
        return "otc/user";
    }

    /**
     * 我的帐户
     *
     * @return
     */
    @RequestMapping(value = "/user/finance",method = RequestMethod.GET)
    public String finance () {
        return "otc/user_finance_list";
    }

    /**
     * 获取充币地址
     *
     * @return
     */
    @RequestMapping(value = "/user/coin/address",method = RequestMethod.GET)
    public String address (HttpServletRequest request) {
        User user=UserUtils.getUser();
        String symbol=request.getParameter("symbol");
        QueryFilter finance_filter=new QueryFilter(ExFinance.class);
        finance_filter.addFilter("symbol_EQ",symbol);
        finance_filter.addFilter("userId_EQ",user.getId());
        ExFinance exFinance=exFinanceService.get(finance_filter);
        exFinance.setCoinAddress(IdGen.uuid());
        exFinanceService.update(exFinance);
        return "redirect:/otc/user/recharge/" + symbol;
    }
    /**
     * 充币
     *
     * @return
     */

    @RequestMapping(value = "/user/recharge/{symbol}",method = RequestMethod.GET)
    public String recharge (@PathVariable String symbol,Model model) {

        User user = UserUtils.getUser();
        QueryFilter coin_filter =new QueryFilter(ExCoin.class);
        coin_filter.addFilter("symbol_EQ",symbol);
        coin_filter.addFilter("status_EQ",ExCoin.STATUS1);
        ExCoin coin= exCoinService.get(coin_filter);

        QueryFilter finance_filter=new QueryFilter(ExFinance.class);
        finance_filter.addFilter("symbol_EQ",symbol);
        finance_filter.addFilter("userId_EQ",user.getId());
        ExFinance exFinance=exFinanceService.get(finance_filter);

        model.addAttribute("symbol", symbol);
        model.addAttribute("allowRecharge", coin.getAllowRecharge());
        if(ExCoin.ALLOWRECHARGE1.equals(coin.getAllowRecharge())) {
            model.addAttribute("confirm", coin.getCoinConfirm());
            model.addAttribute("coinAddress", exFinance.getCoinAddress());
            model.addAttribute("coinAddressTag",exFinance.getCoinAddressTag());
        }
        return "otc/user_recharge";
    }

    /**
     * 提币
     *
     * @return
     */
    @RequestMapping(value = "/user/deposit/{symbol}",method = RequestMethod.GET)
    public String deposit (@PathVariable String symbol,Model model) {
        return "otc/user_deposit";
    }

    /**
     * 我发布的项目
     *
     * @return
     */
    @RequestMapping(value = "/project/user/list",method = RequestMethod.GET)
    public String projectList() {
        return "otc/user_project_list";
    }

    /**
     * 我发布的项目数据
     *
     * @return
     */
    @ApiOperation(value = "我发布的项目数据",notes = "根据当前用户查询用户的项目")
    @RequestMapping(value = "/user/projectData",method = RequestMethod.GET)
    @ResponseBody
    public PageResult projectData(HttpServletRequest request) {
        QueryFilter filter=new QueryFilter( Releaseproject.class,request);
        filter.addFilter("userId_EQ", UserUtils.getUser().getId());
        filter.addFilter("del_EQ","0");
        return releaseprojectService.findPage(filter);
    }

    /**
     * 我的账户
     *
     * @return
     */
    @ApiOperation(value = "我的账户",notes = "根据当前账户查询用户的账户")
    @RequestMapping(value = "/user/financeData",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult financeData() {
        JsonResult jsonResult = new JsonResult();
        try {
            jsonResult.setData(exFinanceService.findList());
            jsonResult.setCode(Constants.SUCCESS);
            jsonResult.setSuccess(true);
        }catch (Exception e){
            jsonResult.setCode(Constants.FAILED);
            jsonResult.setSuccess(false);
        }
        return jsonResult;
    }
}
