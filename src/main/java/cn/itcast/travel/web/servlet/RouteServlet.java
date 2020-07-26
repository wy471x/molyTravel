package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    Logger log = Logger.getLogger(RouteServlet.class);
    private RouteService routeService = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * pageing query
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQueryByRouteName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        log.info("pageQueryByRouteName executing...");
        // 1. receive arguments
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");

        // receive rname
        String rname = request.getParameter("rname");
        log.info(rname);
        rname = new String(rname.getBytes("iso-8859-1"), "utf-8");


        // 2. handling arguments
        int cid = 0;
        if (cidStr != null && cidStr.length() > 0) {
            cid = Integer.parseInt(cidStr);
        }
        int currentPage = 0;
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
           currentPage = 1;
        }

        int pageSize = 0;

        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            pageSize = 5;
        }

        PageBean<Route> pb = routeService.pageQueryByRouteName(cid, currentPage, pageSize, rname);

        writeValue(pb, response);
    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQueryAllRoute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        log.info("pageQueryAllRoute executing...");
        // 1. receive arguments
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");

        // 2. handling arguments
        int cid = 0;
        if (cidStr != null && cidStr.length() > 0) {
            cid = Integer.parseInt(cidStr);
        }
        int currentPage = 0;
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            currentPage = 1;
        }

        int pageSize = 0;

        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            pageSize = 5;
        }

        PageBean<Route> pb = routeService.pageQueryAllRoute(cid, currentPage, pageSize);

        writeValue(pb, response);
    }

    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        // receive route id

        String rid = request.getParameter("rid");
        log.info(rid);
        Route route = routeService.findOne(rid);
        writeValue(route, response);
    }

    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        log.info(rid);

        User user = (User) request.getSession().getAttribute("user");
        log.info(user.getName());
        int uid;
        if (user == null) {
            uid = 0;
        } else {
            uid = user.getUid();
        }
        boolean flag = favoriteService.isFavorite(rid, uid);
        writeValue(flag, response);
    }

    public void addFavorite(HttpServletRequest request, HttpServletResponse response) {
        String rid = request.getParameter("rid");
        User user = (User) request.getSession().getAttribute("user");

        int uid;
        if (user == null) {
            return ;
        } else {
            uid = user.getUid();
        }

        favoriteService.add(rid, uid);
    }

}