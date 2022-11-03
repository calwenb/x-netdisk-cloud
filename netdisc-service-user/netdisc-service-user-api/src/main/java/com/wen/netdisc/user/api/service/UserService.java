package com.wen.netdisc.user.api.service;

import com.wen.netdisc.common.pojo.User;
import com.wen.netdisc.user.api.dto.LoginPhoneDto;
import com.wen.netdisc.user.api.dto.UserDto;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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


    boolean initAdmin(String superAdminName, String superAdminLoginName, String superAdminPassword);

    int verifyAdmin(int id);


    /**
     * 修改全部用户
     *
     * @param userID
     * @return
     */
    boolean deleteUser(int userID);

    /**
     * 修改全部用户
     */
    boolean updateUser(UserDto dto);

    void upPassword(UserDto userDto);

    /**
     * 登录
     *
     * @return
     */
    String login(UserDto dto);

    /**
     * 使用电话号码 注册/登录
     */
    String loginPhone(LoginPhoneDto dto);

    /**
     * 注册业务
     * 注册、并初始化用户仓库
     *
     */
    String register(UserDto userDto);

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

    boolean uploadHead(MultipartFile file, Integer userId);


    /*    boolean upLevel(Integer uid);*/

    boolean applyUpLevel(Integer uid);

    ResponseEntity<InputStreamResource>  getAvatar();
}
