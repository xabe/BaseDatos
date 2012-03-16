package com.xabe.baseDatos.conexionSimple;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Esta clase lo que hace es un conexión a una base de datos antes de hacer la conexión
 * tenemos que tener en nuestro classpath el jar del driver de base de datos que queremos conectarnos
 * en este ejemplo utilizo Mysql
 * @author Xabe
 *
 */
public class ConexionSimple {
	private Connection connection;
	
	public ConexionSimple(String driverName, String url, String user, String password) throws ClassNotFoundException,SQLException {
		Class.forName(driverName);
		connection = DriverManager.getConnection(url, user, password);
	}
	
	public boolean probarConexion(){
		Statement statement = null;
		boolean result = false;
		try
		{
			statement = connection.createStatement();
			result = statement.execute("Select 1");
		}catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			if(statement != null)
			{
				try
				{
					statement.close();
				}catch (SQLException e) {}
			}
		}
		return result;
	}
	
	public void close() {
		try
		{
			this.connection.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	} 
}
