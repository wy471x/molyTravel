package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/registUserServlet")
public class RegistUserServlet extends HttpServlet {
    private static Logger log = LogManager.getLogger(RegistUserServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // check code
        String check = request.getParameter("check");
        log.info(check);
        // getting checkcode from session
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        log.info(checkcode_server);

        // make comparsion
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)) {
            // checkcode error
            ResultInfo info = new ResultInfo();

            // register fail
            info.setFlag(false);
            info.setErrorMsg("验证码错误！");

            //serializing info object as json
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return ;
        }

        // getting data
        Map<String, String[]> map = request.getParameterMap();

        // packaging object
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // calling regist function to finish user register
        UserService service = new UserServiceImpl();
        boolean flag = service.regist(user);
        ResultInfo info = new ResultInfo();

        if (flag) {
            info.setFlag(true);
            //info.setErrorMsg("Regist Success!");
        } else {
            info.setFlag(false);
            info.setErrorMsg("注册失败！");
        }

        // info object serialized as json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);

        // writting json back to client
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
