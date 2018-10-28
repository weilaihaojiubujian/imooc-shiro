package com.imooc.dao.impl;

import com.imooc.dao.UserDao;
import com.imooc.vo.User;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class UserDaoImpl implements UserDao {


    private JdbcTemplate jdbcTemplate;

    @Override
    public User getUserByUserName(String username) {

        String sql="select username,password from users where username=?";

        List<User> list=jdbcTemplate.query(sql, new String[]{username}, new RowMapper<User>() {
            @Nullable
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user=new User();
                user.setUsername(resultSet.getString("username"));

                user.setPassword(resultSet.getString("password"));
                return user;
            }
        });


        if (CollectionUtils.isEmpty(list)){
            return null;
        }

        return list.get(0);
    }

    @Override
    public List<String> queryRolesByUserName(String username) {

        String sql="select role_name from user_roles where username=?";




        return jdbcTemplate.query(sql, new String[]{username}, new RowMapper<String>() {
            @Nullable
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("role_name");
            }
        });
    }
}
