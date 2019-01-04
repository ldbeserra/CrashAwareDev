package br.ufrn.lets.crashawaredev.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExceptionInfoDao {
	
	private Connection con;
	
	public ExceptionInfoDao() {
		try {
			con = ConnectionFactory.getConnection("memo", "memo");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getExceptionInformation(String exceptionName) {
		String sql = "SELECT information FROM crashawaredev.info_exception "
				+ "   WHERE exception_name ilike '" + exceptionName + "' LIMIT 1;";
		
		try {
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			while(result != null && result.next()) {
				return result.getString("information");
			}
			
			stmt.close();
			result.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "Nenhuma informação registrada para este tipo de exceção.";
	}
	
	public boolean presistExceptionInformation(String exName, String text) {
		String sqlInsert = "INSERT INTO crashawaredev.info_exception VALUES (?,?,?);";
		String sqlUpdate = "UPDATE crashawaredev.info_exception SET information = ? WHERE id = ? ";
		
		try {
			int id = 1;
			
			Statement stmt = con.createStatement();

			PreparedStatement prepStmt = null;
			
			ResultSet rsEx = stmt.executeQuery("SELECT id FROM crashawaredev.info_exception WHERE exception_name = '" + exName + "'");
			
			if(rsEx != null && rsEx.next()) {
				id = rsEx.getInt("id");
				
				prepStmt = con.prepareStatement(sqlUpdate);
				prepStmt.setString(1, text);
				prepStmt.setInt(2, id);
				prepStmt.executeUpdate();
				
				prepStmt.close();
				rsEx.close();
				
			} else {
				
				ResultSet rsId = stmt.executeQuery("SELECT MAX(id) AS id FROM crashawaredev.info_exception");
				if(rsId != null && rsId.next())
					id = rsId.getInt("id") + 1;
					
				prepStmt = con.prepareStatement(sqlInsert);
				prepStmt.setInt(1, id);
				prepStmt.setString(2, text);
				prepStmt.setString(3, exName);
				
				prepStmt.executeUpdate();
				
				prepStmt.close();
				rsId.close();
			}
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
