package com.imooc.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class JdbcRealmTest {

    DruidDataSource dataSource=new DruidDataSource();
    {
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8");
        dataSource.setUsername("root");
        dataSource.setPassword("1004758012");
    }

    @Test
    public void testAuthentication()
    {
        JdbcRealm jdbcRealm=new JdbcRealm();

        jdbcRealm.setDataSource(dataSource);
        jdbcRealm.setPermissionsLookupEnabled(true);//要查询权限要先加这个

        String sql="select password from test_user where user_name=?";
        jdbcRealm.setAuthenticationQuery(sql);

        String roleSql="select role_name from test_user_role where user_name=?";
        jdbcRealm.setUserRolesQuery(roleSql);

        //1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager=new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

        //2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject= SecurityUtils.getSubject();

        UsernamePasswordToken token=new UsernamePasswordToken("xiaoming","654321");
        subject.login(token);

        System.out.println("isAuthenticated:"+subject.isAuthenticated());

//        subject.checkRole("admin");
//
//        subject.checkRoles("admin","user");
//
//        subject.checkPermission("user:select");

        subject.checkRole("user");

    }
}
