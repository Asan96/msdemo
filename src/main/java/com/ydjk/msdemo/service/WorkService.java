package com.ydjk.msdemo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.yulichang.base.MPJBaseService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.ydjk.msdemo.dao.WorkDao;
import com.ydjk.msdemo.entity.Work;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface WorkService extends MPJBaseService<Work> {

    @Transactional
    boolean submitFile(MultipartFile file, Long tuid, Long uid, String type_);

    @Transactional
    boolean deleteUploadWorkFile(Work work ,String type_);
}
