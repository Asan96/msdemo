package com.ydjk.msdemo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ydjk.msdemo.entity.User;
import com.ydjk.msdemo.entity.UserTask;
import com.ydjk.msdemo.entity.Work;
import com.ydjk.msdemo.service.UserTaskService;
import com.ydjk.msdemo.utils.JWTUtil;
import com.ydjk.msdemo.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户任务")
@RestController
@RequestMapping("/utask")
public class UserTaskController {

    @Autowired
    private UserTaskService userTaskService;

    @ApiOperation("添加用户任务")
    @PostMapping
    public Result addUserTask(UserTask userTask){
        boolean ret  = userTaskService.save(userTask);
        return ret ? Result.success("新增成功"): Result.fail("新增失败");
    }
}
