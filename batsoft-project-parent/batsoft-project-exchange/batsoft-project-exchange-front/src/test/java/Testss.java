import com.batsoft.FrontApplication;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FrontApplication.class)
public class Testss {
    SimpleAccountRealm simpleAccountRealm =new SimpleAccountRealm();
    @Before
    public void addUser(){
        simpleAccountRealm.addAccount("13265797978","Ya123456");
    }

    @Test
    public  void test(){
    	//构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager =new DefaultSecurityManager();//创建一个默认的安全管理器
        defaultSecurityManager.setRealm(simpleAccountRealm); //将账户域添加到安全管理器中
        
        //2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);//安全管理器提交到安全工具类中
        Subject subject=SecurityUtils.getSubject();

        UsernamePasswordToken token=new UsernamePasswordToken("13265797978","Ya123456");
        subject.login(token); //通过login方法和token进行认证

        subject.isAuthenticated(); //是否认证成功的方法
        System.out.println( subject.isAuthenticated());  //认证成功 输出 true
    }
    
    
    
}
