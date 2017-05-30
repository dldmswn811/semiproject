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
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>spring</title>

<link rel="stylesheet" href="<%=cp%>/resource/jquery/css/smoothness/jquery-ui.min.css" type="text/css"/>
<link rel="stylesheet" href="<%=cp%>/resource/bootstrap/css/bootstrap.min.css" type="text/css"/>
<link rel="stylesheet" href="<%=cp%>/resource/bootstrap/css/bootstrap-theme.min.css" type="text/css"/>

<link rel="stylesheet" href="<%=cp%>/resource/css/style.css" type="text/css"/>


<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>

</head>
<body>

<div>
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
<div><img src="resource/images/ex4.png" style="width: 100%"></div>
<div id="container">
		<div class="mainContent">
			<ul>
				<li><img src="resource/images/mainEx.jpg" style="width:200px; heigth:250px;"></li>
				<li><img src="resource/images/mainEx.jpg" style="width:200px; heigth:250px;"></li>
				<li><img src="resource/images/mainEx.jpg" style="width:200px; heigth:250px;"></li>
			</ul>
		</div>
		<div class="mainContent">
			<ul>
				<li><img src="resource/images/mainEx.jpg" style="width:200px; heigth:250px;"></li>
				<li><img src="resource/images/mainEx.jpg" style="width:200px; heigth:250px;"></li>
				<li><img src="resource/images/mainEx.jpg" style="width:200px; heigth:250px;"></li>
			</ul>
		</div>
		
</div>

<div>
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/bootstrap/js/bootstrap.min.js"></script>
</body>
</html>