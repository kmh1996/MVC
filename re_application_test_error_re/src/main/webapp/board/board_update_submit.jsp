<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="../common/header.jsp"%>
<%!
	Connection conn = null;
	PreparedStatement pstmt = null;
%>
<%

	int notice_num = Integer.parseInt(request.getParameter("notice_num"));

	String notice_author = request.getParameter("notice_author");
	String notice_category = request.getParameter("notice_category");
	String notice_title = request.getParameter("notice_title");
	String notice_content = request.getParameter("notice_content");
	
	conn = DBCPUtil.getConnection();
	
	String sql = "UPDATE notice_board SET notice_category =?, " 
			    +" notice_author = ? ,"
	    		+" notice_title = ? , " 
   				+" notice_content = ?, " 
				+" WHERE notice_num = ?";
	
	pstmt = conn.prepareStatement(sql);
	pstmt.setString(1,notice_category);
	pstmt.setString(2,notice_author);
	pstmt.setString(3,notice_title);
	pstmt.setString(4,notice_content);
	pstmt.setInt(5,notice_num);
	
	out.print("<script>");
	if(pstmt.executeUpdate() > 0){
		out.print("alert('공지 수정 완료');");
		out.print("location.href='board_detail.jsp?notice_num="+notice_num+"';");
	}else{
		out.print("alert('공지 수정 실패');");
		out.print("history.back();");
	}
	out.print("</script>");
%>