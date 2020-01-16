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
			// 加载类路径下，即src目录下的druid.properties这个文件
			info.load(DataSourceUtils.class.getResourceAsStream("druid.properties"));

			// 对它的url进行组合
			ip = info.getProperty("mysql.ip");
			port = info.getProperty("mysql.port");
			database = info.getProperty("mysql.database");
			url = "jdbc:mysql://" + ip + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC";
			info.setProperty("url", url); // 设置url
			System.out.println(url);

			// 读取属性文件创建连接池
			ds = DruidDataSourceFactory.createDataSource(info);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Connection getCon() throws Exception {
		return ds.getConnection();
	}
}
