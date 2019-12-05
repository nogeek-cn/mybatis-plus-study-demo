package com.darian.demo_high.dao;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.darian.demo_high.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UpdateTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void updateTest() {
        User user = new User();
        user.setId(1088248166370832385L);
        user.setAge(26);
        int i = userMapper.updateById(user);
        System.out.println(i);
    }

    @Test
    public void updateTest2() {
        UpdateWrapper<User> updateWrapper = Wrappers.update();
        updateWrapper.eq("name", "雨");
        User user = new User();
        user.setId(1088248166370832385L);
        user.setAge(26);
        int i = userMapper.update(user, updateWrapper);
        System.out.println(i);
    }


    @Test
    public void updateTest3() {
        UpdateWrapper<User> updateWrapper = Wrappers.update();
        updateWrapper.eq("name", "雨")
                .set("age", 30);
        int i = userMapper.update(null, updateWrapper);
        System.out.println(i);
    }

    @Test
    public void updateTest4() {
        LambdaUpdateWrapper<User> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(User::getName, "雨")
                .set(User::getAge, 30);
        int i = userMapper.update(null, updateWrapper);
        System.out.println(i);
    }

    @Test
    public void updateTest5() {

        boolean update = new LambdaUpdateChainWrapper<User>(userMapper)
                .eq(User::getName, "雨")
                .set(User::getAge, 30)
                .update();
        System.out.println(update);
    }

}
