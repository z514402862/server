/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author zzl
 */
public class OperateMySQL {

	public static boolean selectExist(String userName) {
		boolean bool = true;
		try {
			Connection conn = User.getConnection();
			String sql = "select * from userinfor where userName=?";
			PreparedStatement per = conn.prepareStatement(sql);

			per.setString(1, userName);

			ResultSet re = per.executeQuery();
			
			if (!re.next()) {
				bool = false;
			} 
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return bool;
	}

	public static boolean insert(String userName, String password, String nickName, String ip) {
		boolean bool = false;
		try {
			Connection conn = User.getConnection();
			String sql = "insert into userinfor values (null,?,?,?,?)";
			PreparedStatement per = conn.prepareStatement(sql);
			
			per.setString(1, userName);
			per.setString(2, password);
			per.setString(3, nickName);
			per.setString(4, ip);
			
			int res = per.executeUpdate();
			
			if(res == 1){
				bool = true;
			}
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		return bool;
	}
	public static ArrayList<String> userInfor(String userName) {
		ArrayList<String> list = new ArrayList<String>();
		try {
			Connection conn = User.getConnection();

			String sql = "select password,nickname,ip from userinfor where userName=?";

			PreparedStatement per = conn.prepareStatement(sql);
			
			per.setString(1, userName);

			ResultSet re = per.executeQuery();

			if (re.next()) {
				list.add(re.getString("passWord"));
				list.add(re.getString("nickName"));
				list.add(re.getString("ip"));
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return list;
	}
}
