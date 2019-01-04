package br.ufrn.lets.crashawaredev.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
private static Connection connection = null;
	
	public static Connection getConnection(String user, String senha) throws SQLException {
    	if(connection == null){
    			connection = DriverManager.getConnection(
                    "jdbc:postgresql://bd-testes.info.ufrn.br:5432/memo", user, senha);
    	}
    		
        return connection;
	}
}
