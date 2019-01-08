package spring.dev.service;

import org.springframework.beans.factory.annotation.Autowired;
import spring.dev.dao.UserDao;
import spring.dev.domain.User;

public interface UserLevelUpgradePolicy {
    void setUserDao(UserDao userDao);
    boolean canUpgradeLevel(User user);
    void upgradeLevel(User user);
}
