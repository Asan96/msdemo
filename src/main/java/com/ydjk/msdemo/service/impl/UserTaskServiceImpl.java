package com.ydjk.msdemo.service.impl;

import com.github.yulichang.base.MPJBaseServiceImpl;
import com.ydjk.msdemo.entity.UserTask;
import com.ydjk.msdemo.mapper.UserTaskMapper;
import com.ydjk.msdemo.service.UserTaskService;
import org.springframework.stereotype.Service;

@Service
public class UserTaskServiceImpl extends MPJBaseServiceImpl<UserTaskMapper, UserTask> implements UserTaskService {

}
