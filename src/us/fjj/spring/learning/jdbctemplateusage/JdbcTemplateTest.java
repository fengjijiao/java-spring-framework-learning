package us.fjj.spring.learning.jdbctemplateusage;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import us.fjj.spring.learning.jdbctemplateusage.test1.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * JdbcTemplate实现增删改查
 * 事务中大部分案例会用到JdbcTemplate相关的功能
 * <p>
 * <p>
 * 什么是JdbcTemplate?
 * java中操作db最原始的方式就是纯jdbc了，是不是每次操作db都需要加载数据库驱动、获取连接、获取PreparedStatement、执行sql、关闭PreparedStatement、关闭连接等等，操作还是比较繁琐的，spring中提供了一个模块，对象jdbc操作进行了封装，使其更简单，就是本文中的JdbcTemplate，JdbcTemplate是Spring对JDBC的封装，目的是使JDBC更加易于使用。
 * <p>
 * JdbcTemplate的使用步骤
 * 1.创建数据源DataSource
 * 2.创建JdbcTemplate，new JdbcTemplate(dataSource)
 * 3.调用JdbcTemplate的方法操作db，如增删改查
 */
public class JdbcTemplateTest {
    public static DataSource getDataSource() {
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/test1?characterEncoding=UTF-8");
        dataSource.setUsername("333");
        dataSource.setPassword("***");
        dataSource.setInitialSize(5);
        return dataSource;
    }

    @Test
    public void test0() {
        //1.创建数据源DataSource
        DataSource dataSource = getDataSource();
        //2.创建JdbcTemplate，new JdbcTemplate(dataSource)
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        //3.调用JdbcTemplate的方法操作db，如增删改查
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from user;");
        System.out.println(maps);
        /**
         * [{id=1, username=a, password=aa}, {id=2, username=b, password=bb}]
         */
        /**
         * 上面查询返回了user表所有的记录，返回了一个集合，集合中是一个Map，Map表示一行记录，key为列名，value为列对应的值
         * 有没有感觉到特别的方便，只需要jdbcTemplate.queryForList("select * from user")这么简单的一行代码，数据就被获取到了。
         *
         * 下面我们来探索更强大更好用的功能。
         */
    }

    /**
     * 增加、删除、修改操作
     * JdbcTemplate中以update开头的方法，用来执行增、删、改操作。
     * <p>
     * 无参情况
     * Api : int update(final String sql)
     * <p>
     * 案例
     */
    @Test
    public void test1() {
        DataSource dataSource = getDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        int updateRows = jdbcTemplate.update("insert into user (username, password) value ('jyx', 'jyx123')");
        System.out.println("影响行数：" + updateRows);
        /**
         * 影响行数：1
         */
    }

    /**
     * 有参情况1
     * <p>
     * Api : int update(String, object... args)
     * <p>
     * 案例
     * sql中使用?作为占位符
     */
    @Test
    public void test2() {
        DataSource dataSource = getDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        int updateRows = jdbcTemplate.update("insert into user (username, password) value (?, ?)", "ssw", "ssw123");
        System.out.println("影响行数：" + updateRows);
    }

    /**
     * 有参情况2
     * Api : int update(String sql, PreparedStatementSetter pss)
     * 通过PreparedStatementSetter来设置参数，是个函数式接口，内部有个setValues方法会传递一个PreparedStatement参数，我们可以通过这个参数手动的设置参数的值。
     * <p>
     * 案例
     */
    @Test
    public void test3() {
        DataSource dataSource = getDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        int updateRows = jdbcTemplate.update("insert into user (username, password) value (?, ?)",
                ps -> {
                    ps.setString(1, "yk");
                    ps.setString(2, "yk123");
                });
        System.out.println("影响行数：" + updateRows);
        /**
         * 影响行数：1
         */
    }


