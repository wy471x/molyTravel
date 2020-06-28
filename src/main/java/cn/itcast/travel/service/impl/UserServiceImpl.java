package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    /**
     * user register
     * @param user
     * @return
     */
    @Override
    public boolean regist(User user) {
        User u = userDao.findByUsername(user.getUsername());

        if (u != null) {
            return false;
        }

        userDao.save(user);
        return true;
    }

}
