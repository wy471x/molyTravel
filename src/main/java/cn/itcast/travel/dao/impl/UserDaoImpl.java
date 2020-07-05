package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.UnsupportedEncodingException;

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
        String sql = "INSERT INTO tab_user(username, password, name, birthday, sex, telephone, email, status, code)" +
                "VALUES(?,?,?,?,?,?,?,?,?)";

        template.update(sql, user.getUsername(), user.getPassword(),
                        user.getName(), user.getBirthday(), user.getSex(),
                user.getTelephone(), user.getEmail(), user.getStatus(),
                user.getCode());
    }

    /**
     * querying user based on active code
     * @param code
     * @return
     */
    @Override
    public User findByCode(String code) {
        User user = null;
        try {
            String sql = "SELECT * FROM tab_user WHERE code = ?";
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * modifying status of user specified
     * @param user
     */
    @Override
    public void updateStatus(User user) {
        String sql = "UPDATE tab_user SET status = 'Y' WHERE uid = ?";
        template.update(sql, user.getUid());
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        User user = null;
        try {
            String sql = "SELECT * FROM tab_user WHERE username = ? and password = ?";
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username, password);
        } catch (Exception e) {

        }

        return user;
    }
}
