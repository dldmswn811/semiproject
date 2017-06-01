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
    function sendOk() {
        var f = document.boardForm;

    	var str = f.rv_title.value;
        if(!str) {
            alert("제목을 입력하세요. ");
            f.rv_title.focus();
            return;
        }

    	str = f.rv_content.value;
        if(!str) {
            alert("내용을 입력하세요. ");
            f.rv_content.focus();
            return;
        }

    	var mode="${mode}";
    	if(mode=="created")
    		f.action="<%=cp%>/reviews/created_ok.do";
    	else if(mode=="update")
    		f.action="<%=cp%>/reviews/update_ok.do";
       	else if(mode=="reply")
       		f.action="<%=cp%>/reviews/reply_ok.do";

        f.submit();
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
            <h3><span style="font-family: Webdings">2</span> 축제 리뷰 </h3>
        </div>
        
        <div>
			<form name="boardForm" method="post">
			  <table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">
			  <tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
			      <td style="padding-left:10px;"> 
			        <input type="text" name="rv_title" maxlength="100" class="boxTF" style="width: 95%;" value="${dto.rv_title}">
			      </td>
			  </tr>
			
			  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">작성자</td>
			      <td style="padding-left:10px;"> 
			          ${sessionScope.member.mem_name}
			      </td>
			  </tr>
			
			  <tr align="left" style="border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center; padding-top:5px;" valign="top">내&nbsp;&nbsp;&nbsp;&nbsp;용</td>
			      <td valign="top" style="padding:5px 0px 5px 10px;"> 
			        <textarea name="rv_content" rows="12" class="boxTA" style="width: 95%;">${dto.rv_content}</textarea>
			      </td>
			  </tr>
			  </table>
			
			  <table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
			     <tr height="45"> 
			      <td align="center" >
			        <button type="button" class="btn" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}</button>
			        <button type="reset" class="btn">다시입력</button>
			        <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/reviews/list.do';">${mode=='update'?'수정취소':'등록취소'}</button>
			        
			        
			        
			        <c:if test="${mode=='update'}">
			        	<input type="hidden" name="rv_num" value="${dto.rv_num}">
			        	<input type="hidden" name="page" value="${page}">
			        </c:if>
			        
			        
			        
			        <c:if test="${mode=='reply'}">
			        	<input type="hidden" name="groupNum" value="${dto.groupNum}">
			        	<input type="hidden" name="orderNo" value="${dto.orderNo}">
			        	<input type="hidden" name="depth" value="${dto.depth}">
			        	<input type="hidden" name="parent" value="${dto.rv_num}">
			        	<input type="hidden" name="page" value="${page}">
			        
			        </c:if>
			        
			      </td>
			    </tr>
			  </table>
			</form>
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