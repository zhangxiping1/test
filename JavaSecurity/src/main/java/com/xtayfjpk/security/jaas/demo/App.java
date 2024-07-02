package com.xtayfjpk.security.jaas.demo;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.security.PrivilegedAction;

public class App {

    public static void main(String[] args) {
        testLogin();
        //SecoutJavaHome();
    }

    //没有添加安全管理器
    public static void outJavaHome(){
        System.out.println(System.getProperty("java.library.path"));

    }

    //添加安全管理器
    public static void SecoutJavaHome(){

        System.setProperty("java.security.policy", "D:\\project\\myproject\\JavaLearn\\JavaSecurity\\src\\main\\resources\\demo.policy");
        //安装安全管理器
        System.setSecurityManager(new SecurityManager());

        System.out.println(System.getProperty("java.library.path"));
    }

    //
    public static  void  testLogin(){

        System.setProperty("java.security.auth.login.config", "D:\\project\\myproject\\JavaLearn\\JavaSecurity\\src\\main\\java\\com\\xtayfjpk\\security\\jaas\\demo\\demo.conf");
        System.setProperty("java.security.policy", "D:\\project\\myproject\\JavaLearn\\JavaSecurity\\src\\main\\java\\com\\xtayfjpk\\security\\jaas\\demo\\demo.policy");

        System.setSecurityManager(new SecurityManager());

        System.getSecurityManager().getSecurityContext();
        try {
            //创建登录上下文
            LoginContext context = new LoginContext("demo", new DemoCallbackHander());
            //进行登录，登录不成功则系统退出
            context.login();


            Subject subject = context.getSubject();

            //访问资源  如果 demo.policy 文件 设置了 给主体授权  就会报错,需要使用Subject.doAsPrivileged 方式访问
            //System.out.println(System.getProperty("java.home"));

            //该方法调用需要"doAsPrivileged"权限
            Subject.doAsPrivileged(subject, new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    System.out.println(System.getProperty("java.home"));
                    return null;
                }
            },null);

        } catch (LoginException le) {
            System.err.println("Cannot create LoginContext LoginException. " + le.getMessage());
            System.exit(-1);
        } catch (SecurityException se) {
            System.err.println("Cannot create LoginContext SecurityException. " + se.getMessage());
            System.exit(-1);
        }
    }
}
