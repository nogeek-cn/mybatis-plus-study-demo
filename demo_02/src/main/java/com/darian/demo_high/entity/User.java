package com.darian.demo_high.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
// 支持注解
@TableName("user")
public class User {
    // 默认主键为id
    @TableId // 标记主键
    private Long id;
    //
    @TableField(value = "name") // 标记表的名字
    private String name;
    //
    private Integer age;
    //
    private String email;
    // 主管
    private Long managerId;
    //
    private LocalDateTime createTime;

    // 备注，不对应的数据库的时候
//    @TableField(exist = false)
//    private String remark;
}
