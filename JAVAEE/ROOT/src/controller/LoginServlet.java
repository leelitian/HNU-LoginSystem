package controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import service.UsersService;
import util.DataSourceUtils;
import util.JdbcUtils;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 第一：获取请求的参数
		response.setContentType("application/json; charset=utf-8"); // 设置代码返回的编码格式

		String user = request.getParameter("user");
		String pwd = request.getParameter("pwd");
		String client = request.getParameter("client");
		System.out.println("usename: " + user + "   " + "password: " + pwd);

		// 从session中获取connection（如果没有，就设置一个）
		JdbcUtils jdbcUtils = null;
		HttpSession session = request.getSession();
		DataSourceUtils dsu = new DataSourceUtils();
		if (session.getAttribute("utils") == null) {
			try {
				System.out.println("建立连接");
				jdbcUtils = new JdbcUtils(dsu.getCon());
			} catch (Exception e) {
				e.printStackTrace();
			}
			session.setAttribute("utils", jdbcUtils);
		} else {
			jdbcUtils = (JdbcUtils) session.getAttribute("utils");
		}

		UsersService uservice = new UsersService(jdbcUtils);

		// 第二步：调用业务层：注册的业务即可
		boolean flag = false;
		try {
			flag = uservice.login(user, pwd);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		JSONObject jsonObject = new JSONObject(); // 创建Json对象
		if (flag) {
			System.out.println("登陆成功");

			if (client != null && client.equals("Android")) {
				jsonObject.put("state", true);
				System.out.println(jsonObject.toString()); // 调用toString方法将json对象转换成json字符串
				response.getWriter().write(jsonObject.toString());
			}
			// 第三步：如果登陆成功，直接讲所有的用户进行分页显示
			else {
				request.setAttribute("username", user);
				request.getRequestDispatcher("ListServlet").forward(request, response);
			}
		} else {
			System.out.println("用户名或密码错误");

			if (client != null && client.equals("Android")) {
				jsonObject.put("state", false);
				// 调用toString方法将json对象转换成json字符串
				System.out.println(jsonObject.toString());
				response.getWriter().write(jsonObject.toString());
			} else {
				String msg = "用户名或密码错误";
				request.setAttribute("msg", msg);
				request.getRequestDispatcher("login.jsp").forward(request, response);
			}
		}
	}
}