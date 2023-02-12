package com.ydjk.msdemo.controller;

import com.ydjk.msdemo.service.TaskService;
import com.ydjk.msdemo.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "任务")
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @ApiOperation("获取全部任务")
    @GetMapping
    public Result getTasks(){
        return Result.success(taskService.list());
    }

    @ApiOperation("获取任务数量")
    @GetMapping("count")
    public Result getTasksCount(){
        return Result.success((long) taskService.list().size());
    }
}
