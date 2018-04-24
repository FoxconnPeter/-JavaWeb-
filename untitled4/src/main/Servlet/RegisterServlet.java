package main.Servlet;

import main.doMain.User;
import main.utils.DataSourceUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

@WebServlet(name = "RegisterServlet", urlPatterns = "/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置查询码表
        request.setCharacterEncoding("UTF-8");
        Map<String, String[]> properties = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user, properties);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        //现在这个位置 user对象已经封装好了
        //手动封装uid----uuid---随机不重复的字符串32位--java代码生成后是36位
        user.setUid(UUID.randomUUID().toString());

        //3、将参数传递给一个业务操作方法
        try {
            regist(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //4、认为注册成功跳转到登录页面
        response.sendRedirect(request.getContextPath()+"/login.jsp");


    }

    //注册的方法
    public void regist(User user) throws SQLException {
        //操作数据库
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "insert into user values(?,?,?,?,?,?,?,?,?,?)";

        runner.update(sql,user.getUid(),user.getUsername(),user.getPassword(),user.getName(),
                user.getEmail(),null,user.getBirthday(),user.getSex(),null,null);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }



}
