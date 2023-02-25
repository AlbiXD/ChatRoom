package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class SQLConnector {

	private String password;
	private static Connection connection;
	private UserTable user;;

	public SQLConnector(String password) {
		this.password = password;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/albi", "root", password);

			System.out.println("Database Connected");

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
