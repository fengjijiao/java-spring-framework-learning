package us.fjj.spring.learning.transactionusage.test3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class UserService3 {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int insertBatch(User[] users) {
        int result = 0;
        jdbcTemplate.update("truncate table user");
        for (User user : users) {
            result += jdbcTemplate.update("insert into user (username, password) value (?, ?)", user.getUsername(), user.getPassword());
        }
        return result;
    }

    public List<User> findAll() {
        return jdbcTemplate.query("select username, password from user;", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setUsername(rs.getString(1));
                user.setPassword(rs.getString(2));
                return user;
            }
        });
    }
}
