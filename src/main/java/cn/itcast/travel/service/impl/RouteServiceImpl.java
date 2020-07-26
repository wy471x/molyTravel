package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;
import org.apache.log4j.Logger;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    Logger log = Logger.getLogger(RouteServiceImpl.class);
    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    @Override
    public PageBean<Route> pageQueryByRouteName(int cid, int currentPage, int pageSize, String rname) {
        PageBean<Route> pb = new PageBean<Route>();
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);

        int totalCount = routeDao.findTotalCountRouteName(cid, rname);
        pb.setTotalCount(totalCount);

        int start = (currentPage - 1) * pageSize;

        List<Route> list = routeDao.findByPageRouteName(cid, start, pageSize, rname);
        pb.setList(list);

        int totalPage = totalCount % pageSize == 0 ? totalCount/pageSize :
                (totalCount/pageSize) + 1;
        log.info("totalPage=" + totalPage);
        log.info("totalCount=" + totalCount);
        pb.setTotalPage(totalPage);
        log.info(pb.getTotalCount());
        log.info(pb.getTotalPage());
        return pb;
    }

    @Override
    public PageBean<Route> pageQueryAllRoute(int cid, int currentPage, int pageSize) {
        PageBean<Route> pb = new PageBean<Route>();
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);

        int totalCount = routeDao.findTotalCountAllRoute(cid);
        pb.setTotalCount(totalCount);

        int start = (currentPage - 1) * pageSize;

        List<Route> list = routeDao.findByPageAllRoute(cid, start, pageSize);
        pb.setList(list);

        int totalPage = totalCount % pageSize == 0 ? totalCount/pageSize :
                (totalCount/pageSize) + 1;
        log.info("totalPage=" + totalPage);
        log.info("totalCount=" + totalCount);
        pb.setTotalPage(totalPage);
        log.info(pb.getTotalCount());
        log.info(pb.getTotalPage());
        return pb;
    }

    @Override
    public Route findOne(String rid) {
        Route route = routeDao.findOne(Integer.parseInt(rid));

        List<RouteImg> routeImgList = routeImgDao.findByRid(route.getRid());

        route.setRouteImgList(routeImgList);

        Seller seller = sellerDao.findById(route.getSid());
        route.setSeller(seller);

        int count = favoriteDao.findCountByRid(route.getRid());
        route.setCount(count);

        return route;
    }

}
