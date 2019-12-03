# mybatis-plus-study-demo



- 环境

  ```xml
  <dependency>
      <groupId>com.baomidou</groupId>
      <artifactId>mybatis-plus-boot-starter</artifactId>
      <version>${mybatis-plus-boot-starter-version}</version>
  </dependency>
  <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
  </dependency>
  ```

  



## demo_02



### 新增

### 常用注解

- `@TableName`  
- `@TableId ` 
- `@TableField`  



### 排除表字段的三种方式

- `transient` 
- `static` 
- `@TableField(exist = false)`   // 备注，不对应的数据库的时候



```java
package com.darian.demo_02.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
// 支持注解
@TableName("mp_user")
public class User {
    // 默认主键为id
    @TableId // 标记主键
    private Long userId;
    //
    @TableField("name") // 标记表的名字
    private String realName;
    //
    private Integer age;
    //
    private String email;
    // 主管
    private Long managerId;
    //
    private LocalDateTime createTime;

    // 备注，不对应的数据库的时候
    @TableField(exist = false)
    private String remark;
}

```



### 查询

#### 基本的查询方法

- // columnMap -> key -> column 数据库中的列

#### 以条件构造器为参数的查询方法

- 



##### 一、查询需求

1、名字中包含雨并且年龄小于40

​	name like '%雨%' and age<40

```java
@Test
public void setByWrapper1() {
    // QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
    QueryWrapper<User> userQueryWrapper = Wrappers.query();
    userQueryWrapper.like("name", "雨")
        .lt("age", 40);
    userMapper.selectList(userQueryWrapper)
        .forEach(item -> log.info(item.toString()));
}
```



2、名字中包含雨年并且龄大于等于20且小于等于40并且email不为空

   name like '%雨%' and age between 20 and 40 and email is not null

```java
@Test
public void setByWrapper2() {
    QueryWrapper<User> userQueryWrapper = Wrappers.query();
    userQueryWrapper.like("name", "雨")
        .between("age", 1, 100)
        .isNotNull("email");
    userMapper.selectList(userQueryWrapper)
        .forEach(item -> log.info(item.toString()));
}
```



3、名字为王姓或者年龄大于等于25，按照年龄降序排列，年龄相同按照id升序排列

   name like '王%' or age>=25 order by age desc,id asc

```java
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
```



4、创建日期为2019年2月14日并且直属上级为名字为王姓

​      date_format(create_time,'%Y-%m-%d')='2019-02-14' and manager_id in (select id from user where name like '王%')

```java
/**
     * 创建日期为2019年2月14日并且直属上级为名字为王姓
     * 
     * <br>Darian
     **/
@Test
public void setByWrapper4() {
    QueryWrapper<User> userQueryWrapper = Wrappers.query();
    // right 右边 %
    userQueryWrapper
        .apply("date_format(create_time,'%Y-%m-%d') = {0}", "2019-02-14")
        .inSql("manager_id", "select id from user where name like '王%'");

    userMapper.selectList(userQueryWrapper)
        .forEach(item -> log.info(item.toString()));
}
```





5、名字为王姓并且（年龄小于40或邮箱不为空）

​    name like '王%' and (age<40 or email is not null)

```java
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
```



6、名字为王姓或者（年龄小于40并且年龄大于20并且邮箱不为空）

​    name like '王%' or (age<40 and age>20 and email is not null)

```java
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
```





7、（年龄小于40或邮箱不为空）并且名字为王姓

​    (age<40 or email is not null) and name like '王%'

```java
/* （年龄小于40或邮箱不为空）并且名字为王姓
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
```



8、年龄为30、31、34、35

​    age in (30、31、34、35)  

```java
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

```





9、只返回满足条件的其中一条语句即可

limit 1

```java
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
```





##### 二、select中字段不全部出现的查询

10、名字中包含雨并且年龄小于40(需求1加强版)

第一种情况：select id,name

​	           from user

​	           where name like '%雨%' and age<40

```java
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
```





第二种情况：select id,name,age,email

​	           from user

​	           where name like '%雨%' and age<40

```java
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
```



##### 条件构造器中 condition 作用



```java
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
```



##### 条件构造器传入构造对象

默认等值查询

```java
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
```

```sql
SELECT id,name,age,email,manager_id,create_time FROM user WHERE name=? AND name LIKE ? 
```



```java
// 等值 变  like;
@TableField(value = "name",condition = SqlCondition.LIKE)
```





```java
// NOT_EQUAL = "%s&lt;&gt;#{%s}";

// 小于 "%s&lt;#{%s}";
//  大于 "%s&gt;#{%s}";

// 等值 变  like;
@TableField(value = "name",condition = "%s&gt;#{%s}")
```





##### SllEq 方法

- filter 是否放入参数
- value 不为空 -》 sql 等值查询
- value  null  -> sql: is null

```java
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
```







三、统计查询：

11、按照直属上级分组，查询每组的平均年龄、最大年龄、最小年龄。

并且只取年龄总和小于500的组。

select avg(age) avg_age,min(age) min_age,max(age) max_age

from user

group by manager_id

having sum(age) <500



