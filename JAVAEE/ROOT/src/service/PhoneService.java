package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JdbcUtils;
import util.PhoneCode;

public class PhoneService {

	private JdbcUtils jdbcUtils;

	public PhoneService(JdbcUtils jdbcUtils) {
		this.jdbcUtils = jdbcUtils;
	}

	public String sendMessage(String code, String mobile) {
		String state = PhoneCode.getPhonemsg(code, mobile);
		return state;
	}

	public boolean checkCode(String username, String code) throws SQLException {

		String sql = "select * from codes where username = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(username);
		Map<String, Object> map = jdbcUtils.findSimpleResult(sql, params);

		String realcode = (String) map.get("code");

		return code.equals(realcode);
	}

	public boolean deleteCode(String username) throws SQLException {

		String sql = "delete from codes where username = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(username);
		boolean flag = jdbcUtils.updateByPreparedStatement(sql, params);

		return flag;
	}
	
	public Boolean storeCode(String username, String code) throws SQLException {
		String sql = "select * from codes where username = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(username);
		Map<String, Object> map = jdbcUtils.findSimpleResult(sql, params);
		boolean flag = false;

		if (map == null || map.isEmpty()) {
			sql = "insert into codes (username, code) values (?, ?)";
			params = new ArrayList<Object>();

			params.add(username);
			params.add(code);

			flag = jdbcUtils.updateByPreparedStatement(sql, params);
			return flag;

		} else {
			sql = "update codes set code = ? where username = ?";
			params = new ArrayList<Object>();

			params.add(code);
			params.add(username);

			flag = jdbcUtils.updateByPreparedStatement(sql, params);
			return flag;
		}
	}
	
}
