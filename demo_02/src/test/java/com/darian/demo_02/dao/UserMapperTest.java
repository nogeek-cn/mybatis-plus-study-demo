package com.darian.demo_02.dao;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.darian.demo_02.dao.UserMapper;
import com.darian.demo_02.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;


    /**
     * 名字中包含雨并且年龄小于40
     * <br>Darian
     **/
    @Test
    public void setByWrapper1() {
//        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        QueryWrapper<User> userQueryWrapper = Wrappers.query();
        userQueryWrapper.like("name", "雨")
                .lt("age", 40);
        userMapper.selectList(userQueryWrapper)
                .forEach(item -> log.info(item.toString()));
    }

    /**
     * 名字中包含雨年并且龄大于等于20且小于等于40并且email不为空
     * <br>Darian
     **/
    @Test
    public void setByWrapper2() {
        QueryWrapper<User> userQueryWrapper = Wrappers.query();
        userQueryWrapper.like("name", "雨")
                .between("age", 1, 100)
                .isNotNull("email");
        userMapper.selectList(userQueryWrapper)
                .forEach(item -> log.info(item.toString()));
    }

    /**
     * 名字为王姓或者年龄大于等于25，按照年龄降序排列，年龄相同按照id升序排列
     * <br>Darian
     **/
    @Test
    public void setByWrapper3() {
        QueryWrapper<User> userQueryWrapper = Wrappers.query();
        // right 右边 %
        userQueryWrapper.likeRight("name", "雨")
                .or()
                .ge("age", 1)
                .orderByDesc("age")
                .orderByAsc("id");
        userMapper.selectList(userQueryWrapper)
                .forEach(item -> log.info(item.toString()));
    }

    /**
     * 创建日期为2019年2月14日并且直属上级为名字为王姓
     *
     * <br>Darian
     **/
    @Test
    public void setByWrapper4() {
        QueryWrapper<User> userQueryWrapper = Wrappers.query();
        // right 右边 %
        userQueryWrapper.apply("date_format(create_time,'%Y-%m-%d') = {0}", "2019-02-14")
                .inSql("manager_id", "select id from user where name like '王%'");

        userMapper.selectList(userQueryWrapper)
                .forEach(item -> log.info(item.toString()));
    }

    /**
     * 名字为王姓并且（年龄小于40或邮箱不为空）
     *
     * <br>Darian
     **/
    @Test
    public void setByWrapper5() {
        QueryWrapper<User> userQueryWrapper = Wrappers.query();
        // right 右边 %
        userQueryWrapper.likeRight("name", "王")
                .and(wq -> wq.lt("age", 40)
                        .or().isNotNull("email"));

        userMapper.selectList(userQueryWrapper)
                .forEach(item -> log.info(item.toString()));
    }

    /**
     * 名字为王姓或者（年龄小于40并且年龄大于20并且邮箱不为空）
     *
     * <br>Darian
     **/
    @Test
    public void setByWrapper6() {
        QueryWrapper<User> userQueryWrapper = Wrappers.query();
        // right 右边 %
        userQueryWrapper.likeRight("name", "王")
                .or(wq -> wq.lt("age", 40)
                        .gt("age", 20)
                        .isNotNull("email"));

        userMapper.selectList(userQueryWrapper)
                .forEach(item -> log.info(item.toString()));
    }

    /**
     * （年龄小于40或邮箱不为空）并且名字为王姓
     *
     * <br>Darian
     **/
    @Test
    public void setByWrapper7() {
        QueryWrapper<User> userQueryWrapper = Wrappers.query();
        // right 右边 %
        userQueryWrapper.nested(wq -> wq.lt("age", 40)
                .or().isNotNull("email"))
                .likeRight("name", "王");

        userMapper.selectList(userQueryWrapper)
                .forEach(item -> log.info(item.toString()));
    }

    /**
     * 年龄为30、31、34、35
     * <br>Darian
     **/
    @Test
    public void setByWrapper8() {
        QueryWrapper<User> userQueryWrapper = Wrappers.query();
        // right 右边 %
        userQueryWrapper.in("age", Arrays.asList(30, 31, 34, 35));

        userMapper.selectList(userQueryWrapper)
                .forEach(item -> log.info(item.toString()));
    }


    /**
     * 只返回满足条件的其中一条语句即可
     * last 只会执行最后一个，有 sql 注入的风险
     * <br>Darian
     **/
    @Test
    public void setByWrapper9() {
        QueryWrapper<User> userQueryWrapper = Wrappers.query();
        // right 右边 %
        userQueryWrapper.in("age", Arrays.asList(30, 31, 34, 35))
                .last(" limit 1");

        userMapper.selectList(userQueryWrapper)
                .forEach(item -> log.info(item.toString()));
    }

    /**
     * 名字中包含雨并且年龄小于40(需求1加强版)
     * <br>Darian
     **/
    @Test
    public void setByWrapper10() {
        QueryWrapper<User> userQueryWrapper = Wrappers.query();
        // right 右边 %
        userQueryWrapper.select("id", "name")
                .like("name", "雨")
                .lt("age", 40);

        userMapper.selectList(userQueryWrapper)
                .forEach(item -> log.info(item.toString()));
    }

    /**
     * select id,name,age,email from user where name like '%雨%' and age<40
     * <br>Darian
     **/
    @Test
    public void setByWrapper11() {
        QueryWrapper<User> userQueryWrapper = Wrappers.query();
        // right 右边 %
        userQueryWrapper.select(User.class,
                info -> !info.getColumn().equals("create_time")
                        && !info.getColumn().equals("manager_id"))
                .like("name", "雨")
                .lt("age", 40);

        userMapper.selectList(userQueryWrapper)
                .forEach(item -> log.info(item.toString()));
    }


    @Test
    public void selectByIdTest() {
        User user = userMapper.selectById(1087982257332887553L);
        log.info(user.toString());
    }

    @Test
    public void selectByIdsTest() {
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1087982257332887553L,
                1088248166370832385L));

        log.info("" + users.size());
        users.stream().forEach(item ->
                log.info(item.toString()));
    }

    @Test
    public void selectByMapTest() {
        Map<String, Object> columnMap = new HashMap<>();

        // columnMap -> key -> column 数据库中的列
        columnMap.put("name", "王天风");

        userMapper.selectByMap(columnMap)
                .forEach(item -> log.info(item.toString()));


        columnMap.put("age", 43543);
        userMapper.selectByMap(columnMap)
                .forEach(item -> log.info(item.toString()));
    }


    @Test
    public void insertTest() {
        User user = new User();
        user.setName("sdfasdgasdg");
        user.setAge(23);
        user.setManagerId(1088248166370832385L);
        user.setCreateTime(LocalDateTime.now());
        Integer insert = userMapper.insert(user);
        log.info(insert.toString());
    }

    @Deprecated
    @Test
    public void selectTest() {
        List<User> userList = userMapper.selectList(null);
        Assert.assertEquals(5, userList.size());
        userList.stream()
                .forEach(item -> log.info(item.toString()));
    }
}