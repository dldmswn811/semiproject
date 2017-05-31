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
        var f = document.calForm;

       var str = f.cal_code.value;
        if(!str) {
            alert("학교코드를 입력하세요. ");
            f.cal_code.focus();
            return;
        }

        str = f.cal_name.value;
        if(!str) {
            alert("학교 이름을 입력하세요. ");
            f.cal_name.focus();
            return;
        }
        
        str = f.cal_add.value;
        if(!str) {
            alert("학교 주소를 입력하세요. ");
            f.cal_add.focus();
            return;
        }

       var mode="${mode}";
       if(mode=="created")
          f.action="<%=cp%>/admin/unicreated_ok.do";
       else if(mode=="update")
          f.action="<%=cp%>/admin/uniupdate_ok.do";

        f.submit();
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
            <h3><span style="font-family: Webdings">2</span> ${mode=='update'?'학교 수정':'학교 등록'} </h3>
        </div>
        
        <div>
         <form name="calForm" method="post">
           <table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">
           <tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
               <td width="100" bgcolor="#eeeeee" style="text-align: center;">학교 코드</td>
               <td style="padding-left:10px;"> 
                 <input type="text" name="cal_code" maxlength="100" class="boxTF" style="width: 95%;" value="${dto.cal_code}"
                 	${mode=="update" ? "readonly='readonly' ":""}>
               </td>
           </tr>
         
           <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;"> 
               <td width="100" bgcolor="#eeeeee" style="text-align: center;">학교 이름</td>
               <td style="padding-left:10px;"> 
                 <input type="text" name="cal_name" maxlength="100" class="boxTF" style="width: 95%;" value="${dto.cal_name}">
               </td>
           </tr>
           
           <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;"> 
               <td width="100" bgcolor="#eeeeee" style="text-align: center;">학교 주소</td>
               <td style="padding-left:10px;"> 
                 <input type="text" name="cal_add" maxlength="100" class="boxTF" style="width: 95%;" value="${dto.cal_add}">
               </td>
           </tr>

           </table>
         
           <table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
              <tr height="45"> 
               <td align="center" >
                 <button type="button" class="btn" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}</button>
                 <button type="reset" class="btn">다시입력</button>
                 <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/admin/uniList.do';">${mode=='update'?'수정취소':'등록취소'}</button>
                 
                 <c:if test="${mode=='update'}">
                 	<input type = "hidden" name = "page" value ="${page}">
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