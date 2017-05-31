package com.festival;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.util.DBConn;

public class FestivalDAO {
	
	private Connection conn = DBConn.getConnection();
	
	public int insertFestival(FestivalDTO dto){
		int result = 0;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			
			
			
		} catch (Exception e) {
			
			
		} finally{
			if(pstmt != null){
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		
		
		return result;
	}
	
}
