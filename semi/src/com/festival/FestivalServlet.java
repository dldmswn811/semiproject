package com.festival;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.util.MyServlet;

@WebServlet("/festival/*")
public class FestivalServlet extends MyServlet{

	private static final long serialVersionUID = 1L;
	
	private String pathname;
	
	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String uri = req.getRequestURI();
		
		HttpSession session = req.getSession();
		
		//������ ������ ���!
		
		String root=session.getServletContext().getRealPath("/");
		pathname=root+File.separator+"uploads"+File.separator+"festival";
		File f=new File(pathname);
		if(! f.exists()) {
			f.mkdirs();
		}
		
		// uri�� ���� �۾� ����
		if(uri.indexOf("list.do")!=-1) {
				list(req, resp);
		}
		
		else if(uri.indexOf("created.do")!=-1) {
			createdForm(req, resp);
		}
		
		else if(uri.indexOf("created_ok.do")!=-1) {
			createdSubmit(req, resp);
		}	
	}
	
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String cp = req.getContextPath(); // ���� cp�� ������ ����
		
		Calendar cal = Calendar.getInstance(); // Ķ���� ��ü�� ����
		
		// Ķ���� ��ü�� ��� ���� ������
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		
		// get������� �Ѿ�� ��� ���� ������ ����
		String getYear = req.getParameter("year"); 
		String getMonth = req.getParameter("month");
		
		// ���� get������� ���� �Ѿ�Ӵٸ� ������ �ٽ� ����
		if(getYear != null){
			year = Integer.parseInt(getYear);
		}
		
		if(getMonth != null){
			month = Integer.parseInt(getMonth);
		}
		
		//year�� month�� 1���� ������������ �˾Ƴ���.
		cal.set(year, month-1, 1);
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH)+1;
		
		int week = cal.get(Calendar.DAY_OF_WEEK); // ������� 7
		
		// ������ ���ϱ��� �޷¿� ��µǾ�� �ϴ���...
		Calendar preCal = (Calendar)cal.clone();
		preCal.add(Calendar.DATE, -(week-1));
		int pdate = preCal.get(Calendar.DATE);
		
		//�ش���� ������ ��¥
		int endDay = cal.getActualMaximum(Calendar.DATE);
		int preEndDay = preCal.getActualMaximum(Calendar.DATE);
		int n = 1, nextMonthDay = 1;
		
		//���߹迭�� ��¥�� ��� �迭 ����.
		String [][] arrCal;
		arrCal = new String[cal.getActualMaximum(Calendar.WEEK_OF_MONTH)][7];
		
		for(int i = 0; i < arrCal.length; i++){
			for(int j =0; j<arrCal[i].length; j++){
				
				if(pdate <= preEndDay && week != 1){
					arrCal[i][j] = "<span style ='color : #5d5d5d;'>"+pdate+"</span>";
					pdate ++;
					
					
				}
				else if(n <= endDay){
					if(j == 0){
						arrCal[i][j] = "<span style ='color : red;'>"+n+"</span>";
						n++;
						
						
					}
					
					else if(j == 6){
						arrCal[i][j] = "<span style ='color : blue;'>"+n+"</span>";
						n++;
						
						
					}
					
					else{
						arrCal[i][j] = "<span style ='color : black;'>"+n+"</span>";
						n++;
						
						
					}
				}
				else{
					arrCal[i][j] = "<span style ='color : #5d5d5d;'>"+nextMonthDay+"</span>";
					nextMonthDay++; 
					
			 		
				}
			}
		}
		
		req.setAttribute("year", year);
		req.setAttribute("month", month);
		req.setAttribute("arrCal", arrCal);
		
		forward(req, resp, "/WEB-INF/views/festival/list.jsp");
	}
	
	protected void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/festival/created.jsp");
	}
	
	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
	
}
