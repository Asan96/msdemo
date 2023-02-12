package com.ydjk.msdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.ydjk.msdemo.entity.Work;
import com.ydjk.msdemo.mapper.WorkMapper;
import com.ydjk.msdemo.service.UserTaskService;
import com.ydjk.msdemo.service.WorkService;
import com.ydjk.msdemo.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

@Service
public class WorkServiceImpl extends MPJBaseServiceImpl<WorkMapper, Work> implements WorkService {

    @Autowired
    private UserTaskService userTaskService;

    @Value("${param.staticPath}")
    private String staticPath;
    @Override
    @Transactional
    public boolean submitFile(MultipartFile file, Long tuid, Long uid, String type_){
        if (!(Objects.equals(type_, "image") || Objects.equals(type_, "video"))) {
            return false;
        }
        String suffix = FileUtil.getFileSuffix(file.getOriginalFilename());
        String filename = UUID.randomUUID()+suffix;
        String storePath = staticPath+"/work/"+uid+"/";
        String relPath = FileUtil.uploadFile(file, storePath, filename);
        Work work = new Work();
        if (Objects.equals(type_, "image")){
            work.setImageUrl(relPath);
        }else if (Objects.equals(type_, "video")){
            work.setVideoUrl(relPath);
        }
        work.setTuid(tuid);
        UpdateWrapper<Work> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("tuid", tuid);
        this.saveOrUpdate(work, updateWrapper);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteUploadWorkFile(Work work, String type_){
        String filePath;
        LambdaUpdateWrapper<Work> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Work::getId,work.getId());
        if (Objects.equals(type_, "image")){
            filePath = staticPath + work.getImageUrl();
            updateWrapper.set(Work::getImageUrl,null);
        } else if (Objects.equals(type_, "video")) {
            filePath = staticPath + work.getVideoUrl();
            updateWrapper.set(Work::getVideoUrl,null);
        }else{
            return false;
        }
        File file = new File(filePath);
        boolean ret = file.delete();
        boolean updateRet = this.update(updateWrapper);
        return updateRet;
    }


}
