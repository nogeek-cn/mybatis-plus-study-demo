package com.darian.demo_03.dao;

import com.darian.demo_03.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ARTest {

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


}
