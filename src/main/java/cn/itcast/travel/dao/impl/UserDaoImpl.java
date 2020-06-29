package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * querying user information specified username
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        User user = null;
        try {
            String sql = "SELECT * FROM tab_user WHERE username = ?";

            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
        } catch (Exception e) {
            //e.printStackTrace();
        }

        return user;
    }

    /**
     * saving user information
     * @param user
     */
    @Override
    public void save(User user) {
        String sql = "INSERT INTO tab_user(username, password, name, birthday, sex, telephone, email)" +
                "VALUES(?,?,?,?,?,?,?)";

        template.update(sql, user.getUsername(), user.getPassword(),
                        user.getName(), user.getBirthday(), user.getSex(),
                user.getTelephone(), user.getEmail());
    }

}
