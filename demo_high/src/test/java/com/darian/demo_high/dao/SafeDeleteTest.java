package com.darian.demo_high.dao;

import com.darian.demo_high.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SafeDeleteTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void deleteById() {
        int i = userMapper.deleteById(1094592041087729666L);
        System.out.println(i);
    }

    @Test
    public void selectByList() {
        userMapper.selectList(null)
                .forEach(System.out::println);
    }

    @Test
    public void updateById(){
        // 只能更新未删除的数据
        User user = new User();
        user.setId(1087982257332887553L);
        user.setName("sdf");
        int i = userMapper.updateById(user);
        System.out.println(i);
    }
}
