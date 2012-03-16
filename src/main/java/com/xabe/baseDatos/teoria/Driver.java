package com.xabe.baseDatos.teoria;

import com.xabe.baseDatos.util.Constants;

public class Driver {
	private String hola; 
	
	static{
		System.out.println("Ya estoy cargado en jvm");
	}
	
	public Driver() {
		System.out.println("Me han hecho una instacia!!!!");
		hola = "Me han instanciado a las "+Constants.getDateString();
	}
	
	public String getHola() {
		return hola;
	}
}
