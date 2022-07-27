package com.wen.baseorm.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 内置JDBC工具类
 * 用于测试，不建议直接使用
 *
 * @author calwen
 * @since 2022/7/9
 */

@Deprecated
public class JDBCUtil {
    static String url = "jdbc:mysql://localhost:3306/test?useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai";
    static String username = "";
    static String password = "";

    public static Connection getConn() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, username, password);
    }

}
