package util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

public class DataSourceUtils {
	private static DataSource ds;
	private static String ip, port, database, url;
	
	static {
		try {
			Properties info = new Properties();
			// ������·���£���srcĿ¼�µ�druid.properties����ļ�
			info.load(DataSourceUtils.class.getResourceAsStream("druid.properties"));

			// ������url�������
			ip = info.getProperty("mysql.ip");
			port = info.getProperty("mysql.port");
			database = info.getProperty("mysql.database");
			url = "jdbc:mysql://" + ip + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC";
			info.setProperty("url", url); // ����url
			System.out.println(url);

			// ��ȡ�����ļ��������ӳ�
			ds = DruidDataSourceFactory.createDataSource(info);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Connection getCon() throws Exception {
		return ds.getConnection();
	}
}
