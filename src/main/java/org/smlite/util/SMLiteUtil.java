package org.smlite.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class SMLiteUtil {

	 private static final String EMPLOYEE_TABLE = "create table IF NOT EXISTS testdb.Employee ( "
		    + "   id INT PRIMARY KEY, name VARCHAR(20) not null, poCreated BOOLEAN, "
		    + "   poNumber BIGINT(20), poDetails VARCHAR(20), type VARCHAR(20))";
	public static String nextId() {
		return System.currentTimeMillis() + "";
	}

	public static Connection getConnection() throws Exception {
	    String driver = "com.mysql.jdbc.Driver";
	    String url = "jdbc:mysql://localhost:3306";
	    String username = "root";
	    String password = "admin";
	    Class.forName(driver);
	    Connection conn = DriverManager.getConnection(url, username, password);
	    return conn;
	  }
	public static void main(String[] args) {
		Connection conn = null; 
		Statement stmt=null;
		try {
			
			conn = getConnection(); 
			stmt=conn.createStatement();
			stmt.executeUpdate("Drop DATABASE IF EXISTS testdb");
			stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS testdb");
			stmt.executeUpdate(EMPLOYEE_TABLE);
			
		
			System.out.println("Done");
			
		

		}catch (ClassNotFoundException e) {
		      System.out.println("error: failed to load MySQL driver.");
		      e.printStackTrace();
		    } catch (SQLException e) {
		      System.out.println("error: failed to create a connection object.");
		      e.printStackTrace();
		    } catch (Exception e) {
		      System.out.println("other error:");
		      e.printStackTrace();
		    } finally {
		      try {
		        stmt.close();
		        conn.close();        
		      } catch (SQLException e) {
		        e.printStackTrace();
		      }
		    }

	}
}