    /**
     * 获取自增列的值
     * <p>
     * Api : public int update(final PreparedStatementCreator psc, final KeyHolder generatedKeyHolder)
     * <p>
     * 案例
     */
    @Test
    public void test4() {
        DataSource dataSource = getDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "insert into user (username, password) value (?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowCount = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                //手动创建PreparedStatement，注意第二个参数；Statement.RETURN_GENERATED_KEYS
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, "获取自增列的值");
                ps.setString(2, "获取自增列的值123");
                return ps;
            }
        }, keyHolder);
        System.out.println("影响行：" + rowCount + "新纪录Id: " + keyHolder.getKey().intValue());
        /**
         * 影响行：1新纪录Id: 5
         */
    }


    /**
     * 批量增删改操作
     *
     * Api
     * int[] batchUpdate(final String[] sql);
     * int[] batchUpdate(String sql, List<Object[]> batchArgs);
     * int[] batchUpdate(String sql, List<Object[]> batchArgs, int[] argType);
     *
     * 案例
     */
    @Test
    public void test5() {
        DataSource dataSource = getDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Object[]> list = Arrays.asList(new Object[]{"c", "c123"}, new Object[]{"d", "d123"}, new Object[]{"e", "e123"}, new Object[]{"f", "f123"});
        int[] updateRows = jdbcTemplate.batchUpdate("INSERT INTO user (username, password) VALUE (?, ?)", list);
        for (int updateRow : updateRows) {
            System.out.println(updateRow);
        }
        /**
         * 1
         * 1
         * 1
         * 1
         */
    }


    /**
     * 查询操作
     *
     * 查询一列单行
     *
     * sql: 执行的sql，如果有参数，参数占位符?
     * requiredType: 返回的一列数据对应的java类型，如String
     * args: ？占位符对应的参数列表
     * Api: <T> T queryForObject(String sql, Class<T> requiredType, @Nullable Object... args)
     *
     * 案例
     *
     */
    @Test
    public void test6() {
        DataSource dataSource = getDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String name = jdbcTemplate.queryForObject("select username from user where id = ?", String.class, 1 );
        System.out.println(name);
        /**
         *
         */
        /**
         * 使用注意：
         * 若queryForObject中sql查询无结果时，会报错
         * 如id为0记录不存在
         * 此时，会抛出一个异常EmptyResultDataAccessException，期望返回一条记录，但实际上却没有找到记录，和期望结果不符，所以报错了。
         * 这种情况如何解决呢，需要用到查询多行的方式来解决了，即下面要说到的queryForList相关的方法，无结果的时候会返回一个空List，我们可以在这个空的List上做文章、
         */

    }

    /**
     * 查询一列多行
     *
     * Api
     *
     * 以queryForList开头的方法。
     * <T> List<T> queryForList(String sql, Class<T> elementType);
     * <T> List<T> queryForList(String sql, Class<T> elementType, @Nullable Object... args);
     * <T> List<T> queryForList(String sql, Object[] args, Class<T> elementType);
     * <T> List<T> queryForList(String sql, Object[] args, int[] argTypes, Class<T> elementType);
     * 注意：
     * 上面的这个T虽然是泛型，但是只支持Integer.class String.class这种单数据类型的，自已定义的Bean不支持。（所以用来查询单列数据）
     * elementType: 查询结果需要转换为哪种类型？如String、Integer、Double。
     *
     * 案例
     */
    @Test
    public void test7() {
        DataSource dataSource = getDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        //<T> List<T> queryForList(String sql, Class<T> elementType);
        List<String> list1 = jdbcTemplate.queryForList("select username from user where id > 1", String.class);
        list1.forEach(System.out::println);
        //<T> List<T> queryForList(String sql, Class<T> elementType, @Nullable Object... args);
        List<String> list2 = jdbcTemplate.queryForList("select  username from user where id > ?", String.class, 2);
        list2.forEach(System.out::println);
        //<T> List<T> queryForList(String sql, Object[] args, Class<T> elementType);
        List<String> list3 = jdbcTemplate.queryForList("select username from user where id > ?", new Object[]{3}, String.class);
        list3.forEach(System.out::println);
        //<T> List<T> queryForList(String sql, Object[] args, int[] argTypes, Class<T> elementType);
        List<String> list4 = jdbcTemplate.queryForList("select username from user where id > ?", new Object[]{2}, new int[]{Types.INTEGER}, String.class);
        list4.forEach(System.out::println);
        /**
         * b
         * jyx
         * yk
         * 获取自增列的值
         * c
         * d
         * e
         * f
         * jyx
         * yk
         * 获取自增列的值
         * c
         * d
         * e
         * f
         * yk
         * 获取自增列的值
         * c
         * d
         * e
         * f
         * jyx
         * yk
         * 获取自增列的值
         * c
         * d
         * e
         * f
         */
    }

    /**
     * 查单行记录，将记录转换成一个对象
     *
     * Api
     * <T> T queryForObject(String sql, RowMapper<T> rowMapper);
     * <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper);
     * <T> T queryForObject(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper);
     * <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args);
     *
     * 上面这些方法的参数中都有一个rowMapper参数，行映射器，可以将当前行的结果映射为一个自定义的对象。
     * {@link org.springframework.jdbc.core.RowMapper}
     * JdbcTemplate内部会遍历ResultSet，然后循环调用RowMapper#mapRow，得到当前行的结果，将其丢到List中返回，如下：
     * List<T> results = new ArrayList<>();
     * int rowNum = 0;
     * while(rs.next()) {
     *     results.add(this.rowMapper.mapRow(rs, rowNum++));
     * }
     * return results;
     *
     * 案例
     */
    @Test
    public void test8() {
        DataSource dataSource = getDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "select id, username, password from user where id = ?";
        //查询id为3的用户信息
        User user = jdbcTemplate.queryForObject(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getInt(1));
                user.setUsername(rs.getString(1));
                user.setPassword(rs.getString(2));
                return user;
            }
        }, 3);
        System.out.println(user);
        /**
         * User(id=3, username=3, password=jyx)
         */
        /**
         * 使用注意
         * 当queryForObject中sql查询无结果的时候，会报错，必须要返回一行记录。
         */
    }

    /**
     * 查询单行记录，返回指定的javabean
     * RowMapper有个实现类BeanPropertyRowMapper，可以将结果映射为javabean。
     *
     */
    @Test
    public void test9() {
        DataSource dataSource = getDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "select id, username, password from user where id = ?";
        RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
        User user = jdbcTemplate.queryForObject(sql, rowMapper, 3);
        System.out.println( user);
        /**
         * User(id=3, username=jyx, password=jyx123)
         */
    }

    /**
     * 查询多列多行，每行结果为一个Map
     *
     * Api
     * List<Map<String, Object>> queryForList(String sql);
     * List<Map<String, Object>> queryForList(String sql, Object... args);
     * 每行结果为一个Map,key为列名小写，value为对应列值。
     *
     * 案例
     */
    @Test
    public  void test10() {
        DataSource dataSource = getDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql ="select id,username, password from user where id >?";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, 3);
        System.out.println(maps);
        /**
         * [{id=4, username=yk, password=yk123}, {id=5, username=获取自增列的值, password=获取自增列的值123}, {id=6, username=c, password=c123}, {id=7, username=d, password=d123}, {id=8, username=e, password=e123}, {id=9, username=f, password=f123}]
         */
    }


    /**
     * 查询多行多列，将结果映射为javabean
     *
     * Api
     * <T> List<T> query(Strig sql, RowMapper<T> rowMapper, @Nullable Object... args);
     *
     * 案例
     */
    @Test
    public void test11() {
        DataSource dataSource = getDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "select id,username, password from user where id > ?";
        List<User> maps = jdbcTemplate.query(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getInt(1));
                user.setUsername(rs.getString(1));
                user.setPassword(rs.getString(2));
                return user;
            }
        }, 3);
        System.out.println(maps);
        /**
         * [User(id=4, username=4, password=yk), User(id=5, username=5, password=获取自增列的值), User(id=6, username=6, password=c), User(id=7, username=7, password=d), User(id=8, username=8, password=e), User(id=9, username=9, password=f)]
         */
    }


    /**
     * 更简单的方式，使用BeanPropertyRowMapper
     */
    @Test
    public void test12() {
        DataSource dataSource = getDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "select id,username, password from user where id > ?";
        List<User> maps = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), 3);
        System.out.println(maps);
        /**
         * [User(id=4, username=4, password=yk), User(id=5, username=5, password=获取自增列的值), User(id=6, username=6, password=c), User(id=7, username=7, password=d), User(id=8, username=8, password=e), User(id=9, username=9, password=f)]
         */
    }


    /**
     * 总结
     * 1.使用注意：JdbcTemplate中的getObject开头的方法，要求sql必须返回一条记录，否则会报错
     * 2.BeanPropertyRowMapper可以将行记录映射为javabean
     * 3.JdbcTemplate采用模板的方式操作jdbc变得特别容易，代码特别简洁，不过其内部没有动态sql的功能，即通过参数，动态生成指定的sql，mybatis在动态sql方面做的比较好。
     */



}
