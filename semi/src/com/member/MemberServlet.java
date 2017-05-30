package com.member;
 
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import net.sf.json.JSONObject;

@WebServlet("/member/*")
public class MemberServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	protected void forward(HttpServletRequest req, HttpServletResponse resp, String path) throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher(path);
		rd.forward(req, resp);
	}
	
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String uri=req.getRequestURI();
		
		// 로그인 폼
		if(uri.indexOf("login.do")!=-1) {
			login(req,resp);
		// 로그인 처리	
		} else if(uri.indexOf("login_ok.do")!=-1) {
			login_ok(req, resp);
		// 로그아웃 처리	
		} else if(uri.indexOf("logout.do")!=-1) {
			logout(req, resp);
		// 회원가입 폼	
		} else if(uri.indexOf("member.do")!=-1) {
			member(req, resp);
		// 회원가입 처리
		} else if(uri.indexOf("member_ok.do")!=-1) {
			member_ok(req, resp);
		// 회원 아이디 체크
		} else if(uri.indexOf("memIdCheck.do")!=-1) {
			memIdCheck(req, resp);
		} 
	
	}
	
	protected void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		forward(req, resp, "/WEB-INF/views/member/login.jsp");
	}
	protected void login_ok(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MemberDAO dao=new MemberDAO();
		
		String mem_id = req.getParameter("mem_id");
		String mem_pw = req.getParameter("mem_pw");
		
		MemberDTO dto = dao.readMember(mem_id);
		if(dto==null || ! mem_pw.equals(dto.getMem_pw())) {
			req.setAttribute("message", "아이디 혹은 패스워드가 일치하지 않습니다");
			forward(req, resp, "/WEB-INF/views/member/login.jsp");
			return;
		}
		
		HttpSession session=req.getSession();
		
		SessionInfo info = new SessionInfo();
		info.setMem_id(dto.getMem_id());
		info.setMem_name(dto.getMem_name());
		info.setMemRoll(dto.getMem_gt());
		
		session.setAttribute("member", info);
		 
		String cp=req.getContextPath();
		resp.sendRedirect(cp+"/");
	}
	protected void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	      HttpSession session = req.getSession();
	      
	      session.removeAttribute("member");
	      
	      session.invalidate();
	      
	      String cp=req.getContextPath();
	      resp.sendRedirect(cp+"/");
	}
	protected void member(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MemberDAO dao=new MemberDAO();  
		List<MemberDTO> list=null;
	    list=dao.listcalcode();
	      
	    req.setAttribute("list", list);
		req.setAttribute("mode", "created");
	    req.setAttribute("title", "회원가입");
	    forward(req, resp, "/WEB-INF/views/member/member.jsp");
	}
	protected void member_ok(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		  int result=0;
	      MemberDAO dao=new MemberDAO();
	      MemberDTO dto=new MemberDTO();
	      
	      dto.setMem_id(req.getParameter("mem_id"));
	      dto.setMem_name(req.getParameter("mem_name"));
	      dto.setMem_pw(req.getParameter("mem_pw"));
	      dto.setCal_code(req.getParameter("cal_code"));
	      dto.setMem_email1(req.getParameter("mem_email1"));
	      dto.setMem_email2(req.getParameter("mem_mail2"));
	      dto.setMem_tel1(req.getParameter("mem_tel1"));
	      dto.setMem_tel2(req.getParameter("mem_tel2"));
	      dto.setMem_tel3(req.getParameter("mem_tel3"));

	      result=dao.insertMember(dto);
	      
	      if(result >= 1){ //insert all 은 요렇게 비교 
	         forward(req, resp, "/WEB-INF/views/member/complete.jsp");
	         req.setAttribute("message", "회원가입에 성공하였습니다.");
	         return;
	      }
	      else{
	         req.setAttribute("message", "회원가입에 실패하였습니다.");
	         req.setAttribute("mode", "created");
	         req.setAttribute("title", "회원 가입");
	         forward(req, resp, "/WEB-INF/views/member/member.jsp");
	         return;
	      }
	}
	protected void memIdCheck(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		   MemberDAO dao=new MemberDAO();
		   String mem_id=req.getParameter("mem_id");
	      
		   String passed="true";
		   MemberDTO dto=dao.readMember(mem_id); // 가져오면 있으니까 사용하면 X
		   if(dto!=null) {
			   passed="false";
		   }
		   
		   // JSON 형식 → {"키1":"값1" [,"키2":"값2"]} : MAP구조
		   JSONObject job = new JSONObject();
		   job.put("passed",passed);
		   
		   // 클라이언트에게 전송
		   resp.setContentType("text/html;charset=utf-8");
		   PrintWriter out=resp.getWriter();
		   out.print(job.toString());
		   
	   }
	}








