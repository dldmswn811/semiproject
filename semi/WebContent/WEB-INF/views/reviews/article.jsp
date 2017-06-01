<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
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

<link rel="stylesheet" href="<%=cp%>/resource/css/style.css"
	type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/layout.css"
	type="text/css">
<link rel="stylesheet"
	href="<%=cp%>/resource/jquery/css/smoothness/jquery-ui.min.css"
	type="text/css">

<script type="text/javascript" src="<%=cp%>/resource/js/util.js"></script>
<script type="text/javascript"
	src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript">

function deleteBoard(rv_num) {
<c:if test="${sessionScope.member.mem_id=='admin' || sessionScope.member.mem_id==dto.mem_id}">
	var page = "${page}";
	var query = "rv_num="+rv_num+"&page="+page;
	var url="<%=cp%>/reviews/delete.do?"+query;
	
	if(confirm("게시물을 삭제 하시겠습니까 ?"))
		location.href=url;
		
</c:if>
<c:if test="${sessionScope.member.mem_id != 'admin' && sessionScope.member.mem_id != dto.mem_id}">
	alert("게시물을 삭제 할 수 없습니다")
</c:if>
	
}

function updateBoard(rv_num) {
	
<c:if test="${sessionScope.member.mem_id == dto.mem_id}">
	var page = "${page}";
	var query = "rv_num="+rv_num+"&page="+page;
	var url = "<%=cp%>/reviews/update.do?" + query;
	
	location.href=url;
</c:if>
<c:if test="${sessionScope.member.mem_id!=dto.mem_id}">
	alert("게시물을 수정 할 수 없습니다.");
</c:if>
	
}

</script>






<style type="text/css">

* {
    margin:0; padding:0;
}

body {
    font-family:"Malgun Gothic", "맑은 고딕", NanumGothic, 나눔고딕, 돋움, sans-serif;
    font-size: 14px;
    /* line-height: 1.42857143; */
	/* box-sizing: border-box;*/  /* padding과 border는 크기에 포함되지 않음 */
	
}

.body-container {
    clear:both;
    margin: 0px auto 15px;
    min-height: 450px;
}

.body-title {
    color: #424951;
    padding-top: 20px;
    padding-bottom: 5px;
    margin: 0 0 25px 0;
    border-bottom: 1px solid #dddddd;
}
.body-title h3 {
    font-size: 23px;
    min-width: 300px;
    font-family:"Malgun Gothic", "맑은 고딕", NanumGothic, 나눔고딕, 돋움, sans-serif;
    font-weight: bold;
    margin: 0 0 -5px 0;
    padding-bottom: 5px;
    display: inline-block;
    border-bottom: 3px solid #424951;
}

a {
    cursor: pointer;
    color: #000000;
    text-decoration: none;
    /* line-height: 150%; */
	/* box-sizing: border-box;*/  /* padding과 border는 크기에 포함되지 않음 */
}


a:hover, a:active {
    color: #F28011;
    text-decoration: underline;
} 



.btn {
    color:#333333;
    font-weight:500;
    font-family:"Malgun Gothic", "맑은 고딕", NanumGothic, 나눔고딕, 돋움, sans-serif;
    border:1px solid #cccccc;
    background-color:#fff;
    text-align:center;
    cursor:cursor;
    padding:3px 10px 5px;
    border-radius:4px;
}
.btn:active, .btn:focus, .btn:hover {
    background-color:#e6e6e6;
    border-color: #adadad;
    color: #333333;
}
.btn[disabled], fieldset[disabled] .btn {
    pointer-events: none;
    cursor: not-allowed;
    filter: alpha(opacity=65);
    -webkit-box-shadow: none;
    box-shadow: none;
    opacity: .65;
}

.btnConfirm {
    font-size: 15px; 
    border:none;
    color:#ffffff;
    background:#507CD1;
    width: 360px;
    height: 50px;
    line-height: 50px;
    border-radius:4px;
}

