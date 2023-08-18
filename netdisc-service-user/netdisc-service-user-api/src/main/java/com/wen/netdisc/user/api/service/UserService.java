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
    /**
     * 根据请求头获取用户信息
     *
     * @return 用户信息
     */
    User getUserByHeader();

    /**
     * 根据请求头获取用户ID
     *
     * @return 用户ID
     */
    Integer getUidByHeader();

    /**
     * 查询全部用户
     *
     * @return 全部用户
     */
    List<User> queryUsers();

    /**
     * 根据条件查询用户
     *
     * @param term  查询条件
     * @param value 查询值
     * @return 符合条件的用户列表
     */
    List<User> queryUsersTerm(String term, String value);

    /**
     * 根据条件模糊查询用户
     *
     * @param term 查询条件
     * @param key  模糊查询关键字
     * @return 符合条件的用户列表
     */
    List<User> queryUsersLike(String term, String key);

    /**
     * 初始化超级管理员
     *
     * @param superAdminName     超级管理员姓名
     * @param superAdminLoginName 超级管理员登录名
     * @param superAdminPassword 超级管理员密码
     * @return 是否成功初始化超级管理员
     */
    boolean initAdmin(String superAdminName, String superAdminLoginName, String superAdminPassword);

    /**
     * 验证管理员
     *
     * @param id 管理员ID
     * @return 验证结果
     */
    int verifyAdmin(int id);

    /**
     * 删除用户
     *
     * @param userID 用户ID
     * @return 是否成功删除用户
     */
    boolean deleteUser(int userID);

    /**
     * 修改用户信息
     *
     * @param dto 用户DTO
     * @return 是否成功修改用户信息
     */
    boolean updateUser(UserDto dto);

    /**
     * 修改密码
     *
     * @param userDto 用户DTO
     */
    void upPassword(UserDto userDto);

    /**
     * 用户登录
     *
     * @param dto 用户DTO
     * @return 登录结果
     */
    String login(UserDto dto);

    /**
     * 使用电话号码注册/登录
     *
     * @param dto 登录DTO
     * @return 登录结果
     */
    String loginPhone(LoginPhoneDto dto);

    /**
     * 用户注册
     *
     * @param userDto 用户DTO
     * @return 注册结果
     */
    String register(UserDto userDto);

    /**
     * 根据ID获取用户信息
     *
     * @param userID 用户ID
     * @return 用户信息
     */
    User getUserById(int userID);

    /**
     * 发送验证码邮件
     *
     * @param loginName 登录名
     * @param email     邮箱
     * @return 是否成功发送验证码邮件
     */
    boolean sendCode(String loginName, String email);

    /**
     * 验证验证码
     *
     * @param loginName 登录名
     * @param code      验证码
     * @return 验证结果
     */
    boolean verifyCode(String loginName, String code);

    /**
     * 重置密码
     *
     * @param loginName 登录名
     * @param password  密码
     * @return 是否成功重置密码
     */
    boolean repwd(String loginName, String password);

    /**
     * 上传头像
     *
     * @param file   头像文件
     * @param userId 用户ID
     * @return 是否成功上传头像
     */
    boolean uploadHead(MultipartFile file, Integer userId);

    /**
     * 申请升级用户等级
     *
     * @param uid 用户ID
     * @return 是否成功申请升级用户等级
     */
    boolean applyUpLevel(Integer uid);

    /**
     * 获取用户头像
     *
     * @return 用户头像
     */
    ResponseEntity<InputStreamResource> getAvatar();
}
