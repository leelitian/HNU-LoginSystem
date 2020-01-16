package test;

import dao.PersonDao;
import dao.UsersDao;
import entity.Person;
import entity.Users;
import service.PhoneService;
import util.DataSourceUtils;
import util.JdbcUtils;

public class Demo {
	/**
	 * @param args
	 * @throws Exception 
	 */

	public static void main(String[] args) throws Exception {
		
		DataSourceUtils dsu = new DataSourceUtils();
		JdbcUtils jdbcUtils = new JdbcUtils(dsu.getCon());

		UsersDao usersDao = new UsersDao();
		PersonDao personDao = new PersonDao();
		// ���user���person��
		usersDao.clearUser(jdbcUtils);
		personDao.clearPerson(jdbcUtils);

		/*----------------------------(2)--------------------------- */

		Users user[] = { new Users("ly", "123456"), new Users("liming", "345678"), new Users("test", "11111"),
				new Users("test1", "12345") };

		for (int i = 0; i < user.length; ++i)
			usersDao.insertUsers(jdbcUtils, user[i]);

		Person person[] = { new Person("ly", "����", null, null), new Person("liming", "����", 25, null),
				new Person("test", "�����û�", 20, "13388449933") };

		for (int i = 0; i < person.length; ++i)
			personDao.insertPerson(jdbcUtils, person[i]);

		System.out.println("����ɹ���");
		usersDao.print(jdbcUtils);
		personDao.print(jdbcUtils);

		/*----------------------------(3)--------------------------- */

		Person p[] = { new Person("ly", "����", null, null), new Person("test2", "�����û�2", null, null),
				new Person("test1", "�����û�1", 33, null), new Person("test", "����", 23, "18877009966"),
				new Person("admin", "admin", null, null) };

		for (int i = 0; i < p.length; ++i) {
			if (!personDao.findUsername(jdbcUtils, p[i].getUsername())) {
				personDao.insertPerson(jdbcUtils, p[i]);
				if (usersDao.findUserByName(jdbcUtils, p[i].getUsername()).isEmpty())
					usersDao.insertUsers(jdbcUtils, new Users(p[i].getUsername(), "888888"));
			} else {
				personDao.updatePersonByUsername(jdbcUtils, p[i]);
			}
		}

		System.out.println("���³ɹ���");
		usersDao.print(jdbcUtils);
		personDao.print(jdbcUtils);

		/*----------------------------(4)--------------------------- */

//		List<String> userPrefixTest = usersDao.getAllPrefixTest(jdbcUtils);
//		for (String s : userPrefixTest) {
//			usersDao.deleteByUsername(jdbcUtils, s);
//		}
//
//		List<String> personPrefixTest = personDao.getAllPrefixTest(jdbcUtils);
//		for (String s : personPrefixTest) {
//			personDao.deleteByUsername(jdbcUtils, s);
//		}
//
//		System.out.println("ɾ���ɹ���");
//		usersDao.print(jdbcUtils);
//		personDao.print(jdbcUtils);
//
		PhoneService ps = new PhoneService(jdbcUtils);
		ps.storeCode("LLT", "eqweq");
		
		jdbcUtils.releaseConn();
	}
}
