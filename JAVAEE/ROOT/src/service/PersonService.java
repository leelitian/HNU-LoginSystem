package service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.PersonDao;
import entity.Person;
import util.JdbcUtils;

public class PersonService {
	private PersonDao dao = new PersonDao();
	private JdbcUtils jdbcUtils;

	public PersonService(JdbcUtils jdbcUtils) {
		this.jdbcUtils = jdbcUtils;
	}

	public boolean InsertPerson(String username, String name, String age, String teleno)
			throws FileNotFoundException, SQLException, IOException {

		Integer age_int = null;
		if (!age.isEmpty())
			age_int = Integer.valueOf(age);

		if (teleno.isEmpty())
			teleno = null;

		Person person = new Person(username, name, age_int, teleno);
		boolean isExist = dao.findUsername(jdbcUtils, username);

		if (isExist) {
			dao.updatePersonByUsername(jdbcUtils, person);
		} else {
			dao.insertPerson(jdbcUtils, person);
		}

		return isExist;
	}

	public List<Person> getPersonList() throws SQLException, FileNotFoundException, IOException {

		List<Person> personList = new ArrayList<Person>();
		List<Map<String, Object>> listMap = dao.getAllPerson(jdbcUtils);

		for (Map<String, Object> map : listMap) {
			String username = (String) map.get("username");
			String name = (String) map.get("name");
			Integer age = (Integer) map.get("age");
			String teleno = (String) map.get("teleno");

			Person person = new Person(username, name, age, teleno);
			personList.add(person);
		}

		return personList;
	}

	public boolean deletePerson(String username) throws FileNotFoundException, SQLException, IOException {

		return dao.deleteByUsername(jdbcUtils, username);
	}

	public Person getPersonByUsername(String string) throws SQLException {

		Map<String, Object> map = dao.getPersonByUsername(jdbcUtils, string);
		
		String username = (String) map.get("username");
		String name = (String) map.get("name");
		Integer age = (Integer) map.get("age");
		String teleno = (String) map.get("teleno");
		Person person = new Person(username, name, age, teleno);
		
		return person;
	}
}
