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

		if (uri.indexOf("list.do") != -1) {
			listReviews(req, resp);
		} else if(uri.indexOf("created.do")!=-1) {
			rvCreatedForm(req,resp);
		} else if(uri.indexOf("created_ok.do")!=-1) {
			rvCreatedSubmit(req, resp);
		} else if(uri.indexOf("article.do")!=-1) {
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
		int listNum, n=0;
		Iterator<ReviewsDTO>it = listReviews.iterator();
		while(it.hasNext()) {
			ReviewsDTO dto = it.next();
			listNum=reviewsCount-(start+n-1);
			dto.setListNum(listNum);
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
		String listUrl = cp+"/reviews/list.do";
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
	
	
	// �۾�����
	protected void rvCreatedForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "created");
		String path="/WEB-INF/views/reviews/created.jsp";
		forward(req, resp, path);
	}

	
	// ������
	protected void rvCreatedSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp=req.getContextPath();
		
		HttpSession session=req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		
		ReviewsDAO dao = new ReviewsDAO();
		ReviewsDTO dto = new ReviewsDTO();
		
		// mem_id�� ���ǿ� ����� ����
		dto.setMem_id(info.getMem_id());
		
		// �Ķ����
		dto.setRv_title(req.getParameter("rv_title"));
		dto.setRv_content(req.getParameter("rv_content"));
		
		dao.insertReviews(dto, "created");
		
		resp.sendRedirect(cp + "/reviews/list.do");
		
	}
	
	
	// �Խù� ����
	protected void rvArticle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		ReviewsDAO dao = new ReviewsDAO();
		
		int rv_num=Integer.parseInt(req.getParameter("rv_num"));
		String page= req.getParameter("page");
		String searchKey = req.getParameter("searchKey");
		String searchValue = req.getParameter("searchValue");
		if(searchKey ==null) {
			searchKey ="rv_title";
			searchValue="";
		}
		
		searchValue = URLDecoder.decode(searchValue, "UTF-8");
		
		//��ȸ�� ����
		dao.updateReviewCnt(rv_num);
		
		//�Խù� ��������
		ReviewsDTO dto=dao.readReviews(rv_num);
		if(dto==null) {
			resp.sendRedirect(cp+"/reviews/list.do?page="+page);
			return;
		}
		
		dto.setRv_content(dto.getRv_content().replaceAll("\n", "<br>"));
		
		// ������, ������
		ReviewsDTO preReadDto=dao.preReadDto(dto.getGroupNum(), dto.getOrderNo(), searchKey, searchValue);
		ReviewsDTO nextReadDto=dao.nextReadDto(dto.getGroupNum(), dto.getOrderNo(), searchKey, searchValue);
		
		// ����Ʈ�� ������, �����ۿ��� ����� �Ķ����
		String query="page="+page;
		if(searchValue.length()!=0) {
			query+="&searchKey="+searchKey +"&searchValue="+URLEncoder.encode(searchValue,"utf-8");
		}
		
		//jsp�� ������ �Ӽ�
		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		req.setAttribute("query", query);
		req.setAttribute("preReadDto", preReadDto);
		req.setAttribute("nextReadDto", nextReadDto);
		
		//������
		String path="/WEB-INF/views/reviews/article.jsp";
		forward(req, resp, path);
		
	}
	
	protected void rvUpdateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		String cp=req.getContextPath();
		ReviewsDAO dao = new ReviewsDAO();
		
		String page = req.getParameter("page");
		int rv_num=Integer.parseInt(req.getParameter("rv_num"));
		ReviewsDTO dto=dao.readReviews(rv_num);
		
		if(dto==null) {
			resp.sendRedirect(cp+"/reviews/list.do?page="+page);
			return;
		}
		
		if(! dto.getMem_id().equals(info.getMem_id())) {
			resp.sendRedirect(cp+"/reviews/list.do?page="+page);
			return;
		}
		
		
		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		req.setAttribute("mode", "update");
		
		String path="/WEB-INF/views/reviews/created.jsp";
		forward(req, resp, path);
		
	}

	//�����Ϸ�
	protected void rvUpdateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		String cp=req.getContextPath();
		ReviewsDAO dao= new ReviewsDAO();
		
		String page=req.getParameter("page");
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/reviews/list.do?page="+page);
			return;
		}
		
		ReviewsDTO dto=new ReviewsDTO();
		dto.setRv_num(Integer.parseInt(req.getParameter("rv_num")));
		dto.setRv_title(req.getParameter("rv_title"));
		dto.setRv_content(req.getParameter("rv_content"));
		
		dao.updateReview(dto, info.getMem_id());
		
		resp.sendRedirect(cp+"/reviews/list.do?page="+page);
		
	}

	
	//�亯 ��
	protected void rvReplyForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp=req.getContextPath();
		ReviewsDAO dao = new ReviewsDAO();
		
		int rv_num=Integer.parseInt(req.getParameter("rv_num"));
		String page=req.getParameter("page");
		ReviewsDTO dto=dao.readReviews(rv_num);
		if(dto==null) {
			resp.sendRedirect(cp+"/reviews/list.do?page="+page);
			return;
		}
		
		dto.setRv_content("["+dto.getRv_title()+"] �� ���� �亯�Դϴ�. \n");
		
		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		req.setAttribute("mode", "reply");
		
		String path="/WEB-INF/views/reviews/created.jsp";
		forward(req, resp, path);
		
	}

	
	//�亯�Ϸ�
	protected void rvReplySubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String cp= req.getContextPath();
		ReviewsDAO dao = new ReviewsDAO();
		
		String page = req.getParameter("page");
		
		ReviewsDTO dto = new ReviewsDTO();
		dto.setMem_id(info.getMem_id());
		
		dto.setRv_title(req.getParameter("rv_title"));
		dto.setRv_content(req.getParameter("rv_content"));
		dto.setGroupNum(Integer.parseInt(req.getParameter("groupNum")));
		dto.setDepth(Integer.parseInt(req.getParameter("depth")));
		dto.setOrderNo(Integer.parseInt(req.getParameter("orderNo")));
		dto.setParent(Integer.parseInt(req.getParameter("parent")));
		
		dao.insertReviews(dto, "reply");
		
		resp.sendRedirect(cp+"/reviews/list.do?page="+page);
		
	}

	
	// ����
	protected void rvDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String cp= req.getContextPath();
		ReviewsDAO dao= new ReviewsDAO();
		
		String page = req.getParameter("page");
		int rv_num = Integer.parseInt(req.getParameter("rv_num"));
		ReviewsDTO dto=dao.readReviews(rv_num);
		
		if(dto==null) {
			resp.sendRedirect(cp+"/reviews/list.do?page="+page);
			return;
		}
		
		// �Խù��� �ø� ����ڳ� admin�� �ƴϸ� ������ �Ұ���...
		if (! dto.getMem_id().equals(info.getMem_id()) && ! info.getMem_id().equals("admin")) {
			resp.sendRedirect(cp+"/reviews/list.do?page="+page);
			return;
		}
		
		dao.deleteReview(rv_num);
		resp.sendRedirect(cp+"/reviews/list.do?page="+page);
		
	}

	
}
