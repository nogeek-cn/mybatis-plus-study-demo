package com.darian.demo_high.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    //
    private Long id;
    //
    private String name;
    //
    private Integer age;
    //
    private String email;
    // 主管
    private Long managerId;
    //
    private LocalDateTime createTime;
}
