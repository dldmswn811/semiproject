package com.reviews;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class ReviewsDAO {
	private Connection conn = DBConn.getConnection();
	

	// 리뷰등록
	public int insertReviews(ReviewsDTO dto){
		int result =0;
		PreparedStatement pstmt=null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("INSERT INTO reviews(rv_num, mem_id, rv_title, rv_content)");
			sb.append(" VALUES (rv_seq.NEXTVAL, ?, ?, ?)");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, dto.getMem_id());
			pstmt.setString(2, dto.getRv_title());
			pstmt.setString(3, dto.getRv_content());
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null)
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
		}
		
		
		return result;
	}
	
	
	// 데이터의 개수 
	public int reviewsCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("SELECT NVL(COUNT(*),0) FROM reviews");
			pstmt = conn.prepareStatement(sb.toString());
			
			rs=pstmt.executeQuery();
			if(rs.next())
				result=rs.getInt(1);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			
			if(pstmt != null) {
				try{
				pstmt.close();
				} catch (Exception e) {
				}
			}
		}
		
		return result;
	}
	
	
	// 데이터의 개수 (검색시)
	public int reviewsCount(String searchKey, String searchValue) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			
			sb.append("SELECT NVL(COUNT(*), 0) ");
			sb.append(" FROM reviews r JOIN member m ON r.mem_id=m.mem_id ");
			if(searchKey.equals("mem_name")) {
				sb.append(" WHERE INSTR(mem_name, ?) = 1 ");
			} else if(searchKey.equals("created")) {
				searchValue = searchValue.replaceAll("-", "");
				sb.append(" WHERE TO_CHAR(rv_date, 'YYYYMMDD') = ? ");
			} else {
				sb.append(" WHERE INSTR(" +searchKey+", ?) > = 1 ");
			}
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, searchValue);
			
			
			rs = pstmt.executeQuery();
			if(rs.next())
				result = rs.getInt(1);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
	}
	
	
	
	//  리뷰게시물 리스트
	public List<ReviewsDTO> listReviews(int start, int end) {
		List<ReviewsDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs= null;
		StringBuffer sb= new StringBuffer();
		
		try{
			sb.append("SELECT *FROM (");
			sb.append("		SELECT ROWNUM rnum, tb.* FROM ( ");
			sb.append("			SELECT rv_num, r.mem_id, mem_name, rv_title ");
			sb.append("			 	, TO_CHAR(rv_date, 'YYYY-MM-DD') rv_date ");
			sb.append("			 	, rv_cnt ");
			sb.append(" 			FROM reviews r JOIN member m ON r.mem_id=m.mem_id  ");
			sb.append(" 		ORDER BY rv_num DESC  ");
			sb.append(" 	) tb WHERE ROWNUM <= ?  ");
			sb.append(" ) WHERE rnum >= ?  ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ReviewsDTO dto = new ReviewsDTO();
				
				dto.setRv_num(rs.getInt("rv_num"));
				dto.setMem_id(rs.getString("mem_id"));
				dto.setMem_name(rs.getString("mem_name"));
				dto.setRv_title(rs.getString("rv_title"));
				dto.setRv_cnt(rs.getInt("rv_cnt"));
				dto.setRv_date(rs.getString("rv_date"));

				
				list.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return list;
	}
	
	
	// 리뷰게시물 리스트(검색시)
	public List<ReviewsDTO> listReviews(int start, int end, String searchKey, String searchValue){
		List<ReviewsDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("SELECT *FROM (");
			sb.append("		SELECT ROWNUM rnum, tb.* FROM ( ");
			sb.append("			SELECT rv_num, r.mem_id, mem_name, rv_title ");
			sb.append("			 	, TO_CHAR(rv_date, 'YYYY-MM-DD') rv_date ");
			sb.append("			 	, rv_cnt ");
			sb.append(" 			FROM reviews r JOIN member m ON r.mem_id=m.mem_id  ");
			if(searchKey.equals("mem_id")) {
				sb.append("	WHERE INSTR(mem_name, ?) = 1 "); 
			} else if(searchKey.equals("rv_date")) {
				searchValue = searchValue.replaceAll("-", ""); 
				sb.append("  WHERE TO_CHAR(rv_date, 'YYYYMMDD') = ? ");
			} else {
				sb.append("	 WHERE INSTR("+searchKey+", ?) >= 1");
			}
			sb.append(" 		ORDER BY rv_num DESC  ");
			sb.append(" 	) tb WHERE ROWNUM <= ?  ");
			sb.append(" ) WHERE rnum >= ?  ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, searchValue);
			pstmt.setInt(2, end);
			pstmt.setInt(3, start);
			
			rs= pstmt.executeQuery();
			
			while(rs.next()) {
				ReviewsDTO dto= new ReviewsDTO();
				
				dto.setRv_num(rs.getInt("rv_num"));
				dto.setMem_id(rs.getString("mem_id"));
				dto.setMem_name(rs.getString("mem_name"));
				dto.setRv_title(rs.getString("rv_title"));
				dto.setRv_cnt(rs.getInt("rv_cnt"));
				dto.setRv_date(rs.getString("rv_date"));
				
				list.add(dto);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null){
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			
			if(pstmt != null){
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		
		return list;
	}
	
	
	
	// 조회수 증가
	public int updateReviewCnt(int rv_num) {
		int result = 0;
		PreparedStatement pstmt = null;
		StringBuffer sb= new StringBuffer();
		
		try {

			sb.append("UPDATE reviews SET rv_cnt=rv_cnt+1 WHERE rv_num = ? ");
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, rv_num);
			result = pstmt.executeUpdate();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		
		return result;
	}
	
	
	
	// 해당리뷰 보기
	public ReviewsDTO readReviews(int rv_num) {
		ReviewsDTO dto = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try{
			sb.append("");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return dto;
	}
	
	
	
	
	// 이전 글
	public ReviewsDTO preReadRv(int rv_num, String searchKey, String searchValue) {
		ReviewsDTO dto = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			if(searchValue != null && searchValue.length() !=0) {
				sb.append("SELECT ROWNUM, tb.* FROM ( ");
				sb.append("		SELECT rv_num, rv_title FROM reviews r  ");
				if(searchKey.equals("mem_name")){
					sb.append("	WHERE (INSTR(mem_name, ?) = 1)  ");
				} else if(searchKey.equals("rv_date")) {
					searchValue = searchValue.replaceAll("-", "");
					sb.append("	WHERE (TO_CHAR(rv_date, 'YYYYMMDD') = ?) ");
				} else {
					sb.append("	WHERE (INSTR(" +searchKey +", ?) >=1) " );
				}
				sb.append("  	AND(num > ?) ");
				sb.append("		ORDER BY rv_num ASC ");
				sb.append("	 ) tb WHERE ROWNUM = 1 ");
				
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setString(1, searchValue);
				pstmt.setInt(2, rv_num);
				
			} else {
				sb.append("SELECT ROWNUM, tb.* FROM ( ");
				sb.append("	SELECT rv_num, rv_title FROM reviews r ");
				sb.append("		JOIN member m ON r.mem_id=m.mem_id ");
				sb.append("		WHERE rv_num > ? ");
				sb.append("		ORDER BY rv_num ASC ");
				sb.append("	  ) tb WHERE ROWNUM = 1 ");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try{
					rs.close();
				} catch (Exception e) {
				}
			}
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}

			}
		}
		
		return dto;
	}
	
	// 다음 글
	public ReviewsDTO nextReadRv(int rv_num, String searchKey, String searchValue) {
		ReviewsDTO dto = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			
			if(searchValue != null && searchValue.length() !=0) {
				sb.append("SELECT ROWNUM, tb.* FROM ( ");
				sb.append("	SELECT rv_num, rv_title FROM reviews r  ");
				sb.append("	JOIN member m ON r.mem_id=m.mem_id ");
				
				if(searchKey.equals("mem_name")) {
					sb.append("	WHERE (INSTR(mem_name, ?) = 1 ");	
				} else if(searchKey.equals("rv_date")) {
					searchValue = searchValue.replaceAll("-", "");
					sb.append(" WHERE (TO_CHAR(rv_date, 'YYYYMMDD') = ?) ");
				} else {
					sb.append(" WHERE (INSTR(" +searchKey + ", ?) >=1) ");
				}
				sb.append("    AND (rv_num < ?) ");
				sb.append("    ORDER BY rv_num DESC ");
				sb.append("  ) tb WHERE ROWNUM = 1 ");
				
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setString(1, searchValue);
				pstmt.setInt(2, rv_num);
			} else {
				sb.append(" SELECT ROWNUM, tb.* FROM ( ");
				sb.append("		SELECT rv_num, rv_title, FROM reviews r ");
				sb.append("		JOIN member m ON r.mem_id=m.mem_id ");
				sb.append("		WHERE rv_num < ? ");
				sb.append("			ORDER BY rv_num DESC ");
				sb.append("		) tb WHERE ROWNUM = 1" );
				
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, rv_num);
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new ReviewsDTO();
				dto.setRv_num(rs.getInt("rv_num"));
				dto.setRv_title(rs.getString("rv_title"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			
			if (pstmt != null){
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		
		return dto;
	}
	
	

	
	
	// 리뷰 수정
	public int updateReview(ReviewsDTO dto, String mem_id) {
		int result = 0;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		
		try {

			sb.append("UPDATE reviews SET rv_title=?, rv_content=? WHERE rv_num=? AND mem_id");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setString(1, dto.getRv_title());
			pstmt.setString(2, dto.getRv_content());
			pstmt.setInt(3, dto.getRv_num());
			pstmt.setString(4, dto.getMem_id());
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null){
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		
		}
		
		
		return result;
	}
	
	
	
	
	// 리뷰 삭제
	public int deleteReview(int rv_num, String mem_id) {
		int result = 0;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			if(mem_id.equals("admin")) {
				sb.append("DELETE FROM reviews WHERE rv_num=?");
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, rv_num);
				result = pstmt.executeUpdate();
			} else {
				sb.append("DELETE FROM reviews WHERE rv_num=? AND mem_id=?");
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, rv_num);
				pstmt.setString(2, mem_id);
				result = pstmt.executeUpdate();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
				}
			}
		}
		
		return result;
	}
	
	
	
	//  리뷰 좋아요!
	public int insertLikeRv(int rv_num, String mem_id) {
		int result = 0;
		
		return result;
	}
	
	
	// 리뷰 좋아요 개수
	public int countLikeRv(int rv_num) {
		int result = 0;
		
		return result;
	}
	
	
	// 게시물 댓글, 답글 추가
	public int insertReplyRv(ReplyRvDTO dto) {
		int result = 0;
		PreparedStatement pstmt=null;
		StringBuffer sb= new StringBuffer();
		
		try {
			sb.append("INSERT INTO rereviews(r_rv_num, rv_num, mem_id, r_rv_content, r_rv_answer) ");
			sb.append(" VALUES (r_rv_seq.NEXTVAL, ?, ?, ?, ?) ");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setInt(1, dto.getRv_num());
			pstmt.setString(2, dto.getMem_id());
			pstmt.setString(3, dto.getR_rv_content());
			pstmt.setInt(4, dto.getRv_answer());
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		
		
		return result;
	}
	
	
	// 게시물 댓글 개수
	public int reReviewCnt(int rv_num) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			
			sb.append("SELECT NVL(COUNT(*), 0) FROM rereviews WHERE rv_num AND RV_answer=0 ");
			pstmt= conn.prepareStatement(sb.toString());
			pstmt.setInt(1, rv_num);
			
			rs=pstmt.executeQuery();
			if(rs.next())
				result= rs.getInt(1);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return result;
	}
	
	// 게시물 댓글 리스트
	public List<ReplyRvDTO> listReReviews(int r_rv_num, int start, int end) {
		List<ReplyRvDTO> list=new ArrayList<>();
		
		
		return list;
	
	}
	
	
	
	// 게시물의 댓글 삭제
	public int deleteReRv(int r_rv_num, String mem_id){
		int result =0;
		
		
		return result;
	}
	
	
	// 댓글의 답글 리스트
	public List<ReplyRvDTO> listRvAnswer(int rv_answer) {
		List<ReplyRvDTO> list=new ArrayList<>();
		
		return list;
	}
	
	
	//댓글의 답글 개수
	public int reRvAnswerCnt (int answer) {
		int result = 0;
		
		return result;
	}
	
	
	// 댓글의 답글 삭제
	public int deleteRvAnswer (int r_rv_num, String mem_id) {
		int result = 0;
		
		return result;
	}
	
	
}
