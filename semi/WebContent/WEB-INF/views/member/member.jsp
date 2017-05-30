<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String cp=request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
 
<link rel="stylesheet" href="<%=cp%>/resource/css/style.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/css/layout.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/resource/jquery/css/smoothness/jquery-ui.min.css" type="text/css">

<script type="text/javascript" src="<%=cp%>/resource/jquery/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="<%=cp%>/resource/js/util.js"></script>

<script type="text/javascript">
	function memberOk() {
		var f = document.memberForm;
		var str;
		
		str = f.mem_id.value;
		str = str.trim();
		if(!str) {
			alert("아이디를 입력하세요");
			f.mem_id.focus();
			return;
		}
		if(!/^[a-z][a-z0-9_]{4,9}$/i.test(str)) { 
			alert("아이디는 5~10자이며 첫글자는 영문자이어야 합니다.");
			f.mem_id.focus();
			return;
		}
		f.mem_id.value = str;
		
		str = f.mem_pw.value;
		str = str.trim();
		if(!str) {
			alert("패스워드를 입력하세요");
			f.mem_pw.focus();
			return;
		}
		if(!/^(?=.*[a-z])(?=.*[!@#$%^*+=-]|.*[0-9]).{5,10}$/i.test(str)) { 
			alert("패스워드는 5~10자이며 하나 이상의 숫자나 특수문자가 포함되어야 합니다.");
			f.mem_pw.focus();
			return;
		}
		f.mem_pw.value = str;
		
		if(str!= f.memPwCheck.value) {
			alert("패스워드가 일치하지 않습니다.");
			f.memPwCheck.focus();
			return;
		}
		
		str = f.mem_name.value;
		str = str.trim();
		if(!str) {
			alert("이름을 입력하세요");
			f.mem_name.focus();
			return;
		}
		f.mem_name.value = str;
		
		str = f.mem_tel1.value;
		str = str.trim();
		if(!str) {
			alert("전화번호를 입력하세요");
			f.mem_tel1.focus();
			return;
		}
		
		str = f.mem_tel2.value;
		str = str.trim();
		if(!str) {
			alert("전화번호를 입력하세요");
			f.mem_tel2.focus();
			return;
		}

		
		str = f.mem_tel3.value;
		str = str.trim();
		if(!str) {
			alert("전화번호를 입력하세요");
			f.mem_tel3.focus();
			return;	
		}
		
		str = f.mem_email1.value;
		str = str.trim();
		if(!str) {
			alert("이메일을 입력하세요");
			f.mem_email1.focus();
			return; 
		}
		
		str = f.mem_email2.value;
		str = str.trim();
		if(!str) {
			alert("이메일을 입력하세요");
			f.mem_email2.focus();
			return; 
		}
		
		str = f.cal_code.value;
		str = str.trim();
		if(!str) {
			alert("대학을 선택하세요");
			return;
		}
		
		var mode="${mode}";
		if(mode=="created") {
			f.action = "<%=cp%>/member/member_ok.do";
		} else if(mode=="update") {
			f.action = "<%=cp%>/member/update_ok.do";
		}
		f.submit();
	}
	function changeEmail() {
		var f = document.memberForm;
		
		var str = f.selectEmail.value;
		if(str!="direct") {
			f.mem_email2.value=str;
			f.mem_email2.readOnly = true;
			f.mem_email1.focus();
		} else {
			f.mem_email2.value="";
			f.mem_email2.readOnly = false;
			f.mem_email1.focus();
		}
	}
	function changeCollege() {
		var f = document.memberForm;
		
		var str = f.selectCollege.value;
		if(str!="diret") {
			f.cal_code.value=str;
			f.cal_code.readOnly = true;
		} else {
			f.cal_code.value="";
			f.cal_code.readOnly = false;
			f.selectCollege.focus();
		}
		
	}
	
	function memIdCheck() {
		var mem_id=$("#mem_id").val().trim();
		if(!/^[a-z][a-z0-9_]{4,9}$/i.test(mem_id)) { 
			$("#mem_id").parent().next(".help-block").html("첫글자가 영문자인 5~10자 이내의 문자이어야 한다");
			$("#mem_id").focus();
			return;
		}
		var url="<%=cp%>/member/memIdCheck.do";
		var query="mem_id="+mem_id;
		
		
		// 제이슨!
		$.ajax({
			type:"post",
			url:url,
			data:query,
			dataType:"json",
			success:function(data) { // 성공~
				var passed = data.passed;
				if(passed=="true") {
					var s="<span style='color:blue;'>"+$("#mem_id").val()
					+"</span>이 아이디는 사용가능합니다.";
					$("#mem_id").parent().next(".help-block").html(s);				
				} else {
					var s="<span style='color:red;'>"+$("#mem_id").val()
						+"</span>이 아이디는 사용할 수 없습니다.";
					$("#mem_id").parent().next(".help-block").html(s);
					$("#mem_id").val("");
					$("#mem_id").focus();
				}
			}
			,error:function(e) { // 실패~
				console.log(e.responseText);
			}
			
		});
		
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
            <h3><span style="font-family: Webdings">2</span> ${title} </h3>
        </div>
        <div>
			<form name="memberForm" method="post">
			  <table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px;">
			  <tr>
			      <td width="100" valign="top" style="text-align: right; padding-top: 15px;">
			            <label style="font-weight: 900;">아이디</label>
			      </td>
			      <td style="padding: 0 0 15px 15px;">
			        <p style="margin-bottom: 5px;">
			            <input type="text" name="mem_id" id="mem_id" value="${dto.mem_id}"
                         onchange="memIdCheck();" style="width: 95%;"
                         ${mode=="update" ? "readonly='readonly' ":""}
                         maxlength="15" class="boxTF" placeholder="아이디">
			        </p>
			        <p class="help-block">아이디는 필수입니다</p>
			      </td>
			  </tr>
        
			  <tr>
			      <td width="100" valign="top" style="text-align: right; padding-top: 15px;">
			            <label style="font-weight: 900;">패스워드</label>
			      </td>
			      <td style="padding: 0 0 15px 15px;">
			        <p style="margin-bottom: 5px;">
			            <input type="password" name="mem_pw" maxlength="15" class="boxTF"
			                       style="width:95%;" placeholder="패스워드">
			        </p>
			        <p class="help-block">패스워드는 5~10자 이내이며, 하나 이상의 숫자나 특수문자가 포함되어야 합니다.</p>
			      </td>
			  </tr>
			  
			  <tr>
			      <td width="100" valign="top" style="text-align: right; padding-top: 15px;">
			            <label style="font-weight: 900;">패스워드 확인</label>
			      </td>
			      <td style="padding: 0 0 15px 15px;">
			        <p style="margin-bottom: 5px;">
			            <input type="password" name="memPwCheck" maxlength="15" class="boxTF"
			                       style="width: 95%;" placeholder="패스워드 확인">
			        </p>
			        <p class="help-block">패스워드를 한번 더 입력해주세요.</p>
			      </td>
			  </tr>
			  
			  <tr>
			      <td width="100" valign="top" style="text-align: right; padding-top: 15px;">
			            <label style="font-weight: 900;">이름</label>
			      </td>
			      <td style="padding: 0 0 15px 15px;">
			        <p style="margin-bottom: 5px;">
			            <input type="text" name="mem_name" value="${dto.mem_name}" maxlength="30" class="boxTF"
		                      style="width: 95%;"
		                      ${mode=="update" ? "readonly='readonly' ":""}
		                      placeholder="이름">
			        </p>
			      </td>
			  </tr>
			  
			  <tr>
			      <td width="100" valign="top" style="text-align: right; padding-top: 15px;">
			            <label style="font-weight: 900;">이메일</label>
			      </td>
			      <td style="padding: 0 0 15px 15px;">
			        <p style="margin-bottom: 5px;">
			            <select name="selectEmail" onchange="changeEmail();" class="selectField">
			                <option value="">선 택</option>
			                <option value="naver.com" ${dto.mem_email2=="naver.com" ? "selected='selected'" : ""}>네이버 메일</option>
			                <option value="hanmail.net" ${dto.mem_email2=="hanmail.net" ? "selected='selected'" : ""}>한 메일</option>
			                <option value="hotmail.com" ${dto.mem_email2=="hotmail.com" ? "selected='selected'" : ""}>핫 메일</option>
			                <option value="gmail.com" ${dto.mem_email2=="gmail.com" ? "selected='selected'" : ""}>지 메일</option>
			                <option value="direct">직접입력</option>
			            </select>
			            <input type="text" name="mem_email1" value="${dto.mem_email1}" size="13" maxlength="30"  class="boxTF">
			            @ 
			            <input type="text" name="mem_email2" value="${dto.mem_email2}" size="13" maxlength="30"  class="boxTF" readonly="readonly">
			        </p>
			      </td>
			  </tr>
			  
			  <tr>
			      <td width="100" valign="top" style="text-align: right; padding-top: 15px;">
			            <label style="font-weight: 900;">전화번호</label>
			      </td>
			      <td style="padding: 0 0 15px 15px;">
			        <p style="margin-bottom: 5px;">
			            <select class="selectField" id="mem_tel1" name="mem_tel1" >
			                <option value="">선 택</option>
			                <option value="010" ${dto.mem_tel1=="010" ? "selected='selected'" : ""}>010</option>
			                <option value="011" ${dto.mem_tel1=="011" ? "selected='selected'" : ""}>011</option>
			                <option value="016" ${dto.mem_tel1=="016" ? "selected='selected'" : ""}>016</option>
			                <option value="017" ${dto.mem_tel1=="017" ? "selected='selected'" : ""}>017</option>
			                <option value="018" ${dto.mem_tel1=="018" ? "selected='selected'" : ""}>018</option>
			                <option value="019" ${dto.mem_tel1=="019" ? "selected='selected'" : ""}>019</option>
			            </select>
			            -
			            <input type="text" name="mem_tel2" value="${dto.mem_tel2}" class="boxTF" maxlength="4">
			            -
			            <input type="text" name="mem_tel3" value="${dto.mem_tel3}" class="boxTF" maxlength="4">
			        </p>
			      </td>
			  </tr>
			  
			  <tr>
			      <td width="100" valign="top" style="text-align: right; padding-top: 15px;">
			            <label style="font-weight: 900;">대학 선택</label>
			      </td>
			      <td style="padding: 0 0 15px 15px;">
			        <p style="margin-bottom: 5px;">
			        	<select class="selectField" id="cal_code" name="cal_code" >
				        	<option value="">선 택</option>
				        	<c:forEach var="dto" items="${list}">   
				                <option value="${dto.cal_code}">${dto.cal_name}</option>
				            </c:forEach> 
	 			        	</select>
				        	학교 
					</p>
			        <p style="margin:0px;">올바른 정보를 작성하지 않을 경우, 발견 시 계정이 삭제될 수 있음을 주의하시기 바랍니다.</p>
			      </td>
			  </tr>
			  
			  <tr>
			  	<td style="padding: 0 0 15px 15px;">
				        <p style="margin-top: 7px; margin-bottom: 5px;">
				             <label style="font-weight: 900;">성별
				                 <input type="radio" name="mem_gen" value="${dto.mem_gen }">남자
								 <input type="radio" name="mem_gen" value="${dto.mem_gen }">여자
				             </label>
				        </p>
				</td>
			  </tr>
			  
			  <tr>
			      <td width="100" valign="top" style="text-align: right; padding-top: 15px;">
			            <label style="font-weight: 900;">주소</label>
			      </td>
			      <td style="padding: 0 0 15px 15px;">
			        <p style="margin-bottom: 5px;">
			            <input type="text" name="mem_add" value="${dto.mem_add}" maxlength="50" 
			                       class="boxTF" style="width: 95%;" placeholder="예) 경기도 파주시 아동동 정담길 20">
			        </p>
			      </td>
			  </tr>
			  
			  <c:if test="${mode=='mem_created'}">
				  <tr>
				      <td width="100" valign="top" style="text-align: right; padding-top: 5px;">
				            <label style="font-weight: 900;">약관동의</label>
				      </td>
				      <td style="padding: 0 0 15px 15px;">
				        <p style="margin-top: 7px; margin-bottom: 5px;">
				             <label>
				                 <input id="agree" name="agree" type="checkbox" checked="checked"
				                      onchange="form.sendButton.disabled = !checked"> <a href="#">이용약관</a>에 동의합니다.
				             </label>
				        </p>
				      </td>
				  </tr>
			  </c:if>
			  </table>
			  
			  <table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
			     <tr height="45"> 
			      <td align="center" >
			        <button type="button" name="sendButton" class="btn" onclick="memberOk();">${mode=="created"?"회원가입":"정보수정"}</button>
			        <button type="reset" class="btn">다시입력</button>
			        <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/';">${mode=="created"?"가입취소":"수정취소"}</button>
			      </td>
			    </tr>
			    <tr height="30">
			        <td align="center" style="color: blue;">${message}</td>
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











