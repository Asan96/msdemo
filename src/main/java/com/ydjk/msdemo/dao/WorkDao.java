package com.ydjk.msdemo.dao;

import lombok.Data;

import java.util.Date;

@Data
public class WorkDao {
    private Long id;

    private String imageUrl;

    private String videoUrl;

    private Date submitTime;

    private String temp;
}
