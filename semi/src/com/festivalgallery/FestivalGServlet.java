package com.festivalgallery;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.util.FileManager;
import com.util.MyServlet;

@WebServlet("/festivalgallery/*")
public class FestivalGServlet extends MyServlet {
	private static final long serialVersionUID = 1L;
	
	private SessionInfo info;
	private String pathname;//파일 저장경로
	
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String uri=req.getRequestURI();
		String cp=req.getContextPath();
		
		HttpSession session=req.getSession();
		info=(SessionInfo)session.getAttribute("member");
		if(info==null) {
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		
		//이미지는 웹경로에 저장해야 출력 가능
		//이미지 저장 경로
		String root=session.getServletContext().getRealPath("/");
		pathname=root+"uploads"+File.separator+"festival";
		File f=new File(pathname);
		if(!f.exists())
			f.mkdirs();
		
		if(uri.indexOf("list.do")!=-1) {
			list(req, resp);
		} else if(uri.indexOf("created.do")!=-1){
			createdForm(req, resp);
		} else if(uri.indexOf("created_ok.do")!=-1){
			createdSubmit(req, resp);
		} else if(uri.indexOf("article.do")!=-1) {
			article(req, resp);
		} else if(uri.indexOf("update.do")!=-1){
			updateForm(req, resp);
		} else if(uri.indexOf("update_ok.do")!=-1) {
			updateSubmit(req, resp);
		} else if(uri.indexOf("delete.do")!=-1) {
			delete(req, resp);
		} 
	}

	private void delete(HttpServletRequest req, HttpServletResponse resp) {
		
		
	}

	private void updateSubmit(HttpServletRequest req, HttpServletResponse resp) {
		
		
	}

	private void updateForm(HttpServletRequest req, HttpServletResponse resp) {
		
		
	}

	private void article(HttpServletRequest req, HttpServletResponse resp) {
	
		
	}

	private void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		//게시물 저장
		String cp=req.getContextPath();
		FestivalGDAO dao=new FestivalGDAO();
		FestivalGDTO dto=new FestivalGDTO();
		
		String encType="UTF-8";
		int maxSize=5*1024*1024;
		
		MultipartRequest mreq=new MultipartRequest(req, pathname, maxSize, encType, new DefaultFileRenamePolicy());
		
		if(mreq.getFile("upload")!=null) {
			dto.setMem_id(info.getMem_id());
			dto.setFg_title(mreq.getParameter("fg_title"));
			dto.setFg_content(mreq.getParameter("fg_content"));
			String saveFg_imagename=mreq.getFilesystemName("upload");
			
			saveFg_imagename=FileManager.doFilerename(pathname, saveFg_imagename);
			dto.setFg_imagename(saveFg_imagename);
			
			//저장
			dao.insertFestivalGallery(dto);
		}
		resp.sendRedirect(cp+"/festivalgallery/list.do");
	}

	private void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 req.setAttribute("mode", "created");
		 forward(req, resp, "/WEB-INF/views/festivalgallery/created.jsp");
		 
	}

	private void list(HttpServletRequest req, HttpServletResponse resp) {
		
	}

}