.boxTF {
    border:1px solid #999999;
    padding:3px 5px 5px;
    border-radius:4px;
    background-color:#ffffff;
    font-family:"Malgun Gothic", "맑은 고딕", NanumGothic, 나눔고딕, 돋움, sans-serif;
}
.boxTF[readonly] {
	background-color:#eeeeee;
	/* border: none;*/
}

.selectField {
    border:1px solid #999999;
    padding:2px 5px 4px;
    border-radius:4px;
    font-family:"Malgun Gothic", "맑은 고딕", NanumGothic, 나눔고딕, 돋움, sans-serif;
}

.boxTA {
    border:1px solid #999999;
    height:150px;
    padding:3px 5px;
    border-radius:4px;
    background-color:#ffffff;
    font-family:"Malgun Gothic", "맑은 고딕", NanumGothic, 나눔고딕, 돋움, sans-serif;
}


</style>










</head>
<body>

	<div class="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>

	<div class="container">
		<div class="body-container" style="width: 700px;">
			<div class="body-title">
				<h3>
					<span style="font-family: Webdings">2</span> 축제 리뷰
				</h3>
			</div>

			<div>
				<table
					style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">
					<tr height="35"
						style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
						<td colspan="2" align="center">${dto.rv_title}</td>
					</tr>

					<tr height="35" style="border-bottom: 1px solid #cccccc;">
						<td width="50%" align="left" style="padding-left: 5px;">이름 :
							${dto.mem_name}</td>
						<td width="50%" align="right" style="padding-right: 5px;">
							${dto.rv_date} | 조회 ${dto.rv_cnt}</td>
					</tr>

					<tr style="border-bottom: 1px solid #cccccc;">
						<td colspan="2" align="left" style="padding: 10px 5px;"
							valign="top" height="200">${dto.rv_content}</td>
					</tr>

					<tr height="35" style="border-bottom: 1px solid #cccccc;">
						<td colspan="2" align="left" style="padding-left: 5px;">이전글 :
							<c:if test="${not empty preReadDto}">
								<a
									href="<%=cp%>/reviews/article.do?${query}&rv_num=${preReadDto.rv_num}">${preReadDto.rv_title}</a>
							</c:if>

						</td>
					</tr>

					<tr height="35" style="border-bottom: 1px solid #cccccc;">
						<td colspan="2" align="left" style="padding-left: 5px;">다음글 :
							<c:if test="${not empty nextReadDto}">
								<a
									href="<%=cp%>/reviews/article.do?${query}&rv_num=${nextReadDto.rv_num}">${nextReadDto.rv_title}</a>
							</c:if>
						</td>
					</tr>
				</table>

				<table
					style="width: 100%; margin: 0px auto 20px; border-spacing: 0px;">
					<tr height="45">
						<td width="300" align="left">
							<button type="button" class="btn"
								onclick="javascript:location.href='<%=cp%>/reviews/reply.do?rv_num=${dto.rv_num}&page=${page}';">답변</button>
							<c:if test="${sessionScope.member.mem_id==dto.mem_id}">
								<button type="button" class="btn"
									onclick="updateBoard('${dto.rv_num}');">수정</button>
							</c:if> <c:if
								test="${sessionScope.member.mem_id==dto.mem_id || sessionScope.member.mem_id=='admin'}">
								<button type="button" class="btn"
									onclick="deleteBoard('${dto.rv_num}');">삭제</button>
							</c:if>
						</td>

						<td align="right">
							<button type="button" class="btn"
								onclick="javascript:location.href='<%=cp%>/reviews/list.do?${query}';">리스트</button>
						</td>
					</tr>
				</table>
			</div>

		</div>
	</div>

	<div class="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</div>

	<script type="text/javascript"
		src="<%=cp%>/resource/jquery/js/jquery-ui.min.js"></script>
	<script type="text/javascript"
		src="<%=cp%>/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>
</body>
</html>