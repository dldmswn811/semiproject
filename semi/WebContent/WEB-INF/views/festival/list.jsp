<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.Calendar"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%
    request.setCharacterEncoding("utf-8");
	String cp=request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
* {
	margin: 0px;
	padding: 0px;
	font-size: 9pt;
	font-family: 맑은 고딕, 돋움;
}
</style>

<script type="text/javascript">
function change() {
	var year=document.getElementById("year").value;
	var month=document.getElementById("month").value;
	
	var url="list.do?year="+year+"&month="+month;
	
	location.href=url;
}
</script>

</head>
<body>

<table style="width: 700px; margin:0px auto; border-spacing: 0; border-collapse: collapse;">
<tr height="25">
	<td align="center">
		<select id="year" onchange="change();" style="padding: 3px;">
			<c:forEach var = "i" begin ="${year-5}" end ="${year+5}">
				<option value="${i}" ${year == i ? "selected='selected'":"" }> ${i} 年 </option>
			</c:forEach>
					
		</select>
		&nbsp;
		<select id="month" onchange="change();" style="padding: 3px;">
			<c:forEach var = "i" begin ="1" end ="12">
				<option value="${i}" ${month == i ? "selected='selected'":"" }> ${i} 月 </option>
			</c:forEach>
		</select>
	</td>
</tr>
</table>

<div style="margin-top: 30px;">

<table border="1" style="width: 800px; margin:5px auto; border-spacing: 0px;border:1px solid #cccccc; border-collapse: collapse;">
   <tr height="25" bgcolor="#e6e4e6">
   		<td width="100" align="center" style="color: red;">일</td>
   		<td width="100" align="center">월</td>
   		<td width="100" align="center">화</td>
   		<td width="100" align="center">수</td>
   		<td width="100" align="center">목</td>
   		<td width="100" align="center">금</td>
   		<td  width="100" align="center" style="color: blue;">토</td>
   </tr>
   
   <c:forEach var = "row" items ="${arrCal}">
   		<tr height='85' bgcolor='#fff'>
   		<c:forEach var = "col" items="${row}">
   			<td  style='padding-left:5px;color:#cccccc; vertical-align: text-top;'>
   				${col}
   			</td>
   		</c:forEach>
   		</tr>
   </c:forEach>

</table>

<button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/festival/created.do';">글올리기</button>

</div>

</body>
</html>