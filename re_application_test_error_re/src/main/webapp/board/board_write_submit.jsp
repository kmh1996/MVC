<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="../common/header.jsp"%>
<%!
	Connection conn = null;
	PreparedStatement pstmt = null;
%>
<%
	String notice_author = request.getParameter("notice_author");
	String notice_category = request.getParameter("notice_category");
	String notice_title = request.getParameter("notice_title");
	String notice_content = request.getParameter("notice_content");
	
	conn = DBCPUtil.getConnection();
	
	String sql = "INSERT INTO notice_board VALUES(null,?,?,?,now())";
	
	pstmt = conn.prepareStatement(sql);
	
	pstmt.setString(1,notice_category);
	pstmt.setString(2,notice_author);
	pstmt.setString(3,notice_title);
	pstmt.setString(4,notice_content);

	out.print("<script>");
	if(pstmt.executeUpdate() > 0){
		out.print("alert('공지 등록 완료');");
		out.print("location.href='board_list.jsp';");
	}else{
		out.print("alert('공지 등록 실패');");
		out.print("history.back();");
	}
	out.print("</script>");
	
	DBCPUtil.close(pstmt,conn);
%>

