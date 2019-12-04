# mybatis-plus-study-demo

<https://mybatis.plus/guide/>

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





##### 其它以条件构造器为参数的查询方法。

```java
@Test
public void selectByWrapperMaps() {
    
    QueryWrapper<User> queryWrapper = Wrappers.query();

    queryWrapper.like("name", "雨")
            .lt("age", 40)
            .select("id", "name");


    userMapper.selectMaps(queryWrapper)
            .forEach(item ->
                    log.info(item.toString()));
}

// {name=张雨琪, id=1094590409767661570}
// {name=刘红雨, id=1094592041087729666}

// selectMaps 返回的是 List<Map<String, Object>>
```





三、统计查询：

11、按照直属上级分组，查询每组的平均年龄、最大年龄、最小年龄。

并且只取年龄总和小于500的组。

select avg(age) avg_age,min(age) min_age,max(age) max_age

from user

group by manager_id

having sum(age) <500

```java
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

// {max_age=40, avg_age=40.0000, min_age=40}
//  {max_age=25, avg_age=25.0000, min_age=25}
//  {max_age=32, avg_age=26.1429, min_age=23}
```





```java
    @Test
    public void selctByWrapperObjs(){
        QueryWrapper<User> queryWrapper = Wrappers.query();
        queryWrapper.like("name", "雨")
                .lt("age", 40)
                .select("id", "name");


        userMapper.selectObjs(queryWrapper)
                .forEach(item -> log.info(item.toString()));
    }
```

- `#selectObjs` 只返回第一列





```java
@Test
public void selctByWrapperObjs(){
    QueryWrapper<User> queryWrapper = Wrappers.query();
    queryWrapper.like("name", "雨")
            .lt("age", 40);


    Integer integer = userMapper.selectCount(queryWrapper);
    System.out.println("总记录数：" + integer);
}
```

- `#selectCount`  count(1)





```
@Test
public void selctByWrapperOne() {
    QueryWrapper<User> queryWrapper = Wrappers.query();
    queryWrapper.like("name", "雨")
            .lt("age", 40);


    User user = userMapper.selectOne(queryWrapper);
    System.out.println(user);
}
```

- `#selectOne` 返回空 或者一个。



#### `LambdaQueryWrapper`  lambda 构造器减少编写的错误

```java
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
public void selectByWrapperLambda2() {
    new LambdaQueryChainWrapper<User>(userMapper)
        .like(User::getName, "王")
        .ge(User::getAge, 20)
        .list()
        .stream()
        .forEach(System.out::println);
}
```



#### 使用条件构造器的自定义 Sql  

- version >= 3.0.7



```java
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("slect * from user ${eq.customSqlSegment}")
    List<User> selectAll(@Param(Constants.WRAPPER) Wrapper<User> wrapper);
}
```





也可以自定义 xml

```java
<select id="slectALL" resultType="com.darian.demo_02.endity.User">
        slect * from user ${ew.customSqlSegment}
</select>
```

**不用加 where ** mybagis-plus 会自动加

#### mybatis 分页查询

MP 分页插件实现物理分页



```java
@Configuration
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }
}
```



```java
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
public void selectByWrapperByPage2() {
    LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery();
    lambdaQuery.like(User::getName, "雨")
        .lt(User::getAge, 40);
    Page<User> userPage = new Page<>(1, 2);
    IPage<Map<String, Object>> userIPage = userMapper.selectMapsPage(userPage, lambdaQuery);
    System.out.println(userIPage.getPages());
    System.out.println(userIPage.getTotal());
    System.out.println(userIPage.getRecords());
}
```

- `#selectPage` 
- `#selectMapsPage` 

- ```java
  Page<User> userPage = new Page<>(1, 2, false);
  ```

  不查询返回总数

### 更新

#### 根据 id 更新

#### 以条件构造器中作为参数的更新方法

#### 条件构造器中 set 方法使用

```java
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
```

### 删除方法

#### 根据id删除

#### 其它普通删除方法

#### 根据条件构造器删除方法

```
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

```



## ActiveRecord 模式

demo-3

AR 探索

- AR 模式简介
- MP 中 AR 模式的实现



```java
public class User extends Model<User> {
    // ....
}
```

```java
@Test
public void insert() {
    User user = new User();
    user.setId(1L);
    user.setName("sdag");

    boolean insert = user.insert();
    System.out.println(insert);
}
@Test
public void selecyById() {
    User user = new User();
    user.setId(1L);
    user.setName("sfdagadddf");

    User selectById = user.selectById();
    System.out.println(user == selectById);
}

@Test
public void updateById() {
    User user = new User();
    user.setId(1L);
    user.setName("sfdadf");

    boolean row = user.updateById();
    System.out.println(row);
}


@Test
public void deleteById() {
    User user = new User();
    user.setId(1L);
    user.setName("sfdadf");

    boolean row = user.deleteById();
    System.out.println(row);
}

@Test
public void insertOrUpdateId(){
    User user = new User();
    user.setId(1L);
    user.setName("sfdasdfsddf");

    boolean row = user.insertOrUpdate();
    System.out.println(row);
}
```





## MP主键策略

### MP支持的主键策略介绍

### 局部主键策略实现

### 全局主键策略实现

```java
// 默认主键为id
@TableId(type = IdType.AUTO) // 标记主键
private Long id;
```





```java
/**
 * 生成ID类型枚举类
 *
 * @author hubin
 * @since 2015-11-10
 */
@Getter
public enum IdType {
    /**
     * 数据库ID自增
     */
    AUTO(0),
    /**
     * 该类型为未设置主键类型
     */
    NONE(1),
    /**
     * 用户输入ID
     * <p>该类型可以通过自己注册自动填充插件进行填充</p>
     */
    INPUT(2),

    /* 以下3种类型、只有当插入对象ID 为空，才自动填充。 */
    /**
     * 全局唯一ID (idWorker)
     */
    ID_WORKER(3),
    /**
     * 全局唯一ID (UUID)
     */
    UUID(4),
    /**
     * 字符串全局唯一ID (idWorker 的字符串表示)
     */
    ID_WORKER_STR(5);

    private final int key;

    IdType(int key) {
        this.key = key;
    }
}
```



- 主键会给你回写。

```java
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
```

```properties
# 全局id配置
mybatis-plus.global-config.db-config.id-type=auto
```

局部策略优于全局策略



## MP 配置

### 基本配置

https://mybatis.plus/config/#%E5%9F%BA%E6%9C%AC%E9%85%8D%E7%BD%AE





`configuration` 和 `configLocation` 不能同时出现



- fieldStrategy : 默认 not_null，为空的时候，不拼接在 SQL 语句中

全局，局部都可以配置，局部优于全局



##### 表明前缀



### 通用 Service

#### 基本方法

#### 批量方法

#### 链式调用方法

service 可以解决异常问题，解决过长的 SQL 的问题。

```java
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
```







