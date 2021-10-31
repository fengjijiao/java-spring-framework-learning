package us.fjj.spring.learning.jdbctemplatedemo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private JdbcTemplate jdbcTemplate;
    private UserDao userDao;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void createUserTable() {
        this.jdbcTemplate.execute("CREATE TABLE `user` (`id` intEGER PRIMARY KEY, `name` varchar(50) DEFAULT NULL, `age` intEGER DEFAULT NULL);");
    }

    @Override
    public void saveUser(User user) {
        this.jdbcTemplate.update("INSERT INTO user(name, age) VALUES (?,?)",user.getName(), user.getAge());
    }

    @Override
    public List<User> listUser() {
        List<User> users = this.jdbcTemplate.query("SELECT name, age FROM user", new RowMapper<User>() {
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
