package com.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class AdminDAO {
	
	private Connection conn = DBConn.getConnection();
	
	// 대학 테이블 삽입
	public int insertCal(AdminDTO dto){
		int result = 0;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			
			sb.append("INSERT INTO cal(cal_code, cal_name, cal_add) VALUES(?,?,?)");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, dto.getCal_code());
			pstmt.setString(2, dto.getCal_name());
			pstmt.setString(3, dto.getCal_add());
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null){
				try {
					pstmt.close();					
				} catch (SQLException e2) {
				}
				
			}
		}		
		return result;		
	}
	
	// 대학 테이블 수정
	public int updateCal(AdminDTO dto){
		int result = 0;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			
			sb.append("UPDATE cal SET cal_name = ?, cal_add = ? WHERE cal_code = ?");
			
			pstmt = conn.prepareStatement(sb.toString());
						
			pstmt.setString(1, dto.getCal_name());
			pstmt.setString(2, dto.getCal_add());
			pstmt.setString(3, dto.getCal_code());
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null){
				try {
					pstmt.close();					
				} catch (SQLException e2) {
				}
				
			}
		}		
		return result;		
	}
	
	// 대학 테이블 삭제
	public int deleteCal(String cal_code){
		int result = 0;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			
			sb.append("DELETE FROM cal WHERE cal_code = ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, cal_code);
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null){
				try {
					pstmt.close();					
				} catch (SQLException e2) {
				}
				
			}
		}		
		return result;		
	}
	
	// 대학 테이블 갯수
	public int countCal(){
		int result = 0;
		
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		ResultSet rs = null;
		
		try {
			
			sb.append("SELECT NVL(COUNT(*), 0) FROM cal");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				result = rs.getInt(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null){
				try {
					pstmt.close();					
				} catch (SQLException e2) {
				}
				
			}
		}		
		return result;
	}
	
	// 대학 테이블 리스트
	public List<AdminDTO> listCal(int start, int end){
		List<AdminDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			
			sb.append("SELECT * FROM( ");
			sb.append(" SELECT ROWNUM rnum, tb.* FROM( ");
			sb.append(" SELECT cal_code, cal_name, cal_add FROM cal ");
			sb.append(" ORDER BY cal_name ASC");
			sb.append(" ) tb WHERE ROWNUM <= ?");
			sb.append(" ) WHERE rnum >= ? ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				AdminDTO dto = new AdminDTO();
				dto.setCal_code(rs.getString("cal_code"));
				dto.setCal_name(rs.getString("cal_name"));
				dto.setCal_add(rs.getString("cal_add"));
				
				list.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null){
				try {
					pstmt.close();					
				} catch (SQLException e2) {
				}
				
			}
			
			if(rs != null){
				try {
					rs.close();					
				} catch (SQLException e2) {
				}
				
			}
		}		
		return list;
	}
	
	// 대학 테이블 리드
	public AdminDTO readCal(String cal_code){
		AdminDTO dto = new AdminDTO();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			
			sb.append("SELECT cal_code, cal_name, cal_add FROM cal WHERE cal_code = ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, cal_code);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				dto.setCal_code(rs.getString("cal_code"));
				dto.setCal_name(rs.getString("cal_name"));
				dto.setCal_add(rs.getString("cal_add"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null){
				try {
					pstmt.close();					
				} catch (SQLException e2) {
				}
				
			}
			
			if(rs != null){
				try {
					rs.close();					
				} catch (SQLException e2) {
				}
				
			}
		}
		
		return dto;
	}
	
	// 관리자 임명 및 권한 수정
	public int updateAdmin(AdminDTO dto){
		int result = 0;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			
			sb.append("UPDATE member SET mem_gt = ? WHERE mem_id = ?");
			
			pstmt = conn.prepareStatement(sb.toString());
						
			pstmt.setInt(1, dto.getMem_gt());
			pstmt.setString(2, dto.getMem_id());
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null){
				try {
					pstmt.close();					
				} catch (SQLException e2) {
				}
				
			}
		}		
		return result;		
	}
	
	// 회원 테이블 데이터 갯수
	public int countMember(){
		int result = 0;
		
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		ResultSet rs = null;
		
		try {
			
			sb.append("SELECT NVL(COUNT(*), 0) FROM member");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				result = rs.getInt(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null){
				try {
					pstmt.close();					
				} catch (SQLException e2) {
				}
				
			}
		}		
		return result;
	}
	
	// 회원 테이블 리스트 (검색 x)
	public List<AdminDTO> listMember(int start, int end){
		List<AdminDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
				
			sb.append("SELECT rnum, mem_id, mem_name, mem_gt, NVL(cal_code, '-'), NVL(cal_code, '-') FROM( ");
			sb.append(" SELECT ROWNUM rnum, tb.* FROM( ");
			sb.append(" SELECT mem_id, mem_name, mem_gt, m.cal_code, cal_name FROM member m JOIN cal c ON m.cal_code = c.cal_code ");
			sb.append(" ORDER BY mem_gt DESC");
			sb.append(" ) tb WHERE ROWNUM <= ?");
			sb.append(" ) WHERE rnum >= ? ");
				
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				AdminDTO dto = new AdminDTO();
				dto.setMem_id(rs.getString("mem_id"));
				dto.setMem_name(rs.getString("mem_name"));
				dto.setMem_gt(rs.getInt("mem_gt"));
				dto.setCal_code(rs.getString("cal_code"));
				dto.setCal_name(rs.getString("cal_name"));
				
				list.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null){
				try {
					pstmt.close();					
				} catch (SQLException e2) {
				}
				
			}
			
			if(rs != null){
				try {
					rs.close();					
				} catch (SQLException e2) {
				}
					
			}
		}		
		return list;
	}
	
	// 회원 테이블 리드
	public AdminDTO readMember(String mem_id){
		AdminDTO dto = new AdminDTO();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			
			sb.append("SELECT mem_id, mem_name, mem_gt FROM member WHERE mem_id = ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, mem_id);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				dto.setMem_id(rs.getString("mem_id"));
				dto.setMem_name(rs.getString("mem_name"));
				dto.setMem_gt(rs.getInt("mem_gt"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null){
				try {
					pstmt.close();					
				} catch (SQLException e2) {
				}
				
			}
			
			if(rs != null){
				try {
					rs.close();					
				} catch (SQLException e2) {
				}
				
			}
		}
		
		return dto;
	}
	
}
