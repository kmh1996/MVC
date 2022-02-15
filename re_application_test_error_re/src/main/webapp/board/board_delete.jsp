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

	String sql = "DELETE FROM notice_board WHERE noitce_num = ? ";
	
	pstmt = conn.prepareStatement(sql);
	pstmt.setInt(1,notice_num);
	
	out.print("<script>");
	if(pstmt.executeUpdate() > 0){
		out.print("alert('공지 삭제 완료');");
		out.print("location.href='board_list.jsp';");
	}else{
		out.print("alert('공지 삭제 실패');");
		out.print("history.back();");
	}
	out.print("</script>");
	
	DBCPUtil.close(pstmt,conn);
%>
