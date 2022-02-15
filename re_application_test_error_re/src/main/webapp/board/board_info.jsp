<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
	import="util.*, java.util.* , java.sql.* , notice.model.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%!
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	NoticeBoard notice = null;
%>

<%
	request.setCharacterEncoding("UTF-8");

	int notice_num = Integer.parseInt(request.getParameter("notice_num"));
	conn = DBCPUtil.getConnection();
	String sql = "SELECT * FROM notice_board WHERE notice_num = ?";
	pstmt = conn.prepareStatement(sql);
	pstmt.setInt(1,notice_num);
	rs = pstmt.executeQuery();
	
	while(rs.next()){
		notice = new NoticeBoard();
		notice.setNotice_num(rs.getInt("notice_num"));
		notice.setNotice_category(rs.getString("notice_category"));
		notice.setNotice_author(rs.getString("notice_author"));
		notice.setNotice_title(rs.getString("notice_title"));
		notice.setNotice_content(rs.getString("notice_content"));
		notice.setNotice_date(rs.getTimestamp("notice_date"));
	}
	
	pageContext.setAttribute("notice",notice);

%>
