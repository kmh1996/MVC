<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="../../common/header.jsp"/>
<section>
	<table>
		<tr>
			<th colspan="2"><h2>게시물 상세내용</h2></th>
		</tr>
		<tr>
			<td>작성자</td>
			<td>${boardVO.qna_name}</td>
		</tr>
		<tr>
			<td>제목</td>
			<td><c:out value="${boardVO.qna_title}"/></td>
		</tr>
		<tr>
			<td>내용</td>
			<td>
				<div>${boardVO.qna_content}</div>
			</td>
		</tr>
		<c:if test="${!empty boardVO.qna_file}">
			<tr>
				<td>첨부파일</td>
				<td>
					<a href="file_down.bo?qna_file=${boardVO.qna_file}">${boardVO.qna_file_origin}</a>
					<hr/>
					<a href="resources/upload/${boardVO.qna_file}" 
					   download="${boardVO.qna_file_origin}">
						${boardVO.qna_file_origin}
					</a>
				</td>
			</tr>
		</c:if>
		<tr>
			<td colspan="2">
				<c:if test="${!empty member}">
					<a href="boardReplyForm.bo?qna_num=${boardVO.qna_num}">[답글]</a>
					<c:if test="${boardVO.qna_writer_num eq member.num}">
						<a href="boardUpdateForm.bo?qna_num=${boardVO.qna_num}">[수정]</a>
						<a href="boardDelete.bo?qna_num=${boardVO.qna_num}">[삭제]</a>
					</c:if>
				</c:if>
				<a href="boardList.bo">[목록]</a>
			</td>
		</tr>
	</table>
</section>
<%-- <jsp:include page="../comment/comment.jsp"/> --%>
<jsp:include page="../comment/commentAjax.jsp"/>
<jsp:include page="../../common/footer.jsp"/>









