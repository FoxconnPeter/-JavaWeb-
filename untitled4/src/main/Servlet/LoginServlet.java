package main.Servlet;

import main.doMain.User;
import main.utils.DataSourceUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet(name = "LoginServlet", urlPatterns = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置查询码表
        request.setCharacterEncoding("UTF-8");

        //1、获得用户名和密码
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //2、调用一个业务方法进行该用户查询
        User login = null;
        try {
            login = login(username,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //3、通过user是否为null判断用户名和密码是否正确
        if(login!=null){
            //用户名和密码正确
            //登录成功 跳转到网站的首页
            response.sendRedirect(request.getContextPath());
        }else{
            //用户名或密码错误
            //跳回当前login.jsp
            //使用转发 转发到login.jsp  向request域中存储错误信息
            request.setAttribute("loginInfo", "用户名或密码错误");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }


    }

    public User login(String username,String password) throws SQLException{
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from user where username=? and password=?";
        User user = runner.query(sql, new BeanHandler<User>(User.class), username,password);
        return user;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
