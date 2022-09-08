package com.wen.netdisc.user.api.service.impl;

import com.wen.netdisc.common.enums.RedisEnum;
import com.wen.netdisc.common.exception.FailException;
import com.wen.netdisc.common.pojo.User;
import com.wen.netdisc.common.util.CommBeanUtils;
import com.wen.netdisc.common.util.NumberUtil;
import com.wen.netdisc.common.vo.ResultVO;
import com.wen.netdisc.filesystem.client.rpc.FilesystemClient;
import com.wen.netdisc.oauth.client.feign.OauthClient;
import com.wen.netdisc.user.api.dto.LoginPhoneDto;
import com.wen.netdisc.user.api.dto.UserDto;
import com.wen.netdisc.user.api.service.SmsMailService;
import com.wen.netdisc.user.api.service.UserService;
import com.wen.netdisc.user.api.util.UserUtil;
import com.wen.releasedao.core.mapper.BaseMapper;
import com.wen.releasedao.core.wrapper.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author calwen
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class UserServiceImpl implements UserService {
    @Resource
    SmsMailService smsMailService;
    @Resource
    RedisTemplate<String, String> redisTemplate;

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
        List<User> users = baseMapper.getList(User.class);
        return users;
    }

    @Override
    public List<User> queryUsersTerm(String term, String value) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq(term, value);
        List<User> users = baseMapper.getList(User.class, wrapper);
        return users;
    }

    @Override
    public List<User> queryUsersLike(String term, String key) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.like(term, key);
        return baseMapper.getList(User.class, wrapper);
    }

    /**
     * 增加全部用户
     *
     * @param name
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
    public boolean initAdmin(String name, String loginName, String password) {
        User superAdmin = new User();
        superAdmin.setId(-10);
        superAdmin.setUserName(name);
        superAdmin.setLoginName(loginName);
        superAdmin.setPassWord(password);
        superAdmin.setUserType(0);
        return baseMapper.save(superAdmin);
    }

    @Override
    public int verifyAdmin(int userId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("id", userId);
        User user = baseMapper.get(User.class);
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
    public boolean deleteUser(int userId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("id", userId);
        User user = baseMapper.get(User.class, wrapper);
        if (user != null && user.getUserType() == 0) {
            return false;
        }
        return baseMapper.delete(User.class, wrapper);
    }

    /**
     * 修改全部用户
     */
    @Override
    public boolean updateUser(UserDto dto) {
        User user = baseMapper.getById(User.class, dto.getId());
        CommBeanUtils.copyPropertiesIgnoreNull(dto, user);
        return baseMapper.save(user);
    }

    @Override
    public void upPassword(UserDto dto) {
        Integer uid = UserUtil.getUid();
        User user = baseMapper.getById(User.class, uid);
        if (!Objects.equals(dto.getPassWord(), user.getPassWord())) {
            throw new FailException("密码不正确");
        }
        user.setPassWord(dto.getNewPassWord());
        baseMapper.save(user);
    }


    /**
     * 登录
     */
    @Override
    public String login(UserDto dto) {
        QueryWrapper wrapper = new QueryWrapper()
                .eq("login_name", dto.getLoginName())
                .eq("pass_word", dto.getPassWord());
        User user = baseMapper.get(User.class, wrapper);
        if (user == null) {
            throw new FailException("账号密码错误或未注册");
        }
        ResultVO<String> resultVO;
        //记住密码给30天，否则12小时
        if (Boolean.TRUE.equals(dto.getRemember())) {
            resultVO = oauthClient.saveToken(user.getId(), user.getUserType(), 30 * 24);
        } else {
            resultVO = oauthClient.saveToken(user.getId(), user.getUserType(), 12);
        }
        return resultVO.getData();
    }

    @Override
    public String loginPhone(LoginPhoneDto dto) {
        String phone = dto.getPhone();
        String code = dto.getCode();
        if (!smsMailService.verifySmsCode(phone, code)) {
            throw new FailException("验证码不正确");
        }
        QueryWrapper wrapper = new QueryWrapper()
                .eq("login_name", phone);
        User user = baseMapper.get(User.class, wrapper);
        //注册或登录
        if (user == null) {
            UserDto userDto = new UserDto();
            userDto.setUserName(phone);
            userDto.setLoginName(phone);
            userDto.setPassWord(phone);
            userDto.setPhoneNumber(phone);
            return register(userDto);
        } else {
            return oauthClient.saveToken(user.getId(), user.getUserType(), 12)
                    .getData();
        }
    }

    @Override
    public String register(UserDto dto) {
        QueryWrapper wrapper = new QueryWrapper().eq("login_name", dto.getLoginName());
        if (baseMapper.get(User.class, wrapper) != null) {
            throw new FailException("注册失败，账号已存在");
        }
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        user.setUserType(2);
        user.setAvatar("/#");
        user.setRegisterTime(new Date());
        try {
            if (!baseMapper.add(user)) {
                throw new FailException("注册失败");
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            throw new FailException("注册失败");
        }
        if (!filesystemClient.initStore(user.getId()).getData()) {
            //回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new FailException("初始化用户仓库失败");
        }
        ResultVO<String> resultVO = oauthClient.saveToken(user.getId(), user.getUserType(), 12);
        return resultVO.getData();
    }


    @Override
    public User getUserById(int uid) {
        return baseMapper.getById(User.class, uid);
    }

    @Override
    public boolean sendCode(String loginName, String email) {
        QueryWrapper wrapper = new QueryWrapper().eq("login_name", loginName);
        User user = baseMapper.get(User.class, wrapper);
        if (user == null) {
            throw new FailException("用户不存在");
        }
        if (!email.equals(user.getEmail())) {
            throw new FailException("输入邮箱不一致或未预留");
        }
        try {
            String code = NumberUtil.createCode();
            String subject, content;
            subject = "重置密码";
            content = "账号: " + loginName + "，您好。\n" + "您当前正在重置密码，您的验证码为：" + code;
            smsMailService.sendMail(email, subject, content);
            String key = RedisEnum.SMS_Mail_CODE_PREFIX.getProperty() + loginName;
            redisTemplate.opsForValue().set(key, code, 3, TimeUnit.MINUTES);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FailException("发送验证码失败");
        }

    }

    @Override
    public boolean verifyCode(String loginName, String inCode) {
        String key = RedisEnum.SMS_Mail_CODE_PREFIX.getProperty() + loginName;
        String code = redisTemplate.opsForValue().get(key);
        return Objects.equals(inCode, code);
    }

    @Override
    public boolean repwd(String loginName, String password) {
        QueryWrapper wrapper = new QueryWrapper().eq("login_name", loginName);
        User user = baseMapper.get(User.class, wrapper);
        if (user == null) {
            throw new FailException("用户不存在");
        }
        user.setPassWord(password);
        return baseMapper.save(user);
    }

    @Override
    public boolean uploadHead(MultipartFile file, Integer uid) {
        if (file.isEmpty()) {
            throw new FailException("空文件");
        }
        String path = filesystemClient.uploadHead(file).getData();
        User user = baseMapper.getById(User.class, uid);
        user.setAvatar(path);
        return baseMapper.save(user);
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
    @Override
    public boolean applyUpLevel(Integer uid) {
        User user = baseMapper.getById(User.class, uid);
        Integer type = user.getUserType();
        if (type > 10) {
            throw new FailException("请勿多次申请");
        }
        user.setUserType(type + 10);
        return baseMapper.save(user);
    }
}
