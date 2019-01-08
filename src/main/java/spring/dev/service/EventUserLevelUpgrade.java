package spring.dev.service;

import org.springframework.beans.factory.annotation.Autowired;
import spring.dev.dao.UserDao;
import spring.dev.domain.Level;
import spring.dev.domain.User;

import static spring.dev.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static spring.dev.service.UserService.MIN_RECOMEND_FOR_GOLD;

public class EventUserLevelUpgrade implements UserLevelUpgradePolicy{
    private UserDao userDao;
    @Override
    public void setUserDao(UserDao userDao){
        this.userDao = userDao;
    }
    @Override
    public boolean canUpgradeLevel(User user){
        Level currentLevel = user.getLevel();
        switch(currentLevel) {
            case BASIC:
                return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER:
                return (user.getRecommend() >= MIN_RECOMEND_FOR_GOLD);
            case GOLD:
                return false;
            default:
                throw new IllegalStateException("Unknown Level : " + currentLevel);
        }
    }
    @Override
    public void upgradeLevel(User user){
        user.upgradeLevel();
        userDao.update(user);
    }
}
