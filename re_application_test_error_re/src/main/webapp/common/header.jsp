<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
	import="util.*, java.util.* , java.sql.* , notice.model.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link
	href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.css"
	rel="stylesheet">
<title>board_list</title>
</head>
<body>
	<div class="container">
		<nav class="navbar">
			<ul class="navbar nav">
				<li class="nav-item"><a class="nav-link"
					href="${pageContext.request.contextPath}/board/board_list.jsp">공지사항</a>
				</li>
				<li class="nav-item"><a class="nav-link" href="#">질문과답변</a></li>
				<li class="nav-item"><a class="nav-link" href="#">오시는 길</a></li>
				<li class="nav-item"><a class="nav-link" href="#">가시는 길</a></li>
			</ul>
		</nav>
	</div>
	<% request.setCharacterEncoding("UTF-8"); %>