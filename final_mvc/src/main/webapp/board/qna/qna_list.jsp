<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="../../common/header.jsp"/>
<!-- qna_list.jsp -->
<section>
	<table>
		<tr>
			<th colspan="8"><h2>질문과 답변 목록</h2></th>
		</tr>
		<c:if test="${!empty member}">
			<tr>
				<td colspan="8">
					<a href="boardWrite.bo">질문 작성하러 가기</a>
				</td>
			</tr>
		</c:if>
		<!--
			 request 질문과 답변 목록
			 ${list} 
		 -->
		 <tr>
		 	<th>글번호</th>
		 	<th>제목</th>
		 	<th>ref</th>
		 	<th>seq</th>
		 	<th>lev</th>
		 	<th>작성자</th>
		 	<th>작성시간</th>
		 	<th>조회수</th>
		 </tr>
		<c:choose>
			<c:when test="${!empty list}">
				<!-- 목록 출력 -->
				<c:forEach var="board" items="${list}">
					<c:choose>
						<c:when test="${board.qna_delete eq 'N'}">
							<tr>
								<td>${board.qna_num}</td>
								<td>
									<c:if test="${board.qna_re_lev != 0}">
										<c:forEach var="i" begin="1" end="${board.qna_re_lev}">
											&nbsp;&nbsp;&nbsp;
										</c:forEach>
										<!-- ㅁ + 한자 -->
										▶
									</c:if>
									<a href="boardDetail.bo?qna_num=${board.qna_num}">
										<c:out value="${board.qna_title}" escapeXml="true"/>
									</a>
								</td>
								<td>${board.qna_re_ref}</td>
								<td>${board.qna_re_seq}</td>
								<td>${board.qna_re_lev}</td>
								<td>${board.qna_name}</td>
								<td>${board.qna_date}</td>
								<td>${board.qna_readcount}</td>
							</tr>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="8">
									삭제된 게시물 입니다.
								</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<c:if test="${not empty pm}">
					<tr>
						<td colspan="8">
							<c:if test="${pm.prev}">
								<a href="boardList.bo?page=${pm.startPage-1}">[이전]</a>
							</c:if>
							<c:forEach var="i" begin="${pm.startPage}" end="${pm.endPage}">
								<c:choose>
									<c:when test="${i eq pm.cri.page}">
										<span>[${i}]</span>
									</c:when>
									<c:otherwise>
										<a href="boardList.bo?page=${i}">[${i}]</a>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							<c:if test="${pm.next}">
								<a href="boardList.bo?page=${pm.endPage+1}">[다음]</a>
							</c:if>
						</td>
					</tr>
				</c:if>
			</c:when>
			<c:otherwise>
				<tr><th colspan="5">등록된 게시물이 없습니다.</th></tr>
			</c:otherwise>
		</c:choose>
		<!-- 페이징 블럭 -->
	</table>
</section>
<jsp:include page="../../common/footer.jsp"/>















