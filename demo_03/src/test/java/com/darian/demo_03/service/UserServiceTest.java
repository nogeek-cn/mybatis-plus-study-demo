package com.darian.demo_03.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.darian.demo_03.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void getOne() {
        User one = userService.getOne(Wrappers.<User>lambdaQuery()
                .gt(User::getAge, 25));
        System.out.println(one);
    }

    @Test
    public void saveBatch() {
        // batchSize 默认 1000 条一次提交
        boolean b = userService.saveBatch(
                Arrays.asList(new User("sdfsd"),
                        new User("sdfs")),
                23);
        System.out.println(b);
    }

    @Test
    public void saveOrUpdateBatch() {
        // batchSize 默认 1000 条一次提交
        boolean b = userService.saveOrUpdateBatch(
                Arrays.asList(new User("sddffsd"),
                        new User("sdfdfs")),
                23);
        System.out.println(b);
    }

    @Test
    public void chain() {
        userService.lambdaQuery()
                .gt(User::getAge, 23)
                .like(User::getName, "雨")
                .list()
                .forEach(System.out::println);
    }

    @Test
    public void chain1() {
        boolean update =userService.lambdaUpdate()
                .gt(User::getAge, 23)
                .like(User::getName, "雨")
                .update();
        System.out.println(update);
    }


    @Test
    public void chain3() {
        boolean update =userService.lambdaUpdate()
                .gt(User::getAge, 23)
                .like(User::getName, "雨")
                .remove(); // 删除， update remove
        System.out.println(update);
    }
}
