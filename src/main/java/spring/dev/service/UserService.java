package spring.dev.service;

import org.h2.engine.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import spring.dev.dao.UserDao;
import spring.dev.domain.Level;
import spring.dev.domain.User;


import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.security.UnrecoverableEntryException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;


import static org.h2.engine.Session.*;

public class UserService {
    UserDao userDao;
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    UserLevelUpgradePolicy userLevelUpgradePolicy;

    private PlatformTransactionManager transactionManager;

    public void setTransactionManager(PlatformTransactionManager transactionManager){
        this.transactionManager = transactionManager;
    }



    public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMEND_FOR_GOLD = 30;

    protected void upgradeLevels() throws SQLException{

        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        //Connection c = DataSourceUtils.getConnection(dataSource);
        //c.setAutoCommit(false);
        try{
            List<User> users = userDao.getAll();
            for(User user : users){
//            if(userLevelUpgradePolicy.canUpgradeLevel(user)){
//                userLevelUpgradePolicy.upgradeLevel(user);
//            }
                if(canUpgradeLevel(user)){
                    upgradeLevel(user);
                }
            }
            this.transactionManager.commit(status);
        } catch (Exception e){
            this.transactionManager.rollback(status);
            throw e;
        }

    }

    private boolean canUpgradeLevel(User user){
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

    protected void upgradeLevel(User user){
        user.upgradeLevel();
        userDao.update(user);
        sendUpgradeEMail(user);
    }


    public void add(User user){
        if(user.getLevel() == null)user.setLevel(Level.BASIC);
        userDao.add(user);
    }


    private void sendUpgradeEMail(User user) {

    }
}
