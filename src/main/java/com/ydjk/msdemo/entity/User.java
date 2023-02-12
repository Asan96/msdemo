package com.ydjk.msdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@TableName("user")
@Data
public class User {
    @ApiModelProperty(hidden = true)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    @ApiModelProperty(hidden = true)
    private Date lastLogin;

    @ApiModelProperty(hidden = true)
    private String salt;

    private String name;

    private Boolean isTeacher;
}
