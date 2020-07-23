package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {
    int findTotalCountRouteName(int cid, String rname);
    int findTotalCountAllRoute(int cid);

    List<Route> findByPageRouteName(int cid, int start, int pageSize, String rname);
    List<Route> findByPageAllRoute(int cid, int start, int pageSize);
}
