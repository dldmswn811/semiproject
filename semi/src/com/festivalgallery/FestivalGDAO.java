package com.festivalgallery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;


public class FestivalGDAO {
	private Connection conn=DBConn.getConnection();
	
	public int insertFestivalGallery(FestivalGDTO dto) {
		int result=0;
		PreparedStatement pstmt=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("INERT INTO FESTIVALGALLERY(fg_num, fg_title, fg_content, ");
			sb.append(" fg_imagename, mem_id)");
			sb.append(" VALUES(fg_seq.NEXTVAL, ? ,? ,? ,? ) ");
			
			pstmt=conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, dto.getFg_title());
			pstmt.setString(2, dto.getFg_content());
			pstmt.setString(3, dto.getFg_imagename());
			pstmt.setString(4, dto.getMem_id());

			result=pstmt.executeUpdate();
			pstmt.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int dataCount() {
		int result=0;
		PreparedStatement pstmt=null;
		StringBuffer sb=new StringBuffer();
		ResultSet rs=null;
		
		try {
			sb.append("SELECT COUNT(*) FROM FESTIVALGALLERY ");
			 
	         pstmt = conn.prepareStatement(sb.toString());
	         rs=pstmt.executeQuery();
	         
	         if(rs.next())
	            result=rs.getInt(1);
	         
	         rs.close();
	         pstmt.close();
	         
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public List<FestivalGDTO> listFestivalG(int start, int end) {
		List<FestivalGDTO> list=new ArrayList<>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("SELECT * FROM( ");
			sb.append("  SELECT ROWNUM RNUM, tb.*FROM (");
			sb.append("     SELECT fg_num, fg.mem_id, fg_title, fg_imagename, fg_content, fg_created  ");
			sb.append("  FROM festivalgallery fg ");
			sb.append(" JOIN member m fg.mem_id=m.mem_id ");
			sb.append(" ) tb WHERE ROWNUM <=? ");
			sb.append(" ) WHERE RNUM >=? ");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				FestivalGDTO dto=new FestivalGDTO();
				dto.setFg_num(rs.getInt("fg_num"));
				dto.setMem_id(rs.getString("mem_id"));
				dto.setFg_title(rs.getString("fg_title"));
				dto.setFg_imagename(rs.getString("fg_imagename"));
				dto.setFg_content(rs.getString("fg_content"));
				dto.setFg_created(rs.getString("fg_created"));
				
				list.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
}
















