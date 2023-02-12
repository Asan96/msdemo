package com.ydjk.msdemo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
@Slf4j
@Configuration
public class FileUtil {
    public static String storePath;

    @Value("${param.staticPath}")
    public void setStorePath(String storePath) {
        FileUtil.storePath = storePath;
    }


    public static String uploadFile(MultipartFile file, String uploadDirPath, String filename) {
        File uploadPathFile = new File(uploadDirPath);
        if (!uploadPathFile.exists()) {
            uploadPathFile.mkdirs();
        }
        String newFilename = filename != null ? filename : file.getOriginalFilename();
        File newFile = new File(uploadDirPath + newFilename);
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String absPath = newFile.getAbsolutePath();
        return absPath.replace("\\", "/").replace(storePath, "");
    }

    public static String getFileSuffix(String fileName) {
        if (fileName == null) return null;
        return fileName.substring(fileName.lastIndexOf("."));//例如：abc.png  截取后：.png
    }
}
