package com.ydjk.msdemo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.ydjk.msdemo.dao.UserWorkDao;
import com.ydjk.msdemo.dao.WorkDao;
import com.ydjk.msdemo.entity.Task;
import com.ydjk.msdemo.entity.User;
import com.ydjk.msdemo.entity.UserTask;
import com.ydjk.msdemo.entity.Work;
import com.ydjk.msdemo.service.TaskService;
import com.ydjk.msdemo.service.UserTaskService;
import com.ydjk.msdemo.service.WorkService;
import com.ydjk.msdemo.utils.JWTUtil;
import com.ydjk.msdemo.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Api(tags = "作业")
@RestController
@RequestMapping("/work")
public class WorkController {

    @Autowired
    private WorkService workService;

    @Autowired
    private UserTaskService userTaskService;

    @Value("${param.host}")
    private String HOST;
    @RequiresRoles("student")
    @PostMapping("image")
    public Result submitWorkImage(
            @RequestHeader("Authorization") String authorization,
            @RequestParam MultipartFile file,
                             @RequestParam Long tuid){
        Long uid = JWTUtil.getUserId(authorization);
        boolean ret = workService.submitFile(file, tuid, uid, "image");
        return ret ? Result.success("上传成功") : Result.fail("上传失败");
    }
    @RequiresRoles("student")
    @DeleteMapping("image")
    public Result deleteWorkImage(@RequestHeader("Authorization") String authorization, @RequestParam Long tuid){
        Long uid = JWTUtil.getUserId(authorization);
        UserTask userTask = userTaskService.getById(tuid);
        if (!Objects.equals(userTask.getUserId(), uid)){
            return Result.fail("没有操作权限");
        }
        Work work = workService.getOne(new LambdaQueryWrapper<Work>().eq(Work::getTuid, tuid));
        boolean ret = workService.deleteUploadWorkFile(work, "image");
        return ret ? Result.success("删除成功"): Result.fail("删除失败，请刷新后重试");
    }
    @RequiresRoles("student")
    @PostMapping("video")
    public Result submitWorkVideo(@RequestHeader String authorization,
                                  @RequestParam MultipartFile file,
                                  @RequestParam Long tuid){
        Long uid = JWTUtil.getUserId(authorization);
        boolean ret = workService.submitFile(file, tuid, uid, "video");
        return ret ? Result.success("上传成功"): Result.fail("上传失败");
    }
    @RequiresRoles("student")
    @DeleteMapping("video")
    public Result deleteWorkVideo(@RequestHeader("Authorization") String authorization, @RequestParam Long tuid){
        Long uid = JWTUtil.getUserId(authorization);
        UserTask userTask = userTaskService.getById(tuid);
        if (!Objects.equals(userTask.getUserId(), uid)){
            return Result.fail("没有操作权限");
        }
        Work work = workService.getOne(new LambdaQueryWrapper<Work>().eq(Work::getTuid, tuid));
        boolean ret = workService.deleteUploadWorkFile(work, "video");
        return ret ? Result.success("删除成功"): Result.fail("删除失败，请刷新后重试");
    }

    @GetMapping("manage/list")
    public Result manageList(@RequestParam("userId") Long userId,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum){
        IPage<Work> page = new Page<>(pageNum, pageSize);
        MPJLambdaWrapper<Work> mpjLambdaWrapper = new MPJLambdaWrapper<Work>()
                .selectAll(Work.class)
                .selectAs(Task::getTemp, WorkDao::getTemp)
                .leftJoin(UserTask.class, UserTask::getId, Work::getTuid)
                .leftJoin(Task.class, Task::getId, UserTask::getTaskId);
        IPage<WorkDao> pageWorks = workService.selectJoinListPage(page, WorkDao.class, mpjLambdaWrapper);
        return Result.success(pageWorks);
    }
    @RequiresRoles("student")
    @ApiOperation("获取自己的作业")
    @GetMapping("user/list")
    public Result listWork(@RequestHeader("Authorization") String authorization){
        System.out.println("authorization:"+authorization);
        Long uid = JWTUtil.getUserId(authorization);
        List<UserWorkDao> userWorkDaos =  userTaskService.selectJoinList(UserWorkDao.class, new MPJLambdaWrapper<UserTask>()
                .selectAs(Task::getTemp, UserWorkDao::getTemp)
                        .selectAs(Work::getId, UserWorkDao::getId)
                        .selectAs(Work::getImageUrl, UserWorkDao::getImageUrl)
                        .selectAs(Work::getVideoUrl, UserWorkDao::getVideoUrl)
                        .selectAs(UserTask::getId, UserWorkDao::getTuid)
                .rightJoin(Task.class, Task::getId, UserTask::getTaskId)
                .leftJoin(Work.class, Work::getTuid, UserTask::getId)
                .eq(UserTask::getUserId, uid)
        ).stream().peek(
                userWorkDao -> {
                    if (userWorkDao.getImageUrl() != null){
                        userWorkDao.setImageUrl(HOST+userWorkDao.getImageUrl());
                    }
                    if (userWorkDao.getVideoUrl() != null){
                        userWorkDao.setVideoUrl(HOST+userWorkDao.getVideoUrl());
                    }

                }
        ).collect(Collectors.toList());
        return Result.success(userWorkDaos);
    }

    @RequiresRoles("teacher")
    @ApiOperation("获取用户提交的作业")
    @GetMapping("user/list/{uid}")
    public Result listUserWork(@PathVariable("uid") Long uid){
        List<UserWorkDao> userWorkDaos =  userTaskService.selectJoinList(UserWorkDao.class, new MPJLambdaWrapper<UserTask>()
                .selectAs(Task::getTemp, UserWorkDao::getTemp)
                .selectAs(Work::getId, UserWorkDao::getId)
                .selectAs(Work::getImageUrl, UserWorkDao::getImageUrl)
                .selectAs(Work::getVideoUrl, UserWorkDao::getVideoUrl)
                .selectAs(UserTask::getId, UserWorkDao::getTuid)
                .rightJoin(Task.class, Task::getId, UserTask::getTaskId)
                .leftJoin(Work.class, Work::getTuid, UserTask::getId)
                .eq(UserTask::getUserId, uid)
        ).stream().peek(
                userWorkDao -> {
                    if (userWorkDao.getImageUrl() != null){
                        userWorkDao.setImageUrl(HOST+userWorkDao.getImageUrl());
                    }
                    if (userWorkDao.getVideoUrl() != null){
                        userWorkDao.setVideoUrl(HOST+userWorkDao.getVideoUrl());
                    }

                }
        ).collect(Collectors.toList());
        return Result.success(userWorkDaos);
    }
}
