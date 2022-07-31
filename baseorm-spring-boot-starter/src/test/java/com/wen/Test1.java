/*
package com.wen;

import com.wen.dao.BaseMapper;
import com.wen.dao.UserDao;
import com.wen.xwebalbum.pojo.User;
import com.wen.util.JDBCUtil;
import com.wen.wrapper.SetWrapper;
import com.wen.wrapper.QueryWrapper;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class Test1 {
    UserDao userDao = new UserDao();
    Connection conn = JDBCUtil.getConn();

    public Test1() throws SQLException, ClassNotFoundException {
    }

    @Test
    public void t1() throws Exception {
        for (int i = 0; i < 10; i++) {
            User user = new User(-1, "文" + i, "123");
            System.out.println(UserDao.insertTarget(conn, user));
        }

    }

    @Test
    public void t2() throws Exception {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.add("username", "文1");
        System.out.println(BaseMapper.deleteTarget(conn, User.class, wrapper));
    }

    @Test
    public void t3() throws Exception {
        ArrayList<User> users = UserDao.selectList(conn, User.class);
        System.out.println(users);
        System.out.println("\n==================\n");

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.add("username", "wen");


        ArrayList<User> users1 = UserDao.selectList(conn, User.class, wrapper);
        System.out.println(users1);
        System.out.println("\n==================\n");
    }

    @Test
    public void t4() throws Exception {
        User user = UserDao.selectTarget(conn, User.class);
        System.out.println(user);
        System.out.println("==================\n");

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.add("username", "文0");
        queryWrapper.or("username", "long");
        user = UserDao.selectTarget(conn, User.class, queryWrapper);
        System.out.println(user);
    }

    @Test
    public void t5() throws Exception {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.add("username", "wen");
        SetWrapper setWrapper = new SetWrapper();
        setWrapper.add("password", "6661");
        System.out.println(UserDao.updateTarget(conn, User.class, setWrapper, queryWrapper));
    }

    @Test
    public void t6() throws Exception {
        User user = new User(115, "文" + 0, "111");
        System.out.println(UserDao.replaceTarget(conn, user));
    }


}
*/
