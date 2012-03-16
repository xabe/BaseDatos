package com.xabe.baseDatos.transaciones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Clase que se encarga de realizar la conexion a la BBDD
 * @author xabe
 *
 */
public class JDBCTransactionDelegate{

	/**
	 * Para realizar una tranasaccion lo que hacemos es poner la propieda setAutoCommit a false para realizar la transacción.
	 * 
	 * También debemos poner nuestra atención en el transacción isolation que es el nivel de aislaimento de nuestra transacción; 
	 * pues sucede que las transacciones pueden ser configuradas para comportarse de varias formas y 
	 * todas ellas se realizan con base en el nivel de isolation, asignado, veamos los diferente tipos:

	 * Connection.TRANSACTION_NONE: con esta indicamos que no soportaremos transacciones.
	 * Connection.TRANSACTION_READ_COMMITTED: este es el mejor valor para las transacciones, pues no permite que otros usuarios puedan leer valores hasta que estos no se encuentren realmente enviados (commit) a la base de datos, así se evita la lectura de datos sucios.
	 * Connection.TRANSACTION_READ_UNCOMMITED: todo lo contrario del anterior, podríamos leer un dato que aun no ha sido enviado de forma exitosa o inclusive un dato que este pronto a hacersele rollback.
     * Connection.TRANSACTION_REPEATABLE_READ: Este nivel de isolation, nos protege un poco trabajar con datos sucios, evita las lecturas repetidas, pero la lectura de datos fantasmas puede ocurrir.
	 * Connection.TRANSACTION_SERIALIZABLE: Este es el nivel mas fuerte o seguro, pues evita todos los errores que permiten los niveles anteriores, el único problema es que todas estas validaciones para evitar errores, perjudicarían el perfomance de la aplicación, a continuación coloco de mas rápido a mas lentos los niveles antes citados:

	 * TRANSACTION_NONE – MUY RAPIDO
	 * TRANSACTION_READ_UNCOMMITED – MUY RAPIDO
	 * TRANSACTION_READ_COMMITED – RAPIDO
	 * TRANSACTION_REPEATABLE_READ – MEDIO RAPIDO
	 * TRANSACTION_SERIALIZABLE – LENTO
	 */
	private Connection connection = null;
	private Statement statement;
	private boolean oldStateOfAutoCommit = false;
	private int oldStateOfTransactionIsolation = Connection.TRANSACTION_READ_COMMITTED;
	
	public JDBCTransactionDelegate(String url,String user,String password,String driverName) throws SQLException, ClassNotFoundException{
		Class.forName(driverName);
		Connection connection = DriverManager.getConnection(url, user, password);
		this.init(connection,Connection.TRANSACTION_READ_COMMITTED);
	}

	public JDBCTransactionDelegate(Connection connection) throws SQLException {
		this.init(connection, Connection.TRANSACTION_READ_COMMITTED);
	}

	private void init(Connection connection, int isolation) throws SQLException {
		this.connection = connection;
		this.oldStateOfTransactionIsolation = this.connection.getTransactionIsolation();
		this.connection.setTransactionIsolation(isolation);
		this.statement = connection.createStatement();
	}

	public void commit() throws SQLException {
		this.connection.commit();
	}

	public void endCommint()  throws SQLException {
		this.statement.close();
		this.connection.setAutoCommit(this.oldStateOfAutoCommit);
		this.connection.setTransactionIsolation(this.oldStateOfTransactionIsolation);
	} 
	
	public void close() {
		try
		{
			this.statement.close();
			this.connection.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void rollback()  throws SQLException{
		this.connection.rollback();
	}

	public void start()  throws SQLException {
		if ( this.connection.getAutoCommit()) 
		{
			this.connection.setAutoCommit(false);
			this.oldStateOfAutoCommit = true;
		}
		this.statement = this.connection.createStatement();
	}

	public Connection getConnection() {
		return this.connection;
	}
	
	public Statement getStatement() {
		return this.statement;
	}

}
