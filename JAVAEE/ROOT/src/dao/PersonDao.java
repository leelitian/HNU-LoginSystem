package dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entity.Person;
import util.JdbcUtils;

public class PersonDao {
	// ���뵽person��
	public boolean insertPerson(JdbcUtils jdbcUtils, Person person) throws SQLException, FileNotFoundException, IOException {

		String sql = "insert into person (username, name, age, teleno) values (?, ?, ?, ?)";
		List<Object> params = new ArrayList<Object>();
		params.add(person.getUsername());
		params.add(person.getName());
		params.add(person.getAge());
		params.add(person.getTeleno());
		boolean flag = jdbcUtils.updateByPreparedStatement(sql, params);

		return flag;
	}

	// ͨ��username����person����
	public boolean updatePersonByUsername(JdbcUtils jdbcUtils, Person person) throws SQLException, FileNotFoundException, IOException {

		String sql;
		List<Object> params = new ArrayList<Object>();
		sql = "update person set name = ?, age = ?, teleno = ? where username = ?";
		params.add(person.getName());
		params.add(person.getAge());
		params.add(person.getTeleno());
		params.add(person.getUsername());

		boolean flag = jdbcUtils.updateByPreparedStatement(sql, params);

		return flag;
	}

	// ���person��
	public boolean clearPerson(JdbcUtils jdbcUtils) throws SQLException, FileNotFoundException, IOException {

		String sql = "delete from person";
		boolean flag = jdbcUtils.updateByPreparedStatement(sql, null);
		return flag;
	}

	// ��ѯperson���Ƿ����username
	public boolean findUsername(JdbcUtils jdbcUtils, String username)
			throws SQLException, FileNotFoundException, IOException {

		String sql = "select * from person where username = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(username);

		Map<String, Object> map = jdbcUtils.findSimpleResult(sql, params);

		return !map.isEmpty();
	}

	// ��username��Ӧ����Ŀ�ӱ���ɾ��
	public boolean deleteByUsername(JdbcUtils jdbcUtils, String username)
			throws SQLException, FileNotFoundException, IOException {

		String sql = "delete from person where username = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(username);

		boolean flag = jdbcUtils.updateByPreparedStatement(sql, params);

		return flag;
	}

	// ��testΪǰ׺���ַ���
	public List<String> getAllPrefixTest(JdbcUtils jdbcUtils)
			throws SQLException, FileNotFoundException, IOException {

		String sql = "select * from person";
		List<Map<String, Object>> listMap = jdbcUtils.findModeResult(sql, null);
		List<String> result = new ArrayList<String>();

		for (Map<String, Object> map : listMap) {
			String s = (String) map.get("username");
			if (s.length() >= 4 && s.substring(0, 4).equals("test"))
				result.add(s);
		}

		return result;
	}

	// ��ӡ��
	public void print(JdbcUtils jdbcUtils) throws SQLException, FileNotFoundException, IOException {

		String sql = "select * from person";
		List<Map<String, Object>> listMap = jdbcUtils.findModeResult(sql, null);

		System.out.println("< person >");
		System.out.println("[name]\t\t[username]\t[age]\t\t[teleno]");
		for (Map<String, Object> map : listMap) {
			System.out.println(map.get("name") + "\t\t" + map.get("username") + "\t\t" + map.get("age") + "\t\t"
					+ map.get("teleno"));
		}
	}
	
	public List<Map<String, Object>> getAllPerson(JdbcUtils jdbcUtils) throws SQLException {
		String sql = "select * from person";
		List<Map<String, Object>> listMap = jdbcUtils.findModeResult(sql, null);
		
		return listMap;
	}

	public Map<String, Object> getPersonByUsername(JdbcUtils jdbcUtils, String username) throws SQLException {
		
		String sql = "select * from person where username = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(username);

		Map<String, Object> map = jdbcUtils.findSimpleResult(sql, params);
		
		return map;
	}
}
