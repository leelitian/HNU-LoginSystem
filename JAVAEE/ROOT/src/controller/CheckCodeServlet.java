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

import service.PhoneService;
import service.UsersService;
import util.DataSourceUtils;
import util.JdbcUtils;

/**
 * Servlet implementation class CheckCodeServlet
 */
@WebServlet("/CheckCodeServlet")
public class CheckCodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String username = request.getParameter("username");
		String code = request.getParameter("code");
		String password = request.getParameter("password");
		String client = request.getParameter("client");

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

		PhoneService phone_service = new PhoneService(jdbcUtils);
		UsersService uservice = new UsersService(jdbcUtils);

		boolean flag = false;
		try {
			flag = phone_service.checkCode(username, code);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		JSONObject jsonObject = new JSONObject(); // 创建Json对象
		if (flag) {
			try {
				flag = uservice.updateUsersPass(username, password);
				phone_service.deleteCode(username);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		if (client != null && client.equals("Android")) {
			jsonObject.put("state", flag);
			System.out.println(jsonObject.toString()); // 调用toString方法将json对象转换成json字符串
			response.getWriter().write(jsonObject.toString());
		} else {
			String msg = "";
			if (flag)
				msg = "重置密码成功！";
			else
				msg = "验证码输入错误！";
			request.setAttribute("msg", msg);
			request.getRequestDispatcher("reset.jsp").forward(request, response);
		}
	}

}
