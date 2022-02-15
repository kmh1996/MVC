<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="../common/header.jsp"%>
<%@ include file="board_info.jsp"%>
<div class="container">
	<div>
		<h1>게시글 상세</h1>
	</div>
	<div class="row  m-5">
		<div class="col-md-2">
			<label class="form-control-plaintext">카테고리</label>
		</div>
		<div class="col-md-4">
			<label class="form-control-plaintext">${notice.notice_category}</label>
		</div>
		<div class="col-md-2">
			<span class="form-control-plaintext text-center">작성자</span>
		</div>
		<div class="col-md-4">
			<span class="form-control-plaintext">${notice.notice_author}</span>
		</div>
	</div>
	<div class="row m-5">
		<div class="col-md-2">
			<span class="form-control-plaintext">제목</span>
		</div>
		<div class="col-md-10">${notice.notice_title}</div>
	</div>

	<div class="row m-5">
		<div class="col-md-2">
			<span class="form-control-plaintext">내용</span>
		</div>
		<div class="col-md-10">
			<textarea name="notice_content" class="form-control" rows="10"
				readonly>${notice.notice_content}</textarea>
		</div>
	</div>
	<div class="row m-5">
		<div class="col-md-4">
			<a href="board_update.jsp?notice_num=${notice.notice_num}"
				class="form-control btn btn-warning">수정</a>
		</div>
		<div class="col-md-4">
			<a href="#" onclick="deleteNotice('${notice.notice_num}',event);"
				class="form-control btn btn-danger">삭제</a>
		</div>
		<div class="col-md-4">
			<a href="board_list.jsp?notice_num=${notice.notice_num}"
				class="form-control btn btn-primary">목록</a>
		</div>
	</div>
</div>
<script>
	function deleteNotice(notice_num,event){
		event.preventDefault();
		if(confirm(notice_num+" 게시물을 삭제하시겠습니까?")){
			location.href='board_delete.jsp?notice_num='+notice_num;
		}
	}
</script>
<%@ include file="../common/footer.jsp"%>