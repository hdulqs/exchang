/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2016年12月11日 上午8:23:34
 */
package com.batsoft.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * <p> TODO</p>
 * @author:         Bat Admin
 * @Date :          2016年12月11日 上午8:23:34 
 */
@Component
public class DefaultJob {

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
      //  System.out.println("现在时间：" + dateFormat.format(new Date()));
    }
    
    @Scheduled(fixedDelay  = 5000)
    public void job0() {
       // System.out.println("上一次執行完毕后5秒：" + dateFormat.format(new Date()));
    }
    
    /**
     * 第一次延迟1秒后执行，之后按fixedRate的规则每5秒执行一次
     * <p> TODO</p>
     * @author:         Bat Admin
     * @param:    
     * @return: void 
     * @Date :          2016年12月11日 上午8:29:53   
     * @throws:
     */
    @Scheduled(initialDelay=1000, fixedRate=5000)
    public void job1() {
       // System.out.println("第一次延迟1秒后执行，之后按fixedRate的规则每5秒执行一次：" + dateFormat.format(new Date()));
    }
    
   
    @Scheduled(cron="*/5 * * * * *")
    public void job2() {
       // System.out.println("通过cron表达式定义规则：" + dateFormat.format(new Date()));
    }
}
