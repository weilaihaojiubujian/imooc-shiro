package com.imooc.shiro.realm;

import com.imooc.dao.UserDao;
import com.imooc.vo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.*;

public class CustomRealm extends AuthorizingRealm {



    @Resource
    private UserDao userDao;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {


        String username= (String) principalCollection.getPrimaryPrincipal();
        Set<String> roles=getRolesByUserName(username);

        Set<String> permissions=getPermissionsByUserName(username);

        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(permissions);
        simpleAuthorizationInfo.setRoles(roles);



        return simpleAuthorizationInfo;
    }

    private Set<String> getPermissionsByUserName(String username) {
        Set<String> sets=new HashSet<>();
        sets.add("user:delete");
        sets.add("user:add");
        return sets;
    }

    /*
    模拟从数据库或缓存中获取角色数据
     */
    private Set<String> getRolesByUserName(String username) {


        List<String> list=userDao.queryRolesByUserName(username);

        Set<String> sets=new HashSet<>(list);

        return sets;
    }


    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        //1.从主体传过来的认证信息中，获得用户名
        String username= (String) authenticationToken.getPrincipal();

        //2.通过用户名到数据库中获取凭证
        String password=getPasswordByUserName(username);
        if(password==null)
        {
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo=new SimpleAuthenticationInfo(username,password,"customRealm");

        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(username));

        return authenticationInfo;
    }

    private String getPasswordByUserName(String username) {
        //一般应该是要到数据库中得到值，现在就简单的用固定的值

        User user=userDao.getUserByUserName(username);

        if (user!=null){
            return user.getPassword();
        }

        return null;

    }
//
//    public static  void main(String[] args)
//    {
//        Md5Hash md5Hash=new Md5Hash("123456","Mark");
//        System.out.println(md5Hash.toString());
//
//    }

}
