package cn.itcast.travel.web.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {
    private static Logger log = LogManager.getLogger(BaseServlet.class.getName());
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // getting request path
        String uri = req.getRequestURI();
        log.info("uri= " + uri);

        // getting method
        String methodName = uri.substring(uri.lastIndexOf('/') + 1);
        log.info("method= " + methodName);

        //getting object of method
        //System.out.println(this);
        log.info(this);
        try {
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class,
                    HttpServletResponse.class);
            method.invoke(this, req, resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * object serialized as json, then this json write back to client
     * @param obj
     * @param response
     * @throws IOException
     */
    public void writeValue(Object obj, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(), obj);
    }

    public  String writeValueAsString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
}
