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
		
		//파일을 저장할 경로!
		
		String root=session.getServletContext().getRealPath("/");
		pathname=root+File.separator+"uploads"+File.separator+"festival";
		File f=new File(pathname);
		if(! f.exists()) {
			f.mkdirs();
		}
		
		// uri에 따른 작업 구분
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
		
		String cp = req.getContextPath(); // 현재 cp를 변수로 지정
		
		Calendar cal = Calendar.getInstance(); // 캘린더 객체를 생성
		
		// 캘린더 객체의 년과 월을 가져옴
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		
		// get방식으로 넘어온 년과 월을 변수로 지정
		String getYear = req.getParameter("year"); 
		String getMonth = req.getParameter("month");
		
		// 만약 get방식으로 값이 넘어왓다면 변수로 다시 세팅
		if(getYear != null){
			year = Integer.parseInt(getYear);
		}
		
		if(getMonth != null){
			month = Integer.parseInt(getMonth);
		}
		
		//year년 month월 1일이 무슨요일인지 알아낸다.
		cal.set(year, month-1, 1);
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH)+1;
		
		int week = cal.get(Calendar.DAY_OF_WEEK); // 토요일은 7
		
		// 전달의 몇일까지 달력에 출력되어야 하는지...
		Calendar preCal = (Calendar)cal.clone();
		preCal.add(Calendar.DATE, -(week-1));
		int pdate = preCal.get(Calendar.DATE);
		
		//해당월의 마지막 날짜
		int endDay = cal.getActualMaximum(Calendar.DATE);
		int preEndDay = preCal.getActualMaximum(Calendar.DATE);
		int n = 1, nextMonthDay = 1;
		
		//이중배열로 날짜를 담는 배열 선언.
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
