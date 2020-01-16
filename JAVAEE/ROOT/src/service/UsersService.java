package service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.UsersDao;
import entity.Users;
import util.JdbcUtils;

// ²åÈë¡¢É¾³ý¡¢¸üÐÂ
public class UsersService {

	private UsersDao dao = new UsersDao();
	private JdbcUtils jdbcUtils;

	public UsersService(JdbcUtils jdbcUtils) {
		this.jdbcUtils = jdbcUtils;
	}

	public boolean register(String username, String pass) throws FileNotFoundException, SQLException, IOException {
		// jdbcUtils.getConnection();
		Users user = new Users(username, pass);

		Map<String, Object> map = dao.findUserByName(jdbcUtils, username);
		if (!map.isEmpty())
			return false;
		boolean flag = dao.insertUsers(jdbcUtils, user);
		// jdbcUtils.releaseConn();
		return flag;
	}

	public boolean login(String username, String pass) throws FileNotFoundException, SQLException, IOException {

		Map<String, Object> map = dao.findUserByName(jdbcUtils, username);
		System.out.println(map);
		if (map.get("pass") != null && map.get("pass").equals(pass))
			return true;
		else
			return false;
	}

	public List<Users> getUsersList() throws SQLException, FileNotFoundException, IOException {
		// jdbcUtils.getConnection();
		List<Users> userList = new ArrayList<Users>();
		List<Map<String, Object>> listMap = dao.getAllUsers(jdbcUtils);

		for (Map<String, Object> map : listMap) {
			String username = (String) map.get("username");
			String pass = (String) map.get("pass");

			Users user = new Users(username, pass);
			userList.add(user);
		}
		// jdbcUtils.releaseConn();
		return userList;
	}

	public Users getUserByName(String username) throws FileNotFoundException, SQLException, IOException {
		// jdbcUtils.getConnection();
		Map<String, Object> map = dao.findUserByName(jdbcUtils, username);

		String pass = null;
		if (map != null && !map.isEmpty()) {
			pass = (String) map.get("pass");
		}
		// jdbcUtils.releaseConn();
		Users user = new Users(username, pass);
		return user;
	}

	public boolean updateUsersPass(String username, String newpass)
			throws FileNotFoundException, SQLException, IOException {
		// jdbcUtils.getConnection();
		Users user = new Users(username, newpass);
		boolean flag = dao.updateUsersByUsername(jdbcUtils, user);
		// jdbcUtils.releaseConn();

		return flag;
	}

	public boolean deleteUser(String username) throws FileNotFoundException, SQLException, IOException {
		// jdbcUtils.getConnection();
		boolean flag = dao.deleteByUsername(jdbcUtils, username);
		// jdbcUtils.releaseConn();
		return flag;
	}
}
