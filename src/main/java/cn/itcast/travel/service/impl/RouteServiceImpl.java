package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.RouteService;
import org.apache.log4j.Logger;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    Logger log = Logger.getLogger(RouteServiceImpl.class);
    private RouteDao routeDao = new RouteDaoImpl();

    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize) {
        PageBean<Route> pb = new PageBean<Route>();
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);

        int totalCount = routeDao.findTotalCount(cid);
        pb.setTotalCount(totalCount);

        int start = (currentPage - 1) * pageSize;

        List<Route> list = routeDao.findByPage(cid, start, pageSize);
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

}
