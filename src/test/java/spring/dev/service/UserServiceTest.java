package spring.dev.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import spring.dev.dao.UserDao;
import spring.dev.domain.Level;
import spring.dev.domain.User;

import javax.jws.Oneway;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.fail;
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
    @Autowired
    PlatformTransactionManager transactionManager;

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
    public void upgradeLevels() throws SQLException{
        userDao.deleteAll();

        for(User user:users) userDao.add(user);

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);

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

    @Test
    public void upgradeAllOrNothing() throws SQLException{
        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);//userDao를 수동 DI 해준다.
        testUserService.setTransactionManager(this.transactionManager);

        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        }catch(TestUserServiceException e){

        }

        checkLevelUpgraded(users.get(1), false);
    }

    private void checkLevelUpgraded(User user, boolean upgraded){
        User userUpdate = userDao.get(user.getId());
        if(upgraded){
            assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
        }else{
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
        }
    }


    static class TestUserService extends UserService {
        private String id;

        private TestUserService(String id){
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user){
            if(user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {

    }
}
