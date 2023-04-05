/**
 * SQLConnector class is responsible for connecting the server
 * and the client with the database responsible for storing users.
 * 
 * @author	Albi Zhaku
 */

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

	/**
	 * Constructor for SQLConnector class that establishes a connection to the
	 * database.
	 * 
	 * @param database - the name of the database to connect to.
	 * 
	 * @param username - the username for the database.
	 * 
	 * @param password - the password for the database.
	 */
	public SQLConnector(String database, String username, String password) {
		this.password = password;
		this.username = username;
		this.database = database;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database, username, password);

			user = UserTable.getInstance(connection, this);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Returns the current user.
	 * 
	 * @return UserTable
	 */
	public UserTable getUser() {
		return user;
	}

	/**
	 * Returns the current connection to the database.
	 * 
	 * @return Connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Sets the current user.
	 * 
	 * @param user UserTable to be set
	 */
	public void setUser(UserTable user) {
		this.user = user;
	}

	/**
	 * Returns the name of the current database.
	 * 
	 * @return String
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * Sets the name of the current database.
	 * 
	 * @param database String to be set as the database name
	 */
	public void setDatabase(String database) {
		this.database = database;
	}

}
