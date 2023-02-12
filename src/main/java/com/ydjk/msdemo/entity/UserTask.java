package com.ydjk.msdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@TableName("user_task")
@Data
@AllArgsConstructor
public class UserTask {
    @ApiModelProperty(hidden = true)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long taskId;

    private Long userId;

    private Date allotTime;

    public UserTask(Long taskId, Long userId){
        this.taskId = taskId;
        this.userId = userId;
    }
}
