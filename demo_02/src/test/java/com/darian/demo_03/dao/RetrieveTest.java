package com.darian.demo_03.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.darian.demo_03.entity.User;
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
    public void selectByWrapperByPage2() {
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.like(User::getName, "雨")
                .lt(User::getAge, 40);
        Page<User> userPage = new Page<>(1, 2, false);
        IPage<Map<String, Object>> userIPage = userMapper.selectMapsPage(userPage, lambdaQuery);
        System.out.println(userIPage.getPages());
        System.out.println(userIPage.getTotal());
        System.out.println(userIPage.getRecords());
    }

    @Test
    public void selectByWrapperByPage() {
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.like(User::getName, "雨")
                .lt(User::getAge, 40);
        Page<User> userPage = new Page<>(1, 2);
        IPage<User> userIPage = userMapper.selectPage(userPage, lambdaQuery);
        System.out.println(userIPage.getPages());
        System.out.println(userIPage.getTotal());
        System.out.println(userIPage.getRecords());
    }

    @Test
    public void selectByWrapperBy() {
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.like(User::getName, "雨")
                .lt(User::getAge, 40);
        userMapper.selectAll(lambdaQuery)
                .forEach(System.out::println);
    }


    @Test
    public void selectByWrapperLambda2() {
        new LambdaQueryChainWrapper<User>(userMapper)
                .like(User::getName, "王")
                .ge(User::getAge, 20)
                .list()
                .stream()
                .forEach(System.out::println);
    }

    @Test
    public void selectByWrapperLambda1() {
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.likeRight(User::getName, "王")
                .and(lqw ->
                        lqw.lt(User::getAge, 40)
                                .or().isNotNull(User::getCreateTime)
                );
        userMapper.selectList(lambdaQuery)
                .forEach(System.out::println);
    }

    @Test
    public void selectByWrapperLambda() {
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery();
//        LambdaQueryWrapper<User> lambdaQuery = new QueryWrapper<User>().lambda();
//        LambdaQueryWrapper<User> lambdaQuery = new LambdaQueryWrapper<>();
        lambdaQuery.like(User::getName, "雨")
                .lt(User::getAge, 40);
        userMapper.selectList(lambdaQuery)
                .forEach(System.out::println);
    }

    @Test
    public void selctByWrapperOne() {
        QueryWrapper<User> queryWrapper = Wrappers.query();
        queryWrapper.like("name", "雨")
                .lt("age", 40);


        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);
    }


    @Test
    public void selctByWrapperCount() {
        QueryWrapper<User> queryWrapper = Wrappers.query();
        queryWrapper.like("name", "雨")
                .lt("age", 40);


        Integer integer = userMapper.selectCount(queryWrapper);
        System.out.println("总记录数：" + integer);
    }

    @Test
    public void selctByWrapperObjs() {
        QueryWrapper<User> queryWrapper = Wrappers.query();
        queryWrapper.like("name", "雨")
                .lt("age", 40);


        userMapper.selectObjs(queryWrapper)
                .forEach(item -> log.info(item.toString()));
    }


    /**
     * 按照直属上级分组，查询每组的平均年龄、最大年龄、最小年龄。
     * <p>
     * 并且只取年龄总和小于500的组。
     * <br>Darian
     **/
    @Test
    public void selectByWrapperAvg() {
        QueryWrapper<User> queryWrapper = Wrappers.query();
        queryWrapper.select(" avg(age) avg_age ",
                "min(age) min_age",
                "max(age) max_age")
                .groupBy("manager_id")
                .having("sum(age)<{0}", 500);

        userMapper.selectMaps(queryWrapper)
                .forEach(item -> log.info(item.toString()));
    }


    @Test
    public void selectByWrapperMaps() {
        QueryWrapper<User> queryWrapper = Wrappers.query();
        queryWrapper.like("name", "雨")
                .lt("age", 40)
                .select("id", "name");


        userMapper.selectMaps(queryWrapper)
                .forEach(item -> log.info(item.toString()));
    }


    @Test
    public void selectByWrapperAllEq() {
        QueryWrapper<User> queryWrapper = Wrappers.query();

        Map<String, Object> params = new HashMap<>();
        params.put("name", "王天风");
        params.put("age", null);

        queryWrapper.allEq((k, v) -> !k.equals("name"), params);

        userMapper.selectList(queryWrapper)
                .forEach(item -> log.info(item.toString()));
    }

    @Test
    public void selectByWrapperEntity() {
        User userWhere = new User();
        userWhere.setName("刘红雨");
        QueryWrapper<User> queryWrapper = Wrappers.query(userWhere);
        queryWrapper.like("name", "雨");

        userMapper.selectList(queryWrapper)
                .forEach(item -> log.info(item.toString()));
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
                .forEach(item -> log.info(item.toString()));
    }

}
