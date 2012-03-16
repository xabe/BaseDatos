package com.xabe.baseDatos.poolConexiones;

public class PoolConexionesException extends Exception{
	private static final long serialVersionUID = 1L;

	public PoolConexionesException() {
		super();
	}
	
	public PoolConexionesException(String message) {
		super(message);
	}

}
