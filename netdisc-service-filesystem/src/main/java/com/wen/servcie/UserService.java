package com.wen.servcie;

import com.wen.pojo.User;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Mr.文
 */
public interface UserService {
    /**
     * 查询全部用户
     *
     * @return 全部用户
     */
    List<User> queryUsers();

    /**
     * 增加全部用户
     *
     * @param user
     * @return 修改状态
     */
    int addUser(User user);


    /**
     * 修改全部用户
     *
     * @param userID
     * @return
     */
    int deleteUser(int userID);

    /**
     * 修改全部用户
     *
     * @param user
     * @return 修改状态
     */
    int updateUser(User user);


    /**
     * 登录
     *
     * @param loginName
     * @param pwd
     * @return
     */
    User login(String loginName, String pwd);

    /**
     * 注册业务
     * 注册、并初始化用户仓库
     * @param userName
     * @param loginName
     * @param pwd
     * @return
     */
    Map<String,Object> register(String userName, String loginName, String pwd);

    /**
     * 通过Id获得user信息
     * @param userID
     * @return
     */
    User getUserById(int userID);
}
