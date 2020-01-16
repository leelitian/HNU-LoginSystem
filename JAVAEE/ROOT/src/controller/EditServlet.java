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
import util.DataSourceUtils;
import util.JdbcUtils;

/**
 * Servlet implementation class EditServlet
 */
@WebServlet("/EditServlet")
public class EditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
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
		// 第一：获取请求的参数
		response.setContentType("application/json; charset=utf-8"); // 设置代码返回的编码格式

		String username = request.getParameter("username");
		String name = request.getParameter("name");
		String age = request.getParameter("age");
		String teleno = request.getParameter("teleno");
		String client = request.getParameter("client");
		
		System.out.println("修改前：" + name);
		
//		if (client != null)
//			name = new String(name.getBytes("ISO-8859-1"), "UTF-8");

		System.out.println("修改后：" + name);

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

		boolean flag = true;
		try {
			flag = flag & pservice.deletePerson(username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			flag = flag & (!pservice.InsertPerson(username, name, age, teleno));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		JSONObject jsonObject = new JSONObject(); // 创建Json对象
		if (client != null) {
			jsonObject.put("state", flag);
			System.out.println(jsonObject.toString()); // 调用toString方法将json对象转换成json字符串
			response.getWriter().write(jsonObject.toString());
		}
	}

}
