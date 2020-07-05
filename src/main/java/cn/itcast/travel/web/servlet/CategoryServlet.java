package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/category/*")
public class CategoryServlet extends BaseServlet {
    private static Logger log = LogManager.getLogger(CategoryServlet.class.getName());

    private CategoryService service = new CategoryServiceImpl();

    public void findAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. calling service to query all
        List<Category> cs = service.findAll();
        // 2. serialized as json to return
        writeValue(cs, response);
    }

}
