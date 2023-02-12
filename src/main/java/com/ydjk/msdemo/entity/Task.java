package com.ydjk.msdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@TableName("task")
@Data
public class Task {
    @ApiModelProperty(hidden = true)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer temp;
}
