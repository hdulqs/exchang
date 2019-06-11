package com.batsoft.app.system.start;

import com.batsoft.core.common.SpringContextUtil;
import com.batsoft.model.module.system.menu.AppMenu;
import com.batsoft.service.module.exchange.service.CustomerAccountService;
import com.batsoft.service.module.system.service.config.AppConfigService;
import com.batsoft.service.module.system.service.menu.AppMenuService;
import com.batsoft.utils.YamlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统启动初始话数据
 * Created by Administrator on 2017/6/28.
 */
@Slf4j
@Component
@Order(value=2)
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private AppConfigService appConfigService;
    @Autowired
    private AppMenuService appMenuService;
    @Autowired
    private CustomerAccountService customerAccountService;

    @Override
    public void run(String... args) throws Exception {
        log.info(">>>>>>>>>>>>>>>服务启动执行，执行加载系统配置操作开始 <<<<<<<<<<<<<");
        initConfig();
        log.info(">>>>>>>>>>>>>>>服务启动执行，执行加载系统配置操作完成 <<<<<<<<<<<<<");
        log.info(">>>>>>>>>>>>>>>服务启动执行，执行AppMenu开始 <<<<<<<<<<<<<");
        initMenu();
        log.info(">>>>>>>>>>>>>>>服务启动执行，执行AppMenu完成 <<<<<<<<<<<<<");

        log.info(">>>>>>>>>>>>>>>服务启动执行，saveUserAddressToRedis开始 <<<<<<<<<<<<<");
        saveUserAddressToRedis("ETH");
        log.info(">>>>>>>>>>>>>>>服务启动执行，saveUserAddressToRedis 完成<<<<<<<<<<<<<");
    }

    /**
     * 加载系统参数配置文件 并且放入缓存
     */
    private void initConfig(){
        try {
            appConfigService.findConfigToCache();
        }catch (Exception e){
            e.printStackTrace();
            log.info(">>>>>>>>>>>>>>>initConfig服务启动失败<<<<<<<<<<<<<"+ e.getMessage());
        }

    }

    /**
     * 初始化 menu
     */
    private void initMenu() {
        try {
            Resource[] resources= SpringContextUtil.findResourceArr("/config/menu_*.yml");
            Map<String, Object> menusMap = new HashMap();

            for (Resource resource : resources) {
                menusMap = YamlUtils.yaml2Map(resource);
                Map<String, Object> menus = (Map<String, Object>) menusMap.get("menus");

                if (null == appMenuService.findByKey(menus.get("key").toString())) {
                    Integer sort=appMenuService.findMaxSort("0");
                    if(null==sort){
                        sort=0;
                    }
                    AppMenu appMenu = new AppMenu();
                    appMenu.setName(menus.get("name").toString());
                    appMenu.setParentId("0");
                    appMenu.setDescription(menus.get("description").toString());
                    appMenu.setSort(sort+5);
                    appMenu.setIcon(menus.get("icon").toString());
                    appMenu.setPath(menus.get("path").toString());
                    appMenu.setLevel(1);
                    appMenu.setLevelPath("0,");
                    appMenu.setDisplay(AppMenu.DISPLAY1);
                    appMenu.setMenuKey(menus.get("key").toString());
                    appMenuService.save(appMenu);
                }

                saveMenu(menus, menus.get("key").toString());

            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("app menu Init error"+e.getMessage());
        }

    }

    /**
     * 递归保存数据
     * @param datas
     * @param pk 父key
     */
    public void saveMenu(Map<String ,Object> datas,String pk) {
        List items = (List)datas.get("item");
        if(null!=items&&items.size()>0) {
            for (int i = 0; i < items.size(); i++) {
                Map<String, Object> item = (Map<String, Object>) items.get(i);
                AppMenu appMenu=appMenuService.findByKey(item.get("key").toString());
                if(null==appMenu){
                    Integer sort=appMenuService.findMaxSort(appMenuService.findByKey(pk).getId());
                    if(null==sort){
                        sort=0;
                    }
                    AppMenu menu=new AppMenu();
                    menu.setName(item.get("name").toString());
                    menu.setParentId(appMenuService.findByKey(pk).getId());
                    menu.setDescription(item.get("description").toString());
                    menu.setSort(sort+5);
                    menu.setPermission(item.get("permission").toString());
                    menu.setIcon(item.get("icon").toString());
                    menu.setPath(item.get("path").toString());
                    menu.setMenu(Integer.valueOf(item.get("menu").toString()));
                    menu.setMenuKey(item.get("key").toString());
                    menu.setDisplay(1);
                    appMenuService.saveAppMenu(menu);
                }

                saveMenu(item,item.get("key").toString());
            }
        }
    }

    /**
     * 初始化用户币地址到缓存
     */
    private void saveUserAddressToRedis(String coinCode){
        customerAccountService.addAdressToRedis(coinCode);
    }

}
