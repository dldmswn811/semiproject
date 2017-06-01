<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
   String cp = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>spring</title>

<link rel="stylesheet" href="<%=cp%>/resource/css/style.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/layout.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/jquery/css/smoothness/jquery-ui.min.css" type="text/css">

<script type="text/javascript" src="<%=cp%>/resource/js/util.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript">
function deleteCal(cal_code) {
<c:if test="${sessionScope.member.memRoll >= 2}">
    var page = "${page}";
    var query = "cal_code=" + cal_code + "&page=" + page;
    var url = "<%=cp%>/admin/powerdelete.do?" + query;

    if(confirm("위 자료를 삭제 하시 겠습니까 ? "))
    	location.href=url;
</c:if>    
<c:if test="${sessionScope.member.memRoll < 2}">
    alert("게시물을 삭제할 수  없습니다.");
</c:if>
}

function updateCal(cal_code) {
<c:if test="${sessionScope.member.memRoll >= 2}">
    var page = "${page}";
    var query = "cal_code=" + cal_code + "&page=" + page;
    var url = "<%=cp%>/admin/powerupdate.do?" + query;

    location.href=url;
</c:if>

<c:if test="${sessionScope.member.memRoll < 2}">
   alert("게시물을 수정할 수  없습니다.");
</c:if>
}

</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
   
<div class="container">
    <div class="body-container" style="width: 700px;">
        <div class="body-title">
            <h3><span style="font-family: Webdings">2</span> 학교 관리 </h3>
        </div>
        
        <div>
         <table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px;">
            <tr height="35">
               <td align="left" width="50%">
                   ${dataCount}개(${page}/${total_page}페이지)
               </td>
               <td align="right">
                   &nbsp;
               </td>
            </tr>
         </table>
         
         <table style="width: 100%; margin: 0px auto; border-spacing: 0px; border-collapse: collapse;">
           <tr align="center" bgcolor="#eeeeee" height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
               <th width="60" style="color: #787878;">아이디</th>
               <th width="80" style="color: #787878;">이름</th>
               <th width="100" style="color: #787878;">권한</th>
               <th width="50" style="color: #787878;">학교</th>
           </tr>
          
          <c:forEach var="dto" items="${list}">
           <tr align="center" bgcolor="#ffffff" height="35" style="border-bottom: 1px solid #cccccc;"> 
               <td>${dto.cal_code }</td>
               <td>${dto.cal_name}</td>
               <td>${dto.cal_add}</td>
               <td>
               <button type="button" class="btn" onclick="updateCal('${dto.cal_code}')">수정</button>
               &nbsp;/&nbsp;
               <button type="button" class="btn" onclick="deleteCal('${dto.cal_code}')">삭제</button>
               </td>
           </tr>
         </c:forEach>
         </table>
          
         <table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
            <tr height="35">
            <td align="center">
            	<c:if test="${dataCount == 0 }">
					등록된 게시물이 없습니다.
				</c:if>
				
				<c:if test="${dataCount != 0 }">
					${paging}
				</c:if>
            </td>
            </tr>
         </table>
         
         <table style="width: 100%; margin: 10px auto; border-spacing: 0px;">
            <tr height="40">
               <td align="left" width="100">
                   <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/admin/powerList.do';">새로고침</button>
               </td>
               
               <td align="right" width="100">
                   <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/admin/powercreated.do';">관리자 등록</button>
               </td>
            </tr>
         </table>
        </div>

    </div>
</div>

<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>
</body>
</html>