package dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entity.Users;
import util.JdbcUtils;

public class UsersDao {
	// 将对象插入至user表
	public boolean insertUsers(JdbcUtils jdbcUtils, Users user)
			throws SQLException, FileNotFoundException, IOException {

		String sql = "insert into users (username, pass) values (?, ?)";
		List<Object> params = new ArrayList<Object>();
		params.add(user.getUsername());
		params.add(user.getPass());

		boolean flag = jdbcUtils.updateByPreparedStatement(sql, params);
		return flag;
	}

	// 将username对应的条目从表中删除
	public boolean deleteByUsername(JdbcUtils jdbcUtils, String username)
			throws SQLException, FileNotFoundException, IOException {

		String sql = "delete from users where username = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(username);

		boolean flag = jdbcUtils.updateByPreparedStatement(sql, params);
		return flag;
	}

	// 清空User
	public boolean clearUser(JdbcUtils jdbcUtils) throws SQLException, FileNotFoundException, IOException {

		String sql = "delete from users";
		boolean flag = jdbcUtils.updateByPreparedStatement(sql, null);
		return flag;
	}

	// 获取users中所有以test为前缀的字符串username
	public List<String> getAllPrefixTest(JdbcUtils jdbcUtils) throws SQLException, FileNotFoundException, IOException {

		String sql = "select * from users";
		List<Map<String, Object>> listMap = jdbcUtils.findModeResult(sql, null);
		List<String> result = new ArrayList<String>();

		for (Map<String, Object> map : listMap) {
			String s = (String) map.get("username");
			if (s.length() >= 4 && s.substring(0, 4).equals("test"))
				result.add(s);
		}

		return result;
	}

	// 返回符合username的user
	public Map<String, Object> findUserByName(JdbcUtils jdbcUtils, String username)
			throws SQLException, FileNotFoundException, IOException {

		String sql = "select * from users where username = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(username);

		Map<String, Object> map = jdbcUtils.findSimpleResult(sql, params);
		return map;
	}
	
	// 打印表
	public void print(JdbcUtils jdbcUtils) throws SQLException, FileNotFoundException, IOException {

		String sql = "select * from users";
		List<Map<String, Object>> listMap = jdbcUtils.findModeResult(sql, null);

		System.out.println("< users >");
		System.out.println("[username]\t[pass]");
		for (Map<String, Object> map : listMap) {
			System.out.println(map.get("username") + "\t\t" + map.get("pass"));
		}
	}
	
	public List<Map<String, Object>> getAllUsers(JdbcUtils jdbcUtils) throws SQLException {
		String sql = "select * from users";
		List<Map<String, Object>> listMap = jdbcUtils.findModeResult(sql, null);
		
		return listMap;
	}
	
	// 通过username更新user数据
	public boolean updateUsersByUsername(JdbcUtils jdbcUtils, Users user) throws SQLException, FileNotFoundException, IOException {

		String sql;
		List<Object> params = new ArrayList<Object>();
		sql = "update users set pass = ? where username = ?";
		params.add(user.getPass());
		params.add(user.getUsername());

		boolean flag = jdbcUtils.updateByPreparedStatement(sql, params);

		return flag;
	}
}
