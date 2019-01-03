package spring.dev.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.*;
import spring.dev.domain.Level;
import spring.dev.domain.User;
import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import spring.dev.dao.UserDao;

public class UserDaoJdbc implements UserDao {

    private DataSource dataSource;
    //private JdbcContext jdbcContext ;
    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> userMapper = new RowMapper<User>() {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setLogin(rs.getInt("login"));
            user.setLevel(Level.valueOf(rs.getInt("level")));
            user.setRecommend(rs.getInt("recommend"));

            return user;
        }
    };

    public UserDaoJdbc() {
    }
/*
    public UserDao(DataSource dataSource) {
        this.dataSource = dataSo    urce;
    }
*/
    public void setDataSource(DataSource dataSource) {
        /*
        this.jdbcContext = new JdbcContext();
        this.jdbcContext.setDataSource(dataSource);
        this.dataSource = dataSource;
        */
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }

    public void add(final User user){//client
        //StatementStrategy st = new AddStatement(user);
        /*
        this.jdbcContext.workWithStatementStrategy( //template
                new StatementStrategy() {//callback
                    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                        PreparedStatemenxt ps = c.prepareStatement("insert into users(id, name, password) values (?,?,?)");
                        ps.setString(1, user.getId());
                        ps.setString(2, user.getName());
                        ps.setString(3, user.getPassword());

                        return ps;
                    }
                }
        );*/
        this.jdbcTemplate.update("insert into users(id, name, password,login,level,recommend) values(?,?,?,?,?,?)",
                user.getId(),user.getName(),user.getPassword(),user.getLogin(), user.getLevel().intValue(), user.getRecommend());

    }

    public void deleteAll(){
        //StatementStrategy st = new DeleteAllStatement();
        this.jdbcTemplate.update("delete from users");
    }

    public User get(String id){
        return this.jdbcTemplate.queryForObject("select * from users where id= ?",
                new Object[] {id},this.userMapper);
        /*
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            c = this.dataSource.getConnection();
            ps = c.prepareStatement("select * from users where id = ?");
            ps.setString(1, id);
            rs = ps.executeQuery();
            User user = null;

            if(rs.next()){
                user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
            }

            if(null == user) throw new EmptyResultDataAccessException("결과값이 없습니다", 1);

            return user;

        }catch(SQLException e){
            throw e;
        }finally{
            if( rs != null){
                try{
                    rs.close();
                }catch(SQLException e){

                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {

                }
            }

            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {

                }
            }
        }
        */
    }

    public int getCount() {
        return this.jdbcTemplate.query(new PreparedStatementCreator(){
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException{
                return con.prepareStatement("select count(*) from users");
            }
        }, new ResultSetExtractor<Integer>() {
            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException{
                rs.next();
                return rs.getInt(1);
            }
        });
/*


        Connection c= null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c = this.dataSource.getConnection();
            ps = c.prepareStatement("select count(*) from users");
            rs = ps.executeQuery();
            rs.next();
            return  rs.getInt(1);
        }catch (SQLException e){
            throw e;
        }finally {

            if( rs != null){
                try{
                    rs.close();
                }catch(SQLException e){

                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {

                }
            }

            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {

                }
            }
        }
        */
    }

    @Override
    public void update(User user) {
        this.jdbcTemplate.update(
                "update users set name = ?, password= ?, level = ?, login = ?, recommend = ? where id = ? "
                ,user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getId());

    }

    public List<User> getAll() {
        return this.jdbcTemplate.query("select * from users order by id",this.userMapper);
    }

    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = dataSource.getConnection();
            ps = stmt.makePreparedStatement(c);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {

                }
            }

            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {

                }
            }
        }
    }
}
