package com.ydjk.msdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.ydjk.msdemo.entity.User;
import com.ydjk.msdemo.entity.UserTask;
import com.ydjk.msdemo.mapper.UserMapper;
import com.ydjk.msdemo.service.TaskService;
import com.ydjk.msdemo.service.UserService;
import com.ydjk.msdemo.service.UserTaskService;
import com.ydjk.msdemo.utils.JWTUtil;
import com.ydjk.msdemo.utils.RedisUtil;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class UserServiceImpl extends MPJBaseServiceImpl<UserMapper, User> implements UserService {
    @Value("${param.encryptNum}")
    private int encryptNum;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserTaskService userTaskService;

    @Override
    public String getCacheKey(Long uid){
        return String.format("%s:uid%s", RedisUtil.UserDbName,uid);
    }

    /**
     * 获取加密密码
     * @param salt 盐
     * @param password 密码
     * @return
     */
    private String getSecretPwd(ByteSource salt, String password){
        return new SimpleHash("MD5", password, salt, encryptNum).toString();
    }

    /**
     * 用户名密码登录
     * @param username 用户名
     * @param password 密码
     * @return token or null
     */
    @Override
    public Map<String, Object> login(String username, String password){
        ByteSource salt = ByteSource.Util.bytes(username);
        String secretPwd = getSecretPwd(salt, password);
        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username).eq(User::getPassword, secretPwd));
        try {
            Assert.notNull(user,"用户名或密码错误");
        }catch (IllegalArgumentException e){
            return null;
        }

        String token;
        try {
            token = JWTUtil.createToken(user);
        }catch (UnsupportedEncodingException e){
            return null;
        }
        String key = String.format("%s:uid%s", RedisUtil.UserDbName,user.getId());
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("uid", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("token", token);
        userInfo.put("is_teacher", user.getIsTeacher());
        redisUtil.set(key, userInfo, RedisUtil.EXPIRE);
        return userInfo;
    }

    /**
     * 创建用户
     * @param user 用户对象
     * @return User
     */
    @Override
    @Transactional
    public User addUser(User user){
        ByteSource salt = ByteSource.Util.bytes(user.getUsername());
        User queryUser = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, user.getUsername()));
        if (queryUser != null) return null;
        user.setSalt(salt.toString());
        String newPs = new SimpleHash("MD5", user.getPassword(), salt, encryptNum).toString();
        user.setPassword(newPs);
        save(user);
        Collection<UserTask> userTaskList = new ArrayList<>();
        taskService.list().forEach(task -> {
            userTaskList.add(new UserTask(task.getId(), user.getId()));
        });
        userTaskService.saveBatch(userTaskList);
        return user;
    }

    /**
     * 退出登录
     * @param token token
     * @return boolean
     */
    @Override
    public boolean logout(String token){
        Long userId = JWTUtil.getUserId(token);
        try {
            Assert.notNull(userId, "token解析失败，用户id为null");
        }catch (IllegalArgumentException ignore){}
        User queryUser = getById(userId);
        if (queryUser == null) return false;
        redisUtil.del(getCacheKey(userId));
        return true;
    }

    @Override
    public User registerUser(User user) {
        return this.addUser(user);
    }

    /**
     * 获取用户
     * @param username 用户名
     * @return User
     */
    @Override
    public User getUser(String username){
        return getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }
}
