package com.darian.demo_03.dao;

import com.darian.demo_03.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class InsertTest {

    @Autowired
    private UserMapper userMapper;
    @Test
    public void insertTest() {
        User user = new User();
        user.setName("sdfasdgasdg");
        user.setAge(23);
        user.setManagerId(1088248166370832385L);
        user.setCreateTime(LocalDateTime.now());
        Integer insert = userMapper.insert(user);
        log.info(insert.toString());
        System.out.println(user.getId());
    }
}
