package com.member;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class MemberDAO {
	private Connection conn = DBConn.getConnection();
	
	public List<MemberDTO> listcalcode() {
		List<MemberDTO> list=new ArrayList<MemberDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("   SELECT cal_code, cal_name, cal_add");
			sb.append("   	from cal ");
			sb.append("	    ORDER BY cal_code");

			pstmt=conn.prepareStatement(sb.toString());
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				MemberDTO dto=new MemberDTO();
				
				dto.setCal_code(rs.getString("cal_code"));
				dto.setCal_name(rs.getString("cal_name"));
				dto.setCal_add(rs.getString("cal_add"));
				
				list.add(dto);
				 
			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return list;
	}
	
	public MemberDTO readMember(String mem_id) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("SELECT mb.mem_id, mem_name, mem_pw,");
			sb.append("      mem_gt, mb.cal_code,");
			sb.append("      mem_add, mem_tel, mem_gen, mem_email");
			sb.append("      FROM member mb");
			sb.append("		 JOIN cal c");
			sb.append("		 ON mb.cal_code = c.cal_code");
			sb.append("      LEFT OUTER JOIN memsub ms ");
			sb.append("      ON mb.mem_id = ms.mem_id");
			sb.append("      WHERE mb.mem_id=?");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, mem_id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MemberDTO();
				dto.setMem_id(rs.getString("mem_id"));
				dto.setMem_name(rs.getString("mem_name"));
				dto.setMem_pw(rs.getString("mem_pw"));
				dto.setMem_gt(rs.getInt("mem_gt"));
				dto.setCal_code(rs.getString("cal_code"));
				dto.setMem_add(rs.getString("mem_add"));
				dto.setMem_tel(rs.getString("mem_tel"));
				dto.setMem_gen(rs.getString("mem_gen"));
				dto.setMem_email(rs.getString("mem_email"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return dto;
	}
	
	// 회원추가
	public int insertMember(MemberDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();

		try {
			sb.append(" INSERT ALL");
			sb.append(" INTO member(mem_id, mem_name, mem_pw, cal_code)");
			sb.append(" VALUES(?,?,?,?)");
			sb.append(" INTO memsub(mem_id, mem_add, mem_gen, mem_tel, mem_email)");
			sb.append(" VALUES(?,?,?,?,?)");
			sb.append(" SELECT * FROM dual");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, dto.getMem_id());
			pstmt.setString(2, dto.getMem_name());
			pstmt.setString(3, dto.getMem_pw());
			pstmt.setString(4, dto.getCal_code());
			pstmt.setString(5, dto.getMem_id());
	        pstmt.setString(6, dto.getMem_add());
			pstmt.setString(7, dto.getMem_gen());
			dto.setMem_tel(dto.getMem_tel1()+"-"+dto.getMem_tel2()+"-"+dto.getMem_tel3());
				pstmt.setString(8, dto.getMem_tel());
			dto.setMem_email(dto.getMem_email1()+"@"+dto.getMem_email2());
				pstmt.setString(9, dto.getMem_email());
			
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}
	
	// 회원수정
	public int updateMember(MemberDTO dto) {
		int result = 0;

		return result;
	}

	// 회원삭제
	public int deleteMember(String userId) {
		int result = 0;

		return result;
	}
}
