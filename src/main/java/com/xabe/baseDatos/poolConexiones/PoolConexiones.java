package com.xabe.baseDatos.poolConexiones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Vector;

/**
 * Esta clase lo que vamos a relizar es hacer un pool de conexion parecido DBCP, BoneCp, cp30, que recomiendo algunos de estos.
 * @author Xabe
 *
 */
public class PoolConexiones {
	
	private static PoolConexiones poolConexiones;
	private static final int DEFAULT_TIME_OUT = 60000;
	private static final int MAX_CONNECTION = 10;
	
	private Vector<Connection> libres;
	private Vector<Connection> ocupadas;
	
	private PoolConexiones(String URL,String user,String passwd, int max) throws PoolConexionesException
	{
		poolConexiones = this;
		libres = new Vector<Connection>(max);
		ocupadas = new Vector<Connection>(max);
		for(int i = 0; i < max; i++)
		{
			try
			{
				libres.addElement(DriverManager.getConnection(URL,user,passwd));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		if(libres.size()==0)
		{
			throw new PoolConexionesException("No se establecieron conexiones de DB:"+URL);
		}
	}
	
	public static void init(String URL,String user,String passwd, int max) throws PoolConexionesException
	{
		if(poolConexiones==null)
		{
			poolConexiones = new PoolConexiones(URL,user,passwd,max);
		}
		else
		{
			throw new PoolConexionesException("Ya esta inicializado el pool");
		}
	}
	
	public static void init(String URL,String user,String passwd) throws PoolConexionesException
	{
		init(URL, user, passwd, MAX_CONNECTION);
	}
	
	
	public static Connection getConnection(boolean wait) throws PoolConexionesException
	{
		if(poolConexiones==null)
		{
			throw new PoolConexionesException("No esta inicializado el pool");
		}
		return poolConexiones._getConnection(wait);
	}
	
	public static Connection getConnection(int timetowait) throws PoolConexionesException
	{
		if(poolConexiones==null)
		{
			throw new PoolConexionesException("No esta inicializado el pool");
		}
		return poolConexiones._getConnection(timetowait);
	}
	
	public static void releaseConnection(Connection con) throws PoolConexionesException
	{
		if(poolConexiones==null)
		{
			throw new PoolConexionesException("No esta inicializado el pool");
		}
		poolConexiones._releaseConnection(con);
	}
	
	private synchronized void _releaseConnection(Connection con) throws PoolConexionesException
	{
		System.out.println("pool de conexiones. entrando realizado");
		if(!ocupadas.contains(con))
		{
			throw new PoolConexionesException("Esta Conexion no figura en uso");
		}
		ocupadas.removeElement(con);
		libres.addElement(con);
		System.out.println("pool de conexiones. conexion devuelta ");
		notify();
	}
	
	
	private synchronized Connection _getConnection(boolean dbwait) throws PoolConexionesException
	{
		System.out.println("DBPOOL._getConnection LIBRES: "+libres.size()+" COGIDAS: "+ocupadas.size());
		if(!dbwait && libres.size() == 0)
		{
			throw new PoolConexionesException("No hay conexiones Disponibles");
		}
		return _getConnection(DEFAULT_TIME_OUT);
	}
	
	private synchronized Connection _getConnection(int dbtimetowait) throws PoolConexionesException
	{
		Connection tmp;
		System.out.println("pool conexiones. entra para recupera una conexion");
		while(libres.size()==0)
		{
			try
			{
				wait(dbtimetowait);
			}
			catch(InterruptedException e)
			{
				System.out.println("pool conexiones. Vence el tiempo de espera el poll esta lleno");
				throw new PoolConexionesException("Timeout");
			}
		}
		tmp = libres.elementAt(0);
		libres.removeElement(tmp);
		ocupadas.addElement(tmp);
		System.out.println("pool de conexiones. obtiene una conexion libre");
		notify();
		return tmp;
	}
	
	
	public static void finalizar() {
		if (poolConexiones != null)
		{
			synchronized (poolConexiones) {
				closeConexion(poolConexiones.ocupadas);
				closeConexion(poolConexiones.libres);				
			}
		}
		System.out.println("pool de conexiones. cierra todas las conexiones.");
	}
	
	
	private static void closeConexion(Vector<Connection> connections){
		for(Connection connection : connections){
			try {
				connection.close();
			} catch (Exception sqle) {
				sqle.printStackTrace();
			}
		}
	}

}
