package com.darian.demo_high.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
// 支持注解
@TableName("user")
@NoArgsConstructor
public class User extends Model<User> {

    public User(String name) {
        this.name = name;
    }

    // 默认主键为id
    @TableId(type = IdType.AUTO) // 标记主键
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
