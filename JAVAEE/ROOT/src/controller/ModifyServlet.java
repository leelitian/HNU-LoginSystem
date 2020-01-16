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
 * Servlet implementation class ModifyServlet
 */
@WebServlet("/ModifyServlet")
public class ModifyServlet extends HttpServlet {
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

		// ��һ����ȡ����Ĳ���
		response.setContentType("application/json; charset=utf-8"); // ���ô��뷵�صı����ʽ

		String username = request.getParameter("username");
		String old = request.getParameter("oldpass");
		String password = request.getParameter("password");
		String client = request.getParameter("client");
		System.out.println("usename: " + username + "   " + "password: " + password);

		// ��session�л�ȡconnection�����û�У�������һ����
		JdbcUtils jdbcUtils = null;
		HttpSession session = request.getSession();
		DataSourceUtils dsu = new DataSourceUtils();
		if (session.getAttribute("utils") == null) {
			try {
				System.out.println("��������");
				jdbcUtils = new JdbcUtils(dsu.getCon());
			} catch (Exception e) {
				e.printStackTrace();
			}
			session.setAttribute("utils", jdbcUtils);
		} else {
			jdbcUtils = (JdbcUtils) session.getAttribute("utils");
		}

		UsersService uservice = new UsersService(jdbcUtils);

		boolean flag = false;

		try {
			if (uservice.getUserByName(username).getPass().equals(old)) {
				flag = true;
				uservice.updateUsersPass(username, password);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		JSONObject jsonObject = new JSONObject(); // ����Json����

		if (client != null) {
			System.out.println("�޸�����");
			jsonObject.put("state", flag);
			System.out.println(jsonObject.toString()); // ����toString������json����ת����json�ַ���
			response.getWriter().write(jsonObject.toString());
		}
	}
}
