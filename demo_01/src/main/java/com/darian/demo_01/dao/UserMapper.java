package com.darian.demo_01.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.darian.demo_01.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
