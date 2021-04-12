package com.ca.tests;

import java.sql.*;

public class TestSQLServer {
	
	public static void main(String[] args) {
		
		System.out.println("Testing teh SQL Server..");
		try{  
			Class.forName("com.mysql.cj.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
					"jdbc:mysql://localhost:3306/cauatdb?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&user=catester&password=Uat#2020");  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select * from run_test");  
			while(rs.next())  
			System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));  
			con.close();  
			}catch(Exception e){ System.out.println(e);}  
			}  
	} 


