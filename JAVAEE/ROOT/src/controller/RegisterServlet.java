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

import service.PersonService;
import service.UsersService;
import util.DataSourceUtils;
import util.JdbcUtils;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 处理登录的业务
		response.setContentType("application/json; charset=utf-8"); // 设置代码返回的编码格式
		// 第一获取用户输入的用户名和密码
		String user = request.getParameter("username");
		String name = request.getParameter("name");
		String age = request.getParameter("age");
		String teleno = request.getParameter("teleno");
		String pwd = request.getParameter("password");
		String client = request.getParameter("client");

//		if (client != null)
//			name = new String(name.getBytes("ISO-8859-1"), "UTF-8");

		System.out.println(user + "+" + pwd + "+" + name + "+" + age + "+" + teleno);
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

		PersonService pservice = new PersonService(jdbcUtils);
		UsersService uservice = new UsersService(jdbcUtils);

		boolean flag = false;
		try {
			flag = uservice.register(user, pwd);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (flag) {
			try {
				pservice.InsertPerson(user, name, age, teleno);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		JSONObject jsonObject = new JSONObject(); // 创建Json对象
		if (flag) {
			System.out.println("注册成功");
			jsonObject.put("state", true);
			System.out.println(jsonObject.toString()); // 调用toString方法将json对象转换成json字符串
			response.getWriter().write(jsonObject.toString());

			if (client == null) {
				request.setAttribute("username", user);
				request.getRequestDispatcher("ListServlet").forward(request, response);
			}

		} else {
			String msg = "用户名已存在";
			jsonObject.put("state", false);
			System.out.println(jsonObject.toString()); // 调用toString方法将json对象转换成json字符串
			response.getWriter().write(jsonObject.toString());

			if (client == null) {
				request.setAttribute("msg", msg);
				System.out.println("用户名已存在");
				request.getRequestDispatcher("register.jsp").forward(request, response);
			}
		}
	}
}
