package com.ydjk.msdemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.ydjk.msdemo.dao.UserDao;
import com.ydjk.msdemo.entity.User;
import com.ydjk.msdemo.entity.UserTask;
import com.ydjk.msdemo.entity.Work;
import com.ydjk.msdemo.service.UserService;
import com.ydjk.msdemo.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "用户账号操作")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result login(@RequestParam("username") String username,
                        @RequestParam("password") String password) {
        Map<String, Object> resp =  userService.login(username, password);
        try {
            Assert.notNull(resp,"用户名或密码错误");
        }catch (IllegalArgumentException e){
//            e.printStackTrace();
            return Result.fail("用户名或密码错误");
        }
        return Result.success(200,"登陆成功",resp);
    }

    @ApiOperation("用户登出")
    @PostMapping("/logout")
    public Result logout(@RequestHeader("Authorization") String Authorization){
        boolean ret = userService.logout(Authorization);
        return ret ? Result.success( "已退出登录") : Result.fail( "退出登录失败，没有用户信息");
    }

    @RequestMapping(path = "/unauthorized/{message}")
    public Result unauthorized(@PathVariable String message) throws UnsupportedEncodingException {
        log.error(message);
        return Result.fail(message);
    }

    @ApiOperation(value = "添加用户")
    @PostMapping("/add")
    public Result register(@RequestBody @Validated User user){
        User u = userService.getUser(user.getUsername());
        if(u != null){
            return Result.fail("用户已存在，创建失败");
        }
        User queryUser = userService.registerUser(user);
        return Result.success(queryUser.getId());
    }

    @RequiresRoles("teacher")
    @ApiOperation("作业用户列表")
    @GetMapping("list")
    public Result userList(
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum){
        IPage<UserDao> page = new Page<>(pageNum, pageSize);
        IPage<UserDao> pageUser  = userService.selectJoinListPage(page, UserDao.class, new MPJLambdaWrapper<User>()
                        .selectAs(User::getId, UserDao::getId)
                        .selectAs(User::getName, UserDao::getName)
                        .selectCount(Work::getImageUrl, UserDao::getImageCount)
                        .selectCount(Work::getVideoUrl, UserDao::getVideoCount)
                .innerJoin(UserTask.class, UserTask::getUserId, User::getId)
                .leftJoin(Work.class, Work::getTuid, UserTask::getId)
                .eq(User::getIsTeacher, false)
                .groupBy(User::getId)
                .orderByAsc(User::getId)

        );
//        List<UserDao> records = pageUser.getRecords().stream().map(
//                user -> new UserDao(user)
//        ).collect(Collectors.toList());
//        pageUser.setRecords(records);
        return Result.success(pageUser);
    }

//    @ApiOperation("作业用户列表")
//    @GetMapping("user/list")
//    public Result userWorkList(){
//        userService.
//        return Result.success(1111);
//    }
}
