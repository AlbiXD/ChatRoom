package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnector {

	private String password;
	private static Connection connection;
	private UserTable user;
	private String database;
	private String username;;

	public SQLConnector(String database, String username, String password) {
		this.password = password;
		this.username = username;
		this.database = database;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", username, password);

			user = UserTable.getInstance(connection);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}

	public UserTable getUser() {
		return user;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setUser(UserTable user) {
		this.user = user;
	}

}
