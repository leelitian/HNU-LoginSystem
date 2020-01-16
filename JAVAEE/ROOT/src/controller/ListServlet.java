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

import entity.Person;
import service.PersonService;
import util.DataSourceUtils;
import util.JdbcUtils;

/**
 * Servlet implementation class ListServlet
 */
@WebServlet("/ListServlet")
public class ListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("application/json; charset=utf-8"); // 设置代码返回的编码格式
		String username = (String) request.getParameter("username");
		String client = (String) request.getParameter("client");
		System.out.println("username: " + username);
		
		if(client == null || client.isEmpty())
			username = (String) request.getAttribute("username");
		
		// 从session中获取connection（如果没有，就设置一个）
		JdbcUtils jdbcUtils = null;
		HttpSession session = request.getSession();
		DataSourceUtils dsu = new DataSourceUtils();
		if (session.getAttribute("utils") == null) {
			try {
				jdbcUtils = new JdbcUtils(dsu.getCon());
			} catch (Exception e) {
				e.printStackTrace();
			}
			session.setAttribute("utils", jdbcUtils);
		} else {
			jdbcUtils = (JdbcUtils) session.getAttribute("utils");
		}

		PersonService personService = null;
		try {
			personService = new PersonService(jdbcUtils);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Person person = null;
		try {
			person = personService.getPersonByUsername(username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(client != null && client.equals("Android")) {
			
			String name = (person.getName() == null) ? "" : person.getName();
			String age = (person.getAge() == null) ? "" : person.getAge().toString();
			String teleno = (person.getTeleno() == null) ? "" : person.getTeleno();
			
			JSONObject jsonObject = new JSONObject(); // 创建Json对象
			jsonObject.put("name", name);
			jsonObject.put("age", age);
			jsonObject.put("teleno", teleno);
			System.out.println(jsonObject.toString()); // 调用toString方法将json对象转换成json字符串
			response.getWriter().write(jsonObject.toString());
			
		} else {
			request.setAttribute("person", person);
			request.getRequestDispatcher("welcome.jsp").forward(request, response);
		}
	}

}
