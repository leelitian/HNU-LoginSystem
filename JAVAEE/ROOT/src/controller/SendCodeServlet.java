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
import service.PhoneService;
import util.DataSourceUtils;
import util.JdbcUtils;

/**
 * Servlet implementation class SendCodeServlet
 */
@WebServlet("/SendCodeServlet")
public class SendCodeServlet extends HttpServlet {
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

		String username = request.getParameter("username");
		String teleno = request.getParameter("teleno");
		String client = request.getParameter("client");

//		if(client == null || client.isEmpty() || client == "NULL") {
//			username = (String) request.getAttribute("username");
//			teleno = (String) request.getAttribute("teleno");
//		}

		System.out.println(username + ":" + teleno + ":" + client);

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

		PhoneService phone_service = new PhoneService(jdbcUtils);
		PersonService person_service = new PersonService(jdbcUtils);

		String code = vcode();
		String msg = new String();

		Person person = null;
		try {
			person = person_service.getPersonByUsername(username);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (person == null || person.getTeleno() == null || !person.getTeleno().equals(teleno)) {
			msg = "�û�������벻ƥ��";
		} else {
			msg = phone_service.sendMessage(code, teleno);
			if (msg.equals("��ȡ��֤��ɹ�")) {
				try {
					phone_service.storeCode(username, code);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		if (client != null && client.equals("Android")) {
			JSONObject jsonObject = new JSONObject(); // ����Json����
			jsonObject.put("msg", msg);
			System.out.println(jsonObject.toString()); // ����toString������json����ת����json�ַ���
			response.getWriter().write(jsonObject.toString());
		} else {
			request.setAttribute("msg", msg);
			request.getRequestDispatcher("reset.jsp").forward(request, response);
		}
	}

	public static String vcode() {
		String vcode = "";
		for (int i = 0; i < 6; i++) {
			vcode = vcode + (int) (Math.random() * 9);
		}
		return vcode;
	}
}
