package listener;

import java.sql.SQLException;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import util.JdbcUtils;

/**
 * Application Lifecycle Listener implementation class SessionListener
 *
 */
@WebListener
public class SessionListener implements HttpSessionListener {

	public SessionListener() {
		// TODO Auto-generated constructor stub
	}

	public void sessionCreated(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		
		HttpSession session = event.getSession();
		JdbcUtils jdbcUtils = (JdbcUtils) session.getAttribute("utils");
		
		if(jdbcUtils != null) {
			try {
				System.out.println(" Õ∑≈¡¨Ω”");
				jdbcUtils.releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
