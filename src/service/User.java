/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Driver;

/**
 *
 * @author zzl
 */
public class User {
	private static final String URL = "jdbc:mysql://localhost:3306/user?zeroDateTimeBehavior=convertToNull";
	private static final String USER = "root";
	private static final String PASSWORD = "1234";
	private static Connection conn = null;

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Connection getConnection(){
		return conn;
	} 
}
