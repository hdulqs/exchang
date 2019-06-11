package com.batsoft.app.system.template;

import com.alibaba.fastjson.JSON;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.common.Tree;
import com.batsoft.core.web.tpl.Tpl;
import com.batsoft.core.web.tpl.TplManager;
import com.batsoft.utils.RequestUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * 模板的Controller
 */
@Controller("templateController")
@RequestMapping("/system/template")
public class TemplateController {

    private static final Logger log = LoggerFactory
            .getLogger(TemplateController.class);
    @Autowired
    private TplManager tplManager;

    @RequiresPermissions("system:template:manage")
    @RequestMapping(value = "view/manage", method = RequestMethod.GET)
    public String manage() {
        return "system/template/manage";
    }

    @RequiresPermissions("system:template:edit")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult edit(@RequestParam String id, @RequestParam String source) {
        JsonResult jsonResult=new JsonResult();
        try {
            tplManager.save(id,source,false);
            jsonResult.setSuccess(true);
            jsonResult.setMsg("模版修改成功");
            jsonResult.setCode(Constants.SUCCESS);
        }catch (Exception e){
            jsonResult.setSuccess(false);
            jsonResult.setMsg("模版修改失败"+e.getMessage());
            jsonResult.setCode(Constants.FAILED);
        }
        return jsonResult;
    }

    @RequiresPermissions("system:template:manage")
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    @ResponseBody
    public String tree(HttpServletRequest request) {
        String treeJson = "";
        String root = RequestUtils.getQueryParam(request, "pid");
        String id = RequestUtils.getQueryParam(request, "id");
        if ("0".equals(root)) {
            root = "";
        }
        List<Tree> tree = new ArrayList<>();
        if ("#".equals(id)) {
            root = "";
            Tree rootTree = new Tree();
            List<? extends Tpl> tplList = tplManager.getChild(root);
            rootTree.setText("Root");
            rootTree.setId("0");
            rootTree.setIcon("fa fa-folder-o");
            rootTree.setChildren(tplList.size() > 0 ? true : false);
            tree.add(rootTree);
        } else {
            List<? extends Tpl> tplList = tplManager.getChild(root);
            for (Tpl tpl : tplList) {
                Tree treeDate = new Tree();
                treeDate.setId(tpl.getName());
                treeDate.setText(tpl.getFilename());
                treeDate.setChildren(tpl.isDirectory());
                treeDate.setIcon(tpl.isDirectory()==true?"fa fa-folder-o":"fa fa-file-code-o");
                tree.add(treeDate);
            }
        }
        treeJson = JSON.toJSONString(tree);
        return treeJson;
    }

    @RequiresPermissions("system:template:manage")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public PageResult list(HttpServletRequest request) {
        PageResult pageResult=new PageResult();
        String root = RequestUtils.getQueryParam(request, "pid");
        if ("0".equals(root)) {
            root = "";
        }
        List<? extends Tpl> tplList = tplManager.getChild(root);
        pageResult.setRows(tplList);
        return pageResult;
    }




}