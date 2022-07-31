package com.wen.netdisc.user.api.service.impl;

import com.wen.baseorm.core.mapper.BaseMapper;
import com.wen.baseorm.core.wrapper.QueryWrapper;
import com.wen.commutil.vo.ResultVO;
import com.wen.netdisc.common.exception.FailException;
import com.wen.netdisc.common.pojo.User;
import com.wen.netdisc.common.util.ResultVoUtil;
import com.wen.netdisc.filesystem.client.rpc.FilesystemClient;
import com.wen.netdisc.oauth.client.feign.OauthClient;
import com.wen.netdisc.user.api.mapper.UserMapper;
import com.wen.netdisc.user.api.service.MailService;
import com.wen.netdisc.user.api.service.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author calwen
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper userMapper;
    @Resource
    MailService mailService;
    @Resource
    RedisTemplate redisTemplate;

    @Resource
    BaseMapper baseMapper;

    @Resource
    OauthClient oauthClient;
    @Resource
    FilesystemClient filesystemClient;


    @Override
    public User getUserByHeader() {
        ResultVO<User> resultVO = oauthClient.getUser();
        return resultVO.getData();
    }

    @Override
    public Integer getUidByHeader() {
        ResultVO<Integer> resultVO = oauthClient.getUserId();
        return resultVO.getData();
    }

    /**
     * 查询全部用户
     *
     * @return 全部用户
     */
    @Override
    public List<User> queryUsers() {
        ArrayList<User> users = baseMapper.selectList(User.class);
        return users;
    }

    @Override
    public ArrayList<User> queryUsersTerm(String term, String value) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.add(term, value);
        ArrayList<User> users = baseMapper.selectList(User.class, wrapper);
        return users;
    }

    @Override
    public List<User> queryUsersLike(String term, String key) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.like(term, key);
        return baseMapper.selectList(User.class, wrapper);
    }

    /**
     * 增加全部用户
     *
     * @param superAdminName
     * @return 修改状态
     */
/*    @Override
    public int addUser(User user) {
        if (user == null) {
            return 0;
        }
        String superAdminLoginName = ConfigUtil.getSuperAdminLoginName();
        //禁止添加与超级管理员同登录名
        if (superAdminLoginName != null && superAdminLoginName.equals(user.getLoginName())) {
            return 0;
        }
        return baseMapper.insertTarget(user);
    }*/
    @Override
    public int initAdmin(String superAdminName, String superAdminLoginName, String superAdminPassword) {
        User superAdmin = new User(-10, superAdminName, superAdminLoginName, superAdminPassword, 0, null, null, null, new Date());
        return baseMapper.replaceTarget(superAdmin);
    }

    @Override
    public int verifyAdmin(int userId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.add("id", userId);
        User user = baseMapper.selectTarget(User.class);
        if (user != null) {
            return user.getUserType();
        }
        return 2;
    }

    /**
     * 删除用户，超级管理员禁止删除
     *
     * @param userId
     * @return
     */
    @Override
    public int deleteUser(int userId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.add("id", userId);
        User user = baseMapper.selectTarget(User.class, wrapper);
        if (user != null && user.getUserType() == 0) {
            return 0;
        }
        return baseMapper.deleteTarget(User.class, wrapper);
    }

    /**
     * 修改全部用户
     *
     * @param user
     * @return 修改状态
     */
    @Override
    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }

    /**
     * 登录
     *
     * @param loginName
     * @param pwd
     * @return
     */
    @Override
    public String login(String loginName, String pwd, boolean remember) {
        User user = userMapper.login(loginName, pwd);
        if (user == null) {
            throw new FailException("账号密码错误或未注册");
        }
        ResultVO<String> resultVO;
        //记住密码给30天，否则12小时
        if (remember) {
            resultVO = oauthClient.saveToken(user.getId(), user.getUserType(), 30 * 24);
        } else {
            ResultVO<String> vo = oauthClient.saveToken(user.getId(), user.getUserType(), 12);
            resultVO = vo;
        }
        ResultVoUtil.isSuccess(resultVO);
        return resultVO.getData();
    }

    @Override
    public String register(User user) {
        try {
            userMapper.addUser(user);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            throw new FailException("注册失败，账号已存在");
        }
        if (!filesystemClient.initStore(user.getId()).getData()) {
            //回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new FailException("初始化用户仓库失败");
        }

        ResultVO<String> resultVO = oauthClient.saveToken(user.getId(), user.getUserType(), 12);
        ResultVoUtil.isSuccess(resultVO);
        return resultVO.getData();
    }


    @Override
    public User getUserById(int userID) {
        return userMapper.getUserById(userID);
    }

    @Override
    public boolean sendCode(String loginName, String email) {
        User user = userMapper.getUserByLName(loginName);
        if (user == null) {
            throw new FailException("用户不存在");
        }
        if (!email.equals(user.getEmail())) {
            throw new FailException("输入邮箱不一致或未预留");
        }
        try {
            String code = this.createCode();
            String subject, content;
            subject = "重置密码";
            content = "账号: " + loginName + "，您好。\n" + "您当前正在重置密码，您的验证码为：" + code;
            mailService.sendSimpleMail(email, subject, content);
            String key = "code:lname:" + loginName;
            redisTemplate.opsForValue().set(key, code, 3, TimeUnit.MINUTES);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean verifyCode(String loginName, String code) {
        String key = "code:lname:" + loginName;
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return false;
        }
        String realCode = String.valueOf(value);
        return realCode.equals(code);
    }

    @Override
    public boolean repwd(String loginName, String password) {
        User user = userMapper.getUserByLName(loginName);
        if (user == null) {
            return false;
        }
        user.setPassWord(password);

        return userMapper.updatepwd(user) > 0;
    }

    @Override
    public boolean uploadHead(MultipartFile file, Integer userId) {
        if (file.isEmpty()) {
            throw new FailException("空文件");
        }
        String path = filesystemClient.uploadHead(file).getData();
        User user = userMapper.getUserById(userId);
        user.setAvatar(path);
        return userMapper.updateUser(user) > 0;
    }

    /**
     * @param uid
     * @return
     */
  /*  @Override
    public boolean upLevel(Integer uid) {
        User user = userMapper.getUserById(uid);
        Integer type = user.getUserType();
        type = type > 10 ? type % 10 + 1 : type;
        user.setUserType(type);

        FileStore store = storeService.queryStoreByUserId(uid);
        //升级规则：max容量*2
        store.setMaxSize(store.getMaxSize() * 2);
        if (userMapper.updateUser(user) > 0 && storeService.updateStore(store)) {
            return true;
        }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return false;
    }*/

    /**
     * @param uid
     * @return
     */
    @Override
    public boolean applyUpLevel(Integer uid) {
        User user = userMapper.getUserById(uid);
        Integer type = user.getUserType();
        if (type > 10) {
            throw new FailException("请勿多次申请");
        }
        user.setUserType(type + 10);
        return userMapper.updateUser(user) > 0;
    }


    private String createCode() {
        StringBuilder code = new StringBuilder();
        //文件生成码
        for (int i = 0; i < 5; i++) {
            code.append(new Random().nextInt(9));
        }
        return String.valueOf(code);
    }
}
