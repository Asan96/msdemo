package com.ydjk.msdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@TableName("work")
@Data
public class Work {
    @ApiModelProperty(hidden = true)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long tuid;

    private String imageUrl;

    private String videoUrl;

    private Date submitTime;
}
