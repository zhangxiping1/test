package com.xtayfjpk.security.jaas.demo;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.util.Scanner;

public class DemoCallbackHander implements CallbackHandler {
    //创建输入对象
    Scanner sc = new Scanner(System.in);

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        NameCallback nameCallback = (NameCallback) callbacks[0];
        PasswordCallback passwordCallback = (PasswordCallback) callbacks[1];
        //设置用户名与密码
        nameCallback.setName(getUserFromSomeWhere());
        passwordCallback.setPassword(getPasswordFromSomeWhere().toCharArray());
    }


    //为简单起见用户名与密码写死直接返回，真实情况可以由用户输入等具体获取
    public String getUserFromSomeWhere() {
        String User = null;
        System.out.print("请输入用户名:");
        User = sc.nextLine();
        return User;
    }

    public String getPasswordFromSomeWhere() {
        String Password = null;
        System.out.print("请输入密码:");
        Password = sc.nextLine();
        return Password;
    }
}
