<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
   String cp = request.getContextPath();
%>

<script src="http://code.jquery.com/jquery-1.12.0.min.js"></script>
<script type="text/javascript">
$(function(){
	var menu=$('nav>ul>li');

	menu.hover(function(){
			$(this).find('ul').slideDown().parent().siblings().children('ul').slideUp();
		}, function(){
			$('nav ul ul').slideUp(300);
	});
});
</script>

</head>

<body>
<div id="wrap">
<div id="header">
	  <div class="header-right">
        <div style="padding: 35px;  float: right;">
            <c:if test="${empty sessionScope.member}">
                <a href="<%=cp%>/member/login.do" style="color: #5d5d5d; font-size:14px;">로그인</a>
                    &nbsp;|&nbsp;
                <a href="<%=cp%>/member/member.do" style="color: #5d5d5d; font-size:14px;">회원가입</a>
            </c:if>
            <c:if test="${not empty sessionScope.member}">
                <span style="color:#1993A8;">${sessionScope.member.mem_name}</span>님
                    &nbsp;|&nbsp;
                    <a href="<%=cp%>/member/logout.do">로그아웃</a>
                    &nbsp;|&nbsp;
                    <a href="<%=cp%>/">정보수정</a>
            </c:if>
        </div>
    </div>
</div>
	<div id="logo"><img src="<%=cp%>/resource/images/COLORLOGO.png"></div>
	<div id="navWrap">
		<nav>
			<ul>
				<li>
					<a href="<%=cp%>/festival/list.do">Fetival</a>
					<ul>
						<li><a href="<%=cp%>/festival/list.do">축제일정</a></li>
						<li><a href="<%=cp%>/festivalevent/list.do">축제홍보</a></li>
						<li><a href="<%=cp%>/reviews/list.do">축제리뷰</a></li>
						<li><a href="<%=cp%>/tips/list.do">Tip</a></li>
						<li><a href="<%=cp%>/festivalgallery/list.do">축제갤러리</a></li>
					</ul>
				</li>
				<li>
					<a href="<%=cp%>/clublist/list.do">Club</a>
					<ul>
						<li><a href="<%=cp%>/clublist/list.do">동아리현황</a></li>
						<li><a href="<%=cp%>/Clubpublicize/list.do">동아리홍보</a></li>
						<li><a href="<%=cp%>/clubqa/list.do">동아리Q&amp;A</a></li>
					</ul>
				</li>
				<li>
					<a href="<%=cp%>/calboard/list.do">Community</a>
					<ul>
						<li><a href="<%=cp%>/calboard/list.do">대학별게시판</a></li>
						<li><a href="<%=cp%>/anonyboard/list.do">익명게시판</a></li>
					</ul>
				</li>
				<li>
					<a href="<%=cp%>/allim/list.do">Help</a>
					<ul>
						<li><a href="<%=cp%>/allim/list.do">공지사항</a></li>
						<li><a href="<%=cp%>/qabbs/list.do">질문과답변</a></li>
					</ul>
				</li>
				<li>
					<a href="<%=cp%>/admin/powerList.do">Admin</a>
					<ul>
						<li><a href="<%=cp%>/admin/uniList.do">학교관리</a></li>
						<li><a href="<%=cp%>/admin/powerList.do">권한관리</a></li>
						<li><a href="<%=cp%>/admin/clubList.do">동아리관리</a></li>
					</ul>
				</li>
			</ul>
		</nav>
	</div>
</div>

