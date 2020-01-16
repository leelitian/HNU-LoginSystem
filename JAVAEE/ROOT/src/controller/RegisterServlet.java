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
		// �����¼��ҵ��
		response.setContentType("application/json; charset=utf-8"); // ���ô��뷵�صı����ʽ
		// ��һ��ȡ�û�������û���������
		String user = request.getParameter("username");
		String name = request.getParameter("name");
		String age = request.getParameter("age");
		String teleno = request.getParameter("teleno");
		String pwd = request.getParameter("password");
		String client = request.getParameter("client");

//		if (client != null)
//			name = new String(name.getBytes("ISO-8859-1"), "UTF-8");

		System.out.println(user + "+" + pwd + "+" + name + "+" + age + "+" + teleno);
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

		JSONObject jsonObject = new JSONObject(); // ����Json����
		if (flag) {
			System.out.println("ע��ɹ�");
			jsonObject.put("state", true);
			System.out.println(jsonObject.toString()); // ����toString������json����ת����json�ַ���
			response.getWriter().write(jsonObject.toString());

			if (client == null) {
				request.setAttribute("username", user);
				request.getRequestDispatcher("ListServlet").forward(request, response);
			}

		} else {
			String msg = "�û����Ѵ���";
			jsonObject.put("state", false);
			System.out.println(jsonObject.toString()); // ����toString������json����ת����json�ַ���
			response.getWriter().write(jsonObject.toString());

			if (client == null) {
				request.setAttribute("msg", msg);
				System.out.println("�û����Ѵ���");
				request.getRequestDispatcher("register.jsp").forward(request, response);
			}
		}
	}
}
