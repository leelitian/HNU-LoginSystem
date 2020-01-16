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

		// ��һ����ȡ����Ĳ���
		response.setContentType("application/json; charset=utf-8"); // ���ô��뷵�صı����ʽ

		String user = request.getParameter("user");
		String pwd = request.getParameter("pwd");
		String client = request.getParameter("client");
		System.out.println("usename: " + user + "   " + "password: " + pwd);

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

		// �ڶ���������ҵ��㣺ע���ҵ�񼴿�
		boolean flag = false;
		try {
			flag = uservice.login(user, pwd);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		JSONObject jsonObject = new JSONObject(); // ����Json����
		if (flag) {
			System.out.println("��½�ɹ�");

			if (client != null && client.equals("Android")) {
				jsonObject.put("state", true);
				System.out.println(jsonObject.toString()); // ����toString������json����ת����json�ַ���
				response.getWriter().write(jsonObject.toString());
			}
			// �������������½�ɹ���ֱ�ӽ����е��û����з�ҳ��ʾ
			else {
				request.setAttribute("username", user);
				request.getRequestDispatcher("ListServlet").forward(request, response);
			}
		} else {
			System.out.println("�û������������");

			if (client != null && client.equals("Android")) {
				jsonObject.put("state", false);
				// ����toString������json����ת����json�ַ���
				System.out.println(jsonObject.toString());
				response.getWriter().write(jsonObject.toString());
			} else {
				String msg = "�û������������";
				request.setAttribute("msg", msg);
				request.getRequestDispatcher("login.jsp").forward(request, response);
			}
		}
	}
}