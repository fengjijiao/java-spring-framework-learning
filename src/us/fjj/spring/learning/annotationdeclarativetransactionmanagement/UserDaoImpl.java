package us.fjj.spring.learning.annotationdeclarativetransactionmanagement;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
public class UserDaoImpl implements UserDao {
    private JdbcTemplate jdbcTemplate;
    private UserDao userDao;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void createUserTable() {
        this.jdbcTemplate.execute("CREATE TABLE `user4` (`id` INTEGER PRIMARY KEY, `name` VARCHAR(50) DEFAULT NULL, `age` INTEGER DEFAULT NULL)");
    }

    @Override
    public void saveUser(User user) {
        try {
            this.jdbcTemplate.update("INSERT INTO user4(name, age) VALUES (?,?)", user.getName(), user.getAge());
        }catch (Exception e) {
            System.out.println("error in creating record, rolling back");
            throw e;
        }
    }

    @Override
    public List<User> listUser() {
        List<User> users = this.jdbcTemplate.query("SELECT name, age FROM user4", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setName(rs.getString("name"));
                user.setAge(rs.getInt("age"));
                return user;
            }
        });
        return users;
    }
}
