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
		
		// �α��� ��
		if(uri.indexOf("login.do")!=-1) {
			login(req,resp);
		// �α��� ó��	
		} else if(uri.indexOf("login_ok.do")!=-1) {
			login_ok(req, resp);
		// �α׾ƿ� ó��	
		} else if(uri.indexOf("logout.do")!=-1) {
			logout(req, resp);
		// ȸ������ ��	
		} else if(uri.indexOf("member.do")!=-1) {
			member(req, resp);
		// ȸ������ ó��
		} else if(uri.indexOf("member_ok.do")!=-1) {
			member_ok(req, resp);
		// ȸ�� ���̵� üũ
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
			req.setAttribute("message", "���̵� Ȥ�� �н����尡 ��ġ���� �ʽ��ϴ�");
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
	    req.setAttribute("title", "ȸ������");
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
	      
	      if(result >= 1){ //insert all �� �䷸�� �� 
	         forward(req, resp, "/WEB-INF/views/member/complete.jsp");
	         req.setAttribute("message", "ȸ�����Կ� �����Ͽ����ϴ�.");
	         return;
	      }
	      else{
	         req.setAttribute("message", "ȸ�����Կ� �����Ͽ����ϴ�.");
	         req.setAttribute("mode", "created");
	         req.setAttribute("title", "ȸ�� ����");
	         forward(req, resp, "/WEB-INF/views/member/member.jsp");
	         return;
	      }
	}
	protected void memIdCheck(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		   MemberDAO dao=new MemberDAO();
		   String mem_id=req.getParameter("mem_id");
	      
		   String passed="true";
		   MemberDTO dto=dao.readMember(mem_id); // �������� �����ϱ� ����ϸ� X
		   if(dto!=null) {
			   passed="false";
		   }
		   
		   // JSON ���� �� {"Ű1":"��1" [,"Ű2":"��2"]} : MAP����
		   JSONObject job = new JSONObject();
		   job.put("passed",passed);
		   
		   // Ŭ���̾�Ʈ���� ����
		   resp.setContentType("text/html;charset=utf-8");
		   PrintWriter out=resp.getWriter();
		   out.print(job.toString());
		   
	   }
	}








