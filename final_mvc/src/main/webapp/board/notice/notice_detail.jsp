<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- notice_detail.jsp -->
<jsp:include page="../../common/header.jsp"/>
<section>
	<table>
		<tr>
			<th colspan="2"><h1>공지글 상세 보기</h1></th>
		</tr>
		<tr>
			<td>작성자</td>
			<td>${notice.notice_author}</td>
		</tr>
		<tr>
			<td>카테고리</td>
			<td>
				${notice.notice_category}
			</td>
		</tr>
		<tr>
			<td>제목</td>
			<td>
				${notice.notice_title}
			</td>
		</tr>
		<tr>
			<td>내용</td>
			<td>
				<div style="width:600px;min-height:300px;overflow:scroll;">
					${notice.notice_content}
				</div>
			</td>
		</tr>
		<tr>
			<td>작성일</td>
			<td>${notice.notice_date}</td>
		</tr>
		<c:if test="${!empty sessionScope.member && member.id eq 'admin'}">
		<tr>
			<td colspan="2">
				<input type="button" onclick="location.href='noticeUpdateForm.do?notice_num=${notice.notice_num}';" value="수정"/>
				<input type="button" onclick="location.href='noticeDelete.do?notice_num=${notice.notice_num}';" value="삭제"/>
			</td>
		</tr>
		</c:if>
	</table>
</section>
<jsp:include page="../../common/footer.jsp"/>





