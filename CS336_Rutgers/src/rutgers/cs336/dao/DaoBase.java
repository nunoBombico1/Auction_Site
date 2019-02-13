package rutgers.cs336.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoBase {
	private static String dbURL;
	private static String user;
	private static String pwd;

	public static void init(String url, String username, String password) {
		dbURL = url;
		user = username;
		pwd = password;
	}

	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		//
		Connection conn = DriverManager.getConnection(dbURL, user, pwd);
		//
		return conn;
	}
}