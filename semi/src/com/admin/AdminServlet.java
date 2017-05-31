package com.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.util.MyServlet;
import com.util.MyUtil;

@WebServlet("/admin/*")
public class AdminServlet extends MyServlet {

	private static final long serialVersionUID = 1L;
	
	private SessionInfo info = null;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		String uri = req.getRequestURI();
		
		HttpSession session = req.getSession();
		
		info = (SessionInfo)session.getAttribute("member");
		
		if(info == null){
			forward(req, resp, "/WEB-INF/views/member/login.jsp");
			return;
		}
		
		if(info.getMemRoll() < 2){
			forward(req, resp, "/WEB-INF/views/main/main.jsp");
			return;
		}
		
		if(uri.indexOf("uniList.do") != -1){
			list(req, resp);
		}
		else if(uri.indexOf("unicreated.do") != -1){
			createdForm(req, resp);
		}
		else if(uri.indexOf("unicreated_ok.do") != -1){
			createdSubmit(req, resp);
		}		
		else if(uri.indexOf("uniupdate.do") != -1){
			updateForm(req, resp);
		}
		else if(uri.indexOf("uniupdate_ok.do") != -1){
			updateSubmit(req, resp);
		}		
		else if(uri.indexOf("delete.do") != -1){
			delete(req, resp);
		}		
		
	}
	
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		AdminDAO dao = new AdminDAO();
		MyUtil myUtil = new MyUtil();
		
		//현재 path를 저장
		String cp = req.getContextPath();
		
		//req page값을 저장
		String page = req.getParameter("page");
		
		//현재 페이지 변경
		int current_page = 1;
		if(page != null){
			current_page = Integer.parseInt(page);
		}
		
		//한페이지에 몇개씩 출력할건지
		int rows = 5;
		
		//데이터의 갯수 받아오기
		int dataCount = dao.countCal();
		
		// total-page구하기
		int total_page = myUtil.pageCount(rows, dataCount);
		
		if(total_page < current_page){
			current_page = total_page;
		}
		
		//게시물을 가져올 시작과 끝위치		
		int start = (current_page-1)*rows + 1;
		int end = current_page*rows;
		
		List<AdminDTO> list = dao.listCal(start, end);
		
		
		// 글리스트 주소
		String listUrl = cp + "/admin/uniList.do";
		
		// 페이징처리
		String paging = myUtil.paging(current_page, total_page, listUrl);
		
		// 포워딩페이지에 넘길 데이터
		req.setAttribute("list", list);
		req.setAttribute("paging", paging);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("listUrl", listUrl);		
		
		forward(req, resp, "/WEB-INF/views/admin/unilist.jsp");
	}
	protected void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/admin/unicreated.jsp");
	}
	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String cp = req.getContextPath();
		
		AdminDTO dto = new AdminDTO();
		AdminDAO dao = new AdminDAO();
		
		dto.setCal_code(req.getParameter("cal_code"));
		dto.setCal_name(req.getParameter("cal_name"));
		dto.setCal_add(req.getParameter("cal_add"));
		
		dao.insertCal(dto);
		
		resp.sendRedirect(cp + "/admin/uniList.do");		
	}
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String cp = req.getContextPath();
		
		String cal_code = req.getParameter("cal_code");
		String page = req.getParameter("page");
		
		AdminDAO dao = new AdminDAO();
		AdminDTO dto = dao.readCal(cal_code);
		
		if(dto == null || (info.getMemRoll() < 2)){			
			resp.sendRedirect(cp + "/admin/unilist.do?page=" + page);
			return;
		}
		
		req.setAttribute("mode", "update");
		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		
		forward(req, resp, "/WEB-INF/views/admin/unicreated.jsp");
	}
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AdminDAO dao = new AdminDAO();
		AdminDTO dto = new AdminDTO();
		
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		
		dto.setCal_code(req.getParameter("cal_code"));
		dto.setCal_name(req.getParameter("cal_name"));
		dto.setCal_add(req.getParameter("cal_add"));
		
		dao.updateCal(dto);
		
		resp.sendRedirect(cp + "/admin/uniList.do?page=" + page);
		
	}
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AdminDAO dao = new AdminDAO();
		
		String cp = req.getContextPath();
		
		String cal_code = req.getParameter("cal_code");
		String page = req.getParameter("page");
		
		dao.deleteCal(cal_code);
		
		resp.sendRedirect(cp + "/admin/uniList.do?page=" + page);
	}
}
