package spring.dev.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import spring.dev.dao.UserDao;
import spring.dev.domain.Level;
import spring.dev.domain.User;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import static spring.dev.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static spring.dev.service.UserService.MIN_RECOMEND_FOR_GOLD;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserServiceTest {
    @Autowired
    UserService userService;
    UserDao userDao;

    List<User> users;

    @Before
    public void setUp(){
        this.userDao = userService.userDao;
        users = Arrays.asList(
                new User("bumjun","박범진","p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
                new User("joytouch","강명성","p2",Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),//승급대상
                new User("erwins","신승한","p3",Level.SILVER, 60, MIN_RECOMEND_FOR_GOLD-1),
                new User("madnite1","이상호","p4",Level.SILVER, 60, MIN_RECOMEND_FOR_GOLD),//승급대상
                new User("green","오민규","p5",Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }


    @Test
    public void bean(){
        assertThat(this.userService, is(notNullValue()));
    }


    @Test
    public void upgradeLevels(){
        userDao.deleteAll();

        for(User user:users) userDao.add(user);

        userService.upgradeLevels();

        checkLevel(users.get(0), false);
        checkLevel(users.get(1), true);
        checkLevel(users.get(2), false);
        checkLevel(users.get(3), true);
        checkLevel(users.get(4), false);

    }

    @Test
    public void add(){
        userDao.deleteAll();

        User userWithLevel = users.get(4); //GOLD레벨
        User userWithoutLevle = users.get(0);
        userWithoutLevle.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevle);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevle.getId());

        assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
        assertThat(userWithoutLevelRead.getLevel(), is(userWithoutLevelRead.getLevel()));

    }

    private void checkLevel(User user, boolean upgraded){
        User userUpdate = userDao.get(user.getId());
        if(upgraded){
            assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
        }else{
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
        }
    }
}
