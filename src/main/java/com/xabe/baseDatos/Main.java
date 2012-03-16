package com.xabe.baseDatos;

import java.sql.SQLException;

import com.xabe.baseDatos.conexionSimple.ConexionSimple;
import com.xabe.baseDatos.teoria.Driver;
import com.xabe.baseDatos.transaciones.JDBCTransactionDelegate;



public class Main {

	public Main() {
	}

	/**
	 * Explicación del metodo forname()
	 * Cuando invocamos el metodo Class.forName(x) lo que hace es carga la clase dinamicamente en tiempo de ejecución, hace que la clase x pasa a ser inicialzado es decir 
	 * que la JVM ejecuta todo el bloque de la parte estatica y despues carga la clase. El metodo devuelve un objecto de tipo Class de la clase X, este objecto no es una instacia de la clase.
	 * Este método utiliza el cargador de clase de la clase que lo invoca.
	 * 
	 */
	public void probarTeoria() {
		try 
		{
			System.out.println("Primero llamamos al método forName:");
			Class<?> c = Class.forName("com.xabe.baseDatos.teoria.Driver");
			Driver a = (Driver) c.newInstance();
			System.out.println(a.getHola());
			System.out.println("Segunda llamada al método forName:");
			Class<?> c1 = Class.forName(Driver.class.getName());
			a = (Driver) c1.newInstance();
			System.out.println(a.getHola());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Este nos premite probar una conexión simple a una base de datos
	 * 
	 * ORACLE
	 * Driver : oracle.jdbc.OracleDriver
	 * URL : jdbc:oracle:thin:@localhost:1521:ORL
	 * 
	 * MYSQL
	 * Driver : com.mysql.jdbc.Driver
	 * URL : jdbc:mysql://localhost:3306/database
	 */
	public void probarConexionSimple(){
		ConexionSimple conexionSimple = null;
		try
		{
			System.out.println("Probando la conexión a la base de datos");
			conexionSimple = new ConexionSimple("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/ke", "root", "root");
			if(conexionSimple.probarConexion())
			{
				System.out.println("Ha conexión a la base de datos");
			}
			else
			{
				System.out.println("No hay conexión a la base de datos");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(conexionSimple != null)
			{
				conexionSimple.close();
			}
		}
	}
	
	/**
	 * En esta clase se encarga de prepara la conexion para que sea una transaccion todos las operaciones que hacemos entes start
	 * hasta el commint en caso de fallo llamamos al metodos rollback para deshacer todoas la operaciones que hemos realizado entre start y commint.
	 */
	public void probarTransacciones() throws Exception{
		JDBCTransactionDelegate delegate = new JDBCTransactionDelegate("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/ke", "root", "root");
		try
		{
			delegate.start();
			//Realiza todas operaciones que deseamos
			delegate.commit();
		}catch (Exception e) {
			e.printStackTrace();
			try
			{
				delegate.rollback();
			}catch (SQLException se) {
				se.printStackTrace();
			}
		}
		finally{
			if(delegate != null)
			{
				delegate.close();
			}
		}
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.probarTeoria();
		main.probarConexionSimple();
	}

}
