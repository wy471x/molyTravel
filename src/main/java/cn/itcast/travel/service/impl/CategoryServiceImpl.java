package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService{
    private static Logger log = LogManager.getLogger(CategoryServiceImpl.class.getName());
    private CategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public List<Category> findAll() {
        // 1. querying from redis
        Jedis jedis = JedisUtil.getJedis();
        Set<Tuple> categorys = jedis.zrangeWithScores("category", 0, -1);
        List<Category> cs = null;

        // 2. judging whether query set is null
        if (categorys == null || categorys.size() == 0) {
            log.info("从数据库查询....");
            // 3. if result of redis is null, querying from database. Then storing data to redis.
            cs = categoryDao.findAll();

            for (int i = 0; i < cs.size(); ++i) {
                jedis.zadd("category", cs.get(i).getCid(), cs.get(i).getCname());
            }
        } else {
            log.info("从redis中查询....");
            cs = new ArrayList<Category>();
            /*for (String name : categorys) {
                Category category = new Category();
                category.setCname(name);
                cs.add(category);
            }*/
            for (Tuple tuple : categorys) {
                Category category = new Category();
                category.setCname(tuple.getElement());
                log.info(tuple.getElement());
                category.setCid((int)tuple.getScore());
                log.info(tuple.getScore());
                cs.add(category);
            }
        }
        return cs;
    }
}
