package com.ydjk.msdemo.dao;

import com.ydjk.msdemo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class UserDao {
    private Long id;
    private String name;
    private Integer imageCount = 0;
    private Integer videoCount = 0;
//
//    public UserDao(User user){
//        this.id = user.getId();
//        this.name = user.getName();
//    }
}
