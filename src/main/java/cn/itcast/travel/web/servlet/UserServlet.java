package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    private static Logger log = LogManager.getLogger(UserServlet.class.getName());
    //declaring object of UserService
    private UserService service = new UserServiceImpl();

    /**
     * user register
     * @param request
     * @param response
     * @throws IOException
     */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws IOException {
       // check code
       String check = request.getParameter("check");
       log.info(check);
       // getting checkcode from session
       HttpSession session = request.getSession();
       String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
       session.removeAttribute("CHECKCODE_SERVER");
       log.info(checkcode_server);
       // make comparsion
       if (checkcode_server == null ||
               !checkcode_server.equalsIgnoreCase(check)) {
           ResultInfo info = new ResultInfo();

           info.setFlag(false);
           info.setErrorMsg("验证码错误！");
           ObjectMapper mapper = new ObjectMapper();
           String json = mapper.writeValueAsString(info);
           response.setContentType("application/json;charset=utf-8");
           response.getWriter().write(json);
           return;
       }

       // 1. getting data
       Map<String, String[]> map = request.getParameterMap();
       // 2. packaging object
       User user = new User();
       try {
           BeanUtils.populate(user, map);
       } catch (IllegalAccessException e) {
           e.printStackTrace();
       } catch (InvocationTargetException e) {
           e.printStackTrace();
       }

       // 3. calling service to finish register
       boolean flag = service.regist(user);
       ResultInfo info = new ResultInfo();
       // 4. response result
       if (flag) {
           info.setFlag(true);
       } else {
           info.setFlag(false);
           info.setErrorMsg("注册失败！");
       }

       // info serialized as json
       ObjectMapper mapper = new ObjectMapper();
       String json = mapper.writeValueAsString(info);

       // writing json to client and configuring content-type
       response.setContentType("application/json;charset=utf-8");
       response.getWriter().write(json);
    }

    /**
     * user login
     * @param request
     * @param response
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. getting username and password
        Map<String, String[]> map = request.getParameterMap();
        // packaging user object
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // 3. calling service to query user
        User u = service.login(user);
        ResultInfo info = new ResultInfo();

        // 4.judging whether user is null
        if (u == null) {
            // username or password is wrong
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误！");
        }

        // getting checkcode
        String check = request.getParameter("check");
        log.info(check);
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        log.info(checkcode_server);
        session.removeAttribute("CHECKCODE_SERVER");
        // make comparsion
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)) {
            // register fail
            info.setFlag(false);
            info.setErrorMsg("验证码错误！");
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
        }

        // 5.juding whether user is active
        if (u != null && !"Y".equals(u.getStatus())) {
            // user isn't active
            info.setFlag(false);
            info.setErrorMsg("您尚未激活，请先进行激活！");
        }

        // 6.judging  whether user is login
        if (u != null && "Y".equals(u.getStatus())) {
            request.getSession().setAttribute("user", u);
            info.setFlag(true);
        }

        // responsing data
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(), info);
    }

    /**
     * finding only one user information
     * @param request
     * @param response
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // getting login user from session
        Object user = request.getSession().getAttribute("user");

        // writting user information back to client
        ObjectMapper mapper = new ObjectMapper();

        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(), user);
    }

    /**
     * user exit
     * @param request
     * @param response
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. destroying session
        request.getSession().invalidate();

        // 2. jumping to login page
        log.info(request.getContextPath());
        response.sendRedirect(request.getContextPath() + "/login.html");
    }

    /**
     * active user
     * @param request
     * @param response
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. getting active code
        String code = request.getParameter("code");
        if (code != null) {
            // 2. calling service to finish active
            boolean flag = service.active(code);

            // 3.judging flag
            String msg = null;
            if (flag) {
                // active successful
                log.info(request.getContextPath());
                msg = "激活成功，请<a href='" + request.getContextPath() +"/login.html'>登录</a>";
            } else {
                // active failure
                msg = "激活失败，请联系管理员！";
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }
}
