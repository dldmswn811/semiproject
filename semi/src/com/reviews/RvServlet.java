package com.reviews;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.util.MyServlet;
import com.util.MyUtil;


@WebServlet("/reviews/*")
public class RvServlet extends MyServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");

		String uri = req.getRequestURI();
		String cp = req.getContextPath(); 

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		if (info == null) {
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}

		if (uri.indexOf("listReviews.do") != -1) {
			listReviews(req, resp);
		} else if(uri.indexOf("created.do")!=-1) {
			rvCreatedForm(req,resp);
		}   else if(uri.indexOf("created_ok.do")!=-1) {
			rvCreatedSubmit(req, resp);
		}  else if(uri.indexOf("article.do")!=-1) {
			rvArticle(req, resp);
		} else if(uri.indexOf("update.do")!=-1) {
			rvUpdateForm(req, resp);
		} else if(uri.indexOf("update_ok.do")!=-1) {
			rvUpdateSubmit(req, resp);
		} else if(uri.indexOf("reply.do")!=-1) {
			rvReplyForm(req, resp);
		} else if(uri.indexOf("reply_ok.do")!=-1) {
			rvReplySubmit(req, resp);
		} else if(uri.indexOf("delete.do")!=-1) {
			rvDelete(req, resp);
		} else if(uri.indexOf("countLikeBoard.do")!=-1) {
			// �Խù� ���� ����
			countLikeBoard(req, resp);
		} else if(uri.indexOf("insertLikeBoard.do")!=-1) {
			// �Խù� ���� ����
			insertLikeBoard(req, resp);
		} else if(uri.indexOf("insertReply.do")!=-1) {
			// ��� �߰�
			insertReply(req, resp);
		} else if(uri.indexOf("listReply.do")!=-1) {
			// ��� ����Ʈ
			listReply(req, resp);
		} else if(uri.indexOf("deleteReply.do")!=-1) {
			// ��� ����
			deleteReply(req, resp);
		} else if(uri.indexOf("insertReplyAnswer.do")!=-1) {
			// ����� ��� �߰�
			insertReplyAnswer(req, resp);
		} else if(uri.indexOf("listReplyAnswer.do")!=-1) {
			// ����� ��� ����Ʈ
			listReplyAnswer(req, resp);
		} else if(uri.indexOf("deleteReplyAnswer.do")!=-1) {
			// ����� ��� ����
			deleteReplyAnswer(req, resp);
		} else if(uri.indexOf("countReplyAnswer.do")!=-1) {
			// ����� ��� ����
			countReplyAnswer(req, resp);
		}

	}
	
	
	
	protected void listReviews(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �Խù� ����Ʈ
		String cp = req.getContextPath();
		ReviewsDAO dao = new ReviewsDAO();
		MyUtil util = new MyUtil();
		
		String page = req.getParameter("page");
		int current_page = 1;
		if(page != null)
			current_page = Integer.parseInt(page);
		
		
		// ����Ʈ �˻�
		String searchKey = req.getParameter("searchKey");
		String searchValue = req.getParameter("searchValue");
		if(searchKey == null) {
			searchKey = "rv_title";
			searchValue = "";
		}
		
		// GET����� ��� ���ڵ�..
		if(req.getMethod().equalsIgnoreCase("GET")) {
			searchValue = URLDecoder.decode(searchValue, "UTF-8");
		}
		
		// ��ü ������ ����
		int reviewsCount;
		if(searchValue.length()==0)
			reviewsCount = dao.reviewsCount();
		else
			reviewsCount = dao.reviewsCount(searchKey, searchValue);
		
		
		// ��ü ������ ��
		int rows = 10;
		int total_page = util.pageCount(rows, reviewsCount);
		
		if(current_page > total_page)
			current_page = total_page;
		
		
		// �Խù� ������ ���۰� ���� ��ġ
		int start = (current_page-1)*rows+1;
		int end=current_page*rows;
		
		// �Խù� ��������
		List<ReviewsDTO> listReviews=null;
		if(searchValue.length()==0)
			listReviews=dao.listReviews(start, end);
		
		else 
			listReviews = dao.listReviews(start, end, searchKey, searchValue);
		
		// ����Ʈ �۹�ȣ �����
		int rv_listnum, n=0;
		Iterator<ReviewsDTO>it = listReviews.iterator();
		while(it.hasNext()) {
			ReviewsDTO dto = it.next();
			rv_listnum=reviewsCount-(start+n-1);
			dto.setRv_listnum(rv_listnum);
			n++;
		}
		
		String query = "";
		if(searchValue.length()!=0) {
			// �˻��� ��� �˻��� ���ڵ�
			searchValue = URLEncoder.encode(searchValue, "UTF-8");
			query ="searchKey=" + searchKey +
					"&searchValue=" + searchValue;
		}
		
		
		// ����¡ó��
		String listUrl = cp+"/reviews/listReviews.do";
		String articleUrl = cp+"/reviews/article.do?page="+current_page;
		if(query.length()!=0) {
			listUrl += "?"+query;
			articleUrl += "&" + query;
		}
		
		String paging = util.paging(current_page, total_page, listUrl);
		
		// �������� jsp�� �ѱ� �Ӽ�
		req.setAttribute("listReviews", listReviews);
		req.setAttribute("page", current_page);
		req.setAttribute("reviewsCount", reviewsCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("paging", paging);
		
		//jsp�� ������
		
		String path = "/WEB-INF/views/reviews/list.jsp";
		forward(req, resp, path);
		
		
	}
	
	protected void rvCreatedForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "created");
		String path="/WEB-INF/views/reviews/created.jsp";
		forward(req, resp, path);
	}
	
	protected void rvCreatedSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �Խù�����
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		String cp=req.getContextPath();
		
		ReviewsDAO dao = new ReviewsDAO();
		ReviewsDTO dto = new ReviewsDTO();
		
		// userId�� ���ǿ� ����� ����
		//dto.setMem_id(info.getUserId());
		
		// �Ķ����
		dto.setRv_title(req.getParameter("rv_title"));
		dto.setRv_content(req.getParameter("rv_content"));
		
		dao.insertReviews(dto);
		
		resp.sendRedirect(cp + "/reviews/listReviews.do");
		
	}
	
	protected void rvArticle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
	
	protected void rvUpdateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}

	protected void rvUpdateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
	}

	protected void rvReplyForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
	}

	protected void rvReplySubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
	}

	protected void rvDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
	
	protected void countLikeBoard(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
	}
	
	protected void insertLikeBoard(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
	
	protected void insertReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
	
	protected void listReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
	
	protected void deleteReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
	
	protected void insertReplyAnswer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
	
	protected void listReplyAnswer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
	
	protected void deleteReplyAnswer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
	
	protected void countReplyAnswer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
	


}
