package com.darian.demo_02.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.darian.demo_02.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RetrieveTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void selectByWrapperAllEq() {


        QueryWrapper<User> queryWrapper = Wrappers.query();

        Map<String, Object> params = new HashMap<>();
        params.put("name", "王天风");
        params.put("age", null);

        queryWrapper.allEq((k, v) -> !k.equals("name"),
                params);

        userMapper.selectList(queryWrapper)
                .forEach(item ->
                        log.info(item.toString()));
    }

    @Test
    public void selectByWrapperEntity() {
        User userWhere = new User();
        userWhere.setName("刘红雨");

        QueryWrapper<User> queryWrapper = Wrappers.query(userWhere);

        queryWrapper.like("name", "雨");

        userMapper.selectList(queryWrapper)
                .forEach(item ->
                        log.info(item.toString()));
    }


    @Test
    public void old() {
        String name = null;
        String email = null;
        QueryWrapper<User> queryWrapper = Wrappers.query();
        if (StringUtils.isNotEmpty(name)) {
            queryWrapper.like("name", name);
        }
        if (StringUtils.isNotEmpty(email)) {
            queryWrapper.like("name", email);
        }
        userMapper.selectList(queryWrapper)
                .forEach(item ->
                        log.info(item.toString()));
    }


    @Test
    public void condition() {
        String name = null;
        String email = null;
        QueryWrapper<User> queryWrapper = Wrappers.query();
        queryWrapper.like(StringUtils.isNotEmpty(name), "name", name)
                .like(StringUtils.isNotEmpty(email), "email", email);
        userMapper.selectList(queryWrapper)
                .forEach(item ->
                        log.info(item.toString()));
    }

}
