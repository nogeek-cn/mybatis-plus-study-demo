package com.darian.demo_high.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Data
// 支持注解
@TableName("user")
public class User extends Model<User> {
    @TableId
    private Long id;
    private String name;
    private Integer age;
    private String email;
    private Long managerId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
    private Integer version;

    // 自定义查询中，不会加上逻辑未删除限定
    // (0, 未删除，1 已删除)
    @TableLogic
    @TableField(select = false) // select 语句，不显示，
    private Integer deleted;

}
