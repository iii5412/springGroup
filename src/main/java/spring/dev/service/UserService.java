package spring.dev.service;

import org.springframework.beans.factory.annotation.Autowired;
import spring.dev.dao.UserDao;
import spring.dev.domain.Level;
import spring.dev.domain.User;

import java.util.List;

public class UserService {
    @Autowired
    UserDao userDao;

    @Autowired
    UserLevelUpgradePolicy userLevelUpgradePolicy;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMEND_FOR_GOLD = 30;

    public void upgradeLevels(){
        List<User> users = userDao.getAll();
        for(User user : users){
            if(userLevelUpgradePolicy.canUpgradeLevel(user)){
                userLevelUpgradePolicy.upgradeLevel(user);
            }
        }
    }



    public void add(User user){
        if(user.getLevel() == null)user.setLevel(Level.BASIC);
        userDao.add(user);
    }
}
