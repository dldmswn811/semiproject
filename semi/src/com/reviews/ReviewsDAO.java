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
	public int insertReviews(ReviewsDTO dto, String mode) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		int seq;

		try {
			sql = "SELECT rv_seq.NEXTVAL FROM dual ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			seq = 0;
			if (rs.next())
				seq = rs.getInt(1);
			rs.close();
			pstmt.close();

			dto.setRv_num(seq);
			if (mode.equals("created")) { // 글쓰기 모드
				dto.setGroupNum(seq);
				dto.setOrderNo(0);
				dto.setDepth(0);
				dto.setParent(0);
			} else if (mode.equals("reply")) { // 답변모드
				updateOderNo(dto.getGroupNum(), dto.getOrderNo());

				dto.setDepth(dto.getDepth() + 1);
				dto.setOrderNo(dto.getOrderNo() + 1);
			}

			sql = "INSERT INTO reviews( ";
			sql += " rv_num, mem_id, rv_title, rv_content, ";
			sql += " groupNum, depth, orderNo, parent) ";
			sql += " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getRv_num());
			pstmt.setString(2, dto.getMem_id());
			pstmt.setString(3, dto.getRv_title());
			pstmt.setString(4, dto.getRv_content());
			pstmt.setInt(5, dto.getGroupNum());
			pstmt.setInt(6, dto.getDepth());
			pstmt.setInt(7, dto.getOrderNo());
			pstmt.setInt(8, dto.getParent());

			result = pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	// 답변일 경우 orderNo 변경
	private int updateOderNo(int groupNum, int orderNo) {
		int result = 0;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();

		sb.append("UPDATE reviews SET orderNo=orderNo+1 WHERE groupNum= ? AND orderNo > ? ");

		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, groupNum);
			pstmt.setInt(2, orderNo);
			result = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
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

			rs = pstmt.executeQuery();
			if (rs.next())
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

			if (searchKey.equals("rv_date")) { 
				searchValue = searchValue.replaceAll("-", "");
				sb.append("SELECT NVL(COUNT(*), 0) FROM reviews r JOIN member m ON r.mem_id=m.mem_id WHERE TO_CHAR(rv_date, 'YYYYMMDD') = ? ");
			} else if(searchKey.equals("mem_id")) {
				sb.append("SELECT NVL(COUNT(*), 0) FROM reviews r JOIN member m ON r.mem_id=m.mem_id WHERE INSTR(mem_id, ?) = 1 ");
			} else {
				sb.append("SELECT NVL(COUNT(*), 0) FROM reviews r JOIN member m ON r.mem_id=m.mem_id WHERE INSTR(" +searchKey +", ?) >= 1 ");
			}
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, searchValue);

			rs = pstmt.executeQuery();
			if (rs.next())
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

	// 리뷰게시물 리스트
	public List<ReviewsDTO> listReviews(int start, int end) {
		List<ReviewsDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();

		try {
			sb.append("SELECT *FROM (");
			sb.append("		SELECT ROWNUM rnum, tb.* FROM ( ");
			sb.append("			SELECT rv_num, r.mem_id, mem_name, rv_title ");
			sb.append("				,groupNum, orderNo, depth, rv_cnt ");
			sb.append("			 	, TO_CHAR(rv_date, 'YYYY-MM-DD') rv_date ");
			sb.append(" 			FROM reviews r JOIN member m ON r.mem_id=m.mem_id  ");
			sb.append(" 		ORDER BY groupNum DESC, orderNo ASC  ");
			sb.append(" 	) tb WHERE ROWNUM <= ?  ");
			sb.append(" ) WHERE rnum >= ?  ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				ReviewsDTO dto = new ReviewsDTO();

				dto.setRv_num(rs.getInt("rv_num"));
				dto.setMem_id(rs.getString("mem_id"));
				dto.setMem_name(rs.getString("mem_name"));
				dto.setRv_title(rs.getString("rv_title"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setRv_cnt(rs.getInt("rv_cnt"));
				dto.setRv_date(rs.getString("rv_date"));

				list.add(dto);
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

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}

		return list;
	}

	// 리뷰게시물 리스트(검색시)
	public List<ReviewsDTO> listReviews(int start, int end, String searchKey, String searchValue) {
		List<ReviewsDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();

		try {
			sb.append("SELECT *FROM (");
			sb.append("		SELECT ROWNUM rnum, tb.* FROM ( ");
			sb.append("			SELECT rv_num, r.mem_id, mem_name, rv_title ");
			sb.append("			,groupNum, orderNo, depth, rv_cnt ");
			sb.append("			 	, TO_CHAR(rv_date, 'YYYY-MM-DD') rv_date ");
			sb.append(" 			FROM reviews r JOIN member m ON r.mem_id=m.mem_id  ");
			if (searchKey.equals("rv_date")) {
				searchValue = searchValue.replaceAll("-", "");
				sb.append("  WHERE TO_CHAR(rv_date, 'YYYYMMDD') = ? ");
			} else if (searchKey.equals("mem_name")) {
				sb.append("	WHERE INSTR(mem_name, ?) = 1 ");
			} else {
				sb.append("	 WHERE INSTR(" + searchKey + ", ?) >= 1");
			}
			sb.append(" 		ORDER BY groupNum DESC, orderNo ASC  ");
			sb.append(" 	) tb WHERE ROWNUM <= ?  ");
			sb.append(" ) WHERE rnum >= ?  ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, searchValue);
			pstmt.setInt(2, end);
			pstmt.setInt(3, start);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				ReviewsDTO dto = new ReviewsDTO();

				dto.setRv_num(rs.getInt("rv_num"));
				dto.setMem_id(rs.getString("mem_id"));
				dto.setMem_name(rs.getString("mem_name"));
				dto.setRv_title(rs.getString("rv_title"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setRv_cnt(rs.getInt("rv_cnt"));
				dto.setRv_date(rs.getString("rv_date"));

				list.add(dto);
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

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}

		return list;
	}

	// 해당리뷰 보기
	public ReviewsDTO readReviews(int rv_num) {
		ReviewsDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();

		try {
			sb.append("SELECT rv_num, r.mem_id, mem_name, rv_title, rv_content, rv_date, rv_cnt ");
			sb.append("  ,groupNum, depth, orderNo, parent ");
			sb.append("  FROM reviews r ");
			sb.append("  JOIN member m ON r.mem_id = m.mem_id ");
			sb.append("  WHERE rv_num = ? ");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setInt(1, rv_num);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new ReviewsDTO();
				dto.setRv_num(rs.getInt("rv_num"));
				dto.setMem_id(rs.getString("mem_id"));
				dto.setMem_name(rs.getString("mem_name"));
				dto.setRv_title(rs.getString("rv_title"));
				dto.setRv_content(rs.getString("rv_content"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setParent(rs.getInt("parent"));
				dto.setRv_cnt(rs.getInt("rv_cnt"));
				dto.setRv_date(rs.getString("rv_date"));

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("여기서 에러");
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
	public ReviewsDTO preReadDto(int groupNum, int orderNo, String searchKey, String searchValue) {
		ReviewsDTO dto = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();

		try {
			if (searchValue != null && searchValue.length() != 0) {
				sb.append("SELECT ROWNUM, tb.* FROM ( ");
				sb.append("		SELECT rv_num, rv_title   ");
				sb.append("			FROM reviews r	");
				sb.append("			JOIN member m ON r.mem_id=m.mem_id	");

				if (searchKey.equals("rv_date")) {
					searchValue = searchValue.replaceAll("-", "");
					sb.append("	WHERE (TO_CHAR(rv_date, 'YYYYMMDD') = ?) AND ");

				} else if (searchKey.equals("mem_name")) {
					sb.append("	WHERE (INSTR(mem_name, ?) = 1) AND  ");

				} else {
					sb.append("	WHERE (INSTR(" + searchKey + ", ?) >=1) AND ");
				}
				sb.append("  	((groupNum = ? AND orderNo < ?) ");
				sb.append("		OR (groupNum > ? ))");
				sb.append("		ORDER BY groupNum ASC, orderNo DESC ");
				sb.append("	 ) tb WHERE ROWNUM = 1 ");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setString(1, searchValue);
				pstmt.setInt(2, groupNum);
				pstmt.setInt(3, orderNo);
				pstmt.setInt(4, groupNum);

			} else {
				sb.append("SELECT ROWNUM, tb.* FROM ( ");
				sb.append("	SELECT rv_num, rv_title FROM reviews r ");
				sb.append("		JOIN member m ON r.mem_id=m.mem_id ");
				sb.append("		WHERE (groupNum = ? AND orderNo < ?) ");
				sb.append("		OR (groupNum > ?) ");
				sb.append("		ORDER BY groupNum ASC, orderNo DESC ");
				sb.append("	  ) tb WHERE ROWNUM = 1 ");

				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, groupNum);
				pstmt.setInt(2, orderNo);
				pstmt.setInt(3, groupNum);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new ReviewsDTO();
				dto.setRv_num(rs.getInt("rv_num"));
				dto.setRv_title(rs.getString("rv_title"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("?");
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
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

	// 다음 글
	public ReviewsDTO nextReadDto(int groupNum, int orderNo, String searchKey, String searchValue) {
		ReviewsDTO dto = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();

		try {

			if (searchValue != null && searchValue.length() != 0) {
				sb.append("SELECT ROWNUM, tb.* FROM ( ");
				sb.append("		SELECT rv_num, rv_title   ");
				sb.append("	 		FROM reviews r  ");
				sb.append("			JOIN member m ON r.mem_id=m.mem_id ");

				if (searchKey.equals("rv_date")) {
					searchValue = searchValue.replaceAll("-", "");
					sb.append("	WHERE (TO_CHAR(rv_date, 'YYYYMMDD') = ?) AND ");

				} else if (searchKey.equals("mem_name")) {
					sb.append("	WHERE (INSTR(mem_name, ?) = 1) AND  ");

				} else {
					sb.append("	WHERE (INSTR(" + searchKey + ", ?) >=1) AND ");
				}
				sb.append("  	((groupNum = ? AND orderNo > ?) ");
				sb.append("		OR (groupNum < ? ))");
				sb.append("		ORDER BY groupNum DESC, orderNo ASC ");
				sb.append("	 ) tb WHERE ROWNUM = 1 ");

				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setString(1, searchValue);
				pstmt.setInt(2, groupNum);
				pstmt.setInt(3, orderNo);
				pstmt.setInt(4, groupNum);

			} else {
				sb.append(" SELECT ROWNUM, tb.* FROM ( ");
				sb.append("		SELECT rv_num, rv_title FROM reviews r ");
				sb.append("		JOIN member m ON r.mem_id=m.mem_id ");
				sb.append("		WHERE (groupNum = ? AND orderNo >?) ");
				sb.append("			OR (groupNum < ? ) ");
				sb.append("			ORDER BY groupNum DESC,orderNo ASC ");
				sb.append("		) tb WHERE ROWNUM = 1");

				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, groupNum);
				pstmt.setInt(2, orderNo);
				pstmt.setInt(3, groupNum);

			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
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

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}

		return dto;
	}

	// 조회수 증가
	public int updateReviewCnt(int rv_num) {
		int result = 0;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();

		try {

			sb.append("UPDATE reviews SET rv_cnt=rv_cnt+1 WHERE rv_num = ? ");
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, rv_num);
			result = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}

		return result;
	}

	// 리뷰 수정
	public int updateReview(ReviewsDTO dto, String mem_id) {
		int result = 0;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();

		try {

			sb.append("UPDATE reviews SET rv_title=?, rv_content=? WHERE rv_num=? AND mem_id=? ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, dto.getRv_title());
			pstmt.setString(2, dto.getRv_content());
			pstmt.setInt(3, dto.getRv_num());
			pstmt.setString(4, mem_id);
			result = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}

		}

		return result;
	}

	// 리뷰 삭제
	public int deleteReview(int rv_num) {
		int result = 0;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();

		try {
			sb.append("DELETE FROM reviews WHERE rv_num IN (SELECT rv_num FROM reviews START WITH  rv_num = ? CONNECT BY PRIOR rv_num = parent)");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, rv_num);
			result = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
				}
			}
		}

		return result;
	}
}
