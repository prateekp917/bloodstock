package com.virtusa.bloodbank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

	public class DBConnection {
			private static Connection connection=null;
			private static final Logger log=Logger.getRootLogger();
			public DBConnection()
			{
				try
				{
					Class.forName("com.mysql.jdbc.Driver");
				connection=DriverManager.getConnection("jdbc:mysql://10.5.234.18:3306/bloodbank","root","1");
					log.info("Connection to database was established");

				}catch(ClassNotFoundException e)
				{
					log.error(e);
				}catch(SQLException e)
				{
					log.error(e);
				}
			}
			public static final Connection getConnection()
			{
				try {
					if(connection==null||connection.isClosed())
					{
						new DBConnection();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return connection;
			}



			public static void main(String[] args)throws SQLException {
				BasicConfigurator.configure();
				Connection con=DBConnection.getConnection();
				//System.out.println(con);
				con.close();
			}
		
	}


