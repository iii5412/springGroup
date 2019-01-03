package spring.dev.service;

import org.springframework.beans.factory.annotation.Autowired;
import spring.dev.dao.UserDao;
import spring.dev.domain.User;

public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);
    void upgradeLevel(User user);
}
