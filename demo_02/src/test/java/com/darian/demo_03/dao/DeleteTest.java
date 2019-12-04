package com.darian.demo_03.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.darian.demo_03.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DeleteTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void deleteById() {
        int i = userMapper.deleteById(1L);
        System.out.println(i);
    }

    @Test
    public void deleteByMapTest() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", "sssss");
        int i = userMapper.deleteByMap(paramMap);
        System.out.println(i);
    }

    @Test
    public void deleteBatchIds() {
        int i = userMapper.deleteBatchIds(Arrays.asList(1L, 2L, 3L));
        System.out.println(i);
    }

    @Test
    public void deleteByWrapper() {
        LambdaQueryWrapper<User> deleteQuery = Wrappers.lambdaQuery();
        deleteQuery.eq(User::getAge, 456);

        int i = userMapper.delete(deleteQuery);
        System.out.println(i);
    }
}
