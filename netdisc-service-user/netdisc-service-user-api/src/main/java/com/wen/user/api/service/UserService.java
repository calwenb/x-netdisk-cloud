package com.wen.user.api.service;

import com.wen.common.pojo.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author calwen
 */
public interface UserService {
    User getUserByHeader();
    Integer getUidByHeader();
    /**
     * 查询全部用户
     *
     * @return 全部用户
     */
    List<User> queryUsers();

    List<User> queryUsersTerm(String term, String value);

    List<User> queryUsersLike(String term, String key);

    /**
     * 增加全部用户
     *
     * @param user
     * @return 修改状态
     */
    int addUser(User user);

    int initAdmin(String superAdminName, String superAdminLoginName, String superAdminPassword);

    int verifyAdmin(int id);


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
    String login(String loginName, String pwd, boolean remember);

    /**
     * 注册业务
     * 注册、并初始化用户仓库
     *
     * @param user
     */
    String register(User user);

    /**
     * 通过Id获得user信息
     *
     * @param userID
     * @return
     */
    User getUserById(int userID);

    /**
     * 申请发验证邮件
     *
     * @param loginName
     * @param email
     * @return
     */
    boolean sendCode(String loginName, String email);

    boolean verifyCode(String loginName, String code);

    boolean repwd(String loginName, String password);

    boolean uploadHead(MultipartFile file, String userId);


    boolean upLevel(Integer uid);

    boolean applyUpLevel(Integer uid);
}
