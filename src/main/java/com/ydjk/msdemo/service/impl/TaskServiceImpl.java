package com.ydjk.msdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydjk.msdemo.entity.Task;
import com.ydjk.msdemo.mapper.TaskMapper;
import com.ydjk.msdemo.service.TaskService;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
}
