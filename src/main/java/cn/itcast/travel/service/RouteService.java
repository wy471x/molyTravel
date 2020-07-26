package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

public interface RouteService {

    PageBean<Route> pageQueryByRouteName(int cid, int currentPage, int pageSize, String rname);
    PageBean<Route> pageQueryAllRoute(int cid, int currentPage, int pageSize);

    Route findOne(String rid);


}
