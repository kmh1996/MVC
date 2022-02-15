<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link href="resources/css/comment.css" 
rel="stylesheet" 
type="text/css" />

<!-- board/comment/commentAjax.jsp -->
<c:if test="${!empty sessionScope.member}">
	<div class="commentWrap">
		<h3>댓글 작성</h3>
		<br/>
		<div class="commentWriteWrap">
			<textarea required id="comment_content" class="comment_content"></textarea>
			<input type="button" id="commentWriteBtn" value="등록"/>
		</div>
	</div>
</c:if>
<br/>
<!-- 댓글 목록 -->
<h3 id="comment_title">댓글목록</h3>
<div id="comments" class="comments">
</div>
<!-- 댓글 페이징 처리 -->
<div id="pagingWrap" class="pagingWrap">
</div>

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script>
	var page = 1;
	getList(page);
	
	
	// 매개변수로 page번호를 넘겨받아 리스트 정보 호출
	function getList(pageNum){
		$.ajax({
			type : 'GET',
			url : 'list.co',
			data : {
				qna_num : '${boardVO.qna_num}',
				page : pageNum
			},
			dataType : 'json',
			success : function(result){
				console.log(result);
				console.log(result.list);
				console.log(result.pm);
				let totalCount = result.pm.totalCount;
				$("#comment_title").html("댓글 목록-["+totalCount+"]");
				
				page = result.pm.cri.page;
				
				printList(result.list);
				printPageMaker(result.pm);
			}
		});
	}
	
	// list 정보를 넘겨받아 comments 리스트 출력
	function printList(list){
		var html ="";
		$(list).each(function(){
			// this == CommentVO
			html +='<div class="commentListWrap">';
			if(this.comment_delete == 'N'){
				if('${!empty member}' && '${member.id}' == this.comment_id){
					html += "<div class='closeBtn' onclick='deleteComment("+this.comment_num+");'>";
					html += "X";
					html += "</div>";
				}
				html +='<div>';
				html += this.comment_name+' | '+getDate(this.comment_date);
				html +='</div>';
				html +='<br/>';
				html +='<div>';
				html +='<textarea readonly>'+this.comment_content+'</textarea>';
				html +='</div>';
			}else{
				html +='<div>';
				html += this.comment_name+' | '+getDate(this.comment_date);
				html +='</div>';
				html +='<br/>';
				html +='<div>';
				html+="<h3>삭제된 게시물 입니다.</h3>";
				html +='</div>';
			}
			html +='</div>';
		});
		console.log(html);
		$("#comments").html(html);
	}	
	
	// 날짜 포맷 변경
	function getDate(date){
		var dt = new Date(date);
		
		var year = dt.getFullYear();
		var month = dt.getMonth()+1; // 1~12
		var day = dt.getDate();		 // 1~31
		
		if(month < 10) month = "0" + month;
		if(day < 10) day = "0" + day;
		
		var hour = dt.getHours();
		var minute = dt.getMinutes();
		var second = dt.getSeconds();
		
		if(hour < 10) hour = "0" + hour;
		if(minute < 10) minute = "0" + minute;
		if(second < 10) second = "0" + second;
		console.log(hour);
		
		// 작성 날짜가 오늘이면 시간만 출력
		// 아니면 날짜만 출력
		var today = new Date();			// 현재 시간
		var date = today.toDateString();
		console.log(date);
		console.log(dt.toDateString());	// 댓글 작성 시간
		if(date == dt.toDateString()){
			// 오늘 작성
			return hour+":"+minute+":"+second;
		}else{
			// 다른 날짜에 작성
			return year+"-"+month+"-"+day; 
		}
		//return year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
	}
	
	
	// pageMaker 정보로 페이징 블럭 출력
	function printPageMaker(pm){
		console.log(pm);
		var html = "";
		if(pm.start){
			html+="<a href='javascript:getList(1);'>[처음]</a>";	
		}
		if(pm.prev){
			html+="<a href='javascript:getList("+(pm.startPage-1)+");'>[이전]</a>";	
		}
		for(var i = pm.startPage; i <= pm.endPage; i++){
			if(pm.cri.page == i){
				html+="<span style='color:red;'>["+i+"]</span>";
			}else{
				html+="<a href='javascript:getList("+i+");'>["+i+"]</a>";
			}
		}
		if(pm.next){
			html+="<a href='javascript:getList("+(pm.endPage+1)+");'>[다음]</a>";	
		}
		if(pm.last){
			html+="<a href='javascript:getList("+(pm.maxPage)+");'>[마지막]</a>";	
		}
		
		$("#pagingWrap").html(html);
	}
	
	
	
	// 댓글 번호로 댓글 삭제 요청 처리
	function deleteComment(comment_num){
		if(confirm(comment_num+"번 댓글을 삭제하시겠습니까? ")){
			// 삭제 처리
			$.ajax({
				type : 'POST',
				url : 'commentDelete.co',
				data : {
					id : '${member.id}',
					comment_num : comment_num
				},
				dataType : 'json',
				success : function(data){
					if(data == true){
						alert(comment_num +' 댓글 삭제 성공');
						getList(page);
					}else{
						alert('게시물 삭제 실패');
					}
				}
			});
		}
	}
	
	// 댓글 삽입 처리
	$("#commentWriteBtn").click(function(){
		var obj = {
			type : 'POST' ,
			url : 'commentWrite.co' ,
			data : {
				id :  '${member.id}',
				name : '${member.name}',
				comment_content : $("#comment_content").val(),
				qna_num : '${boardVO.qna_num}'
			},
			dataType : 'json',
			success : function(data){
				if(data == true){
					alert('댓글 삽입 성공- '+ data);
					$("#comment_content").val(null);
					page = 1;
					getList(page);
				}else{
					alert('댓글 삽입 실패-'+data);
				}
				$("#comment_content").focus();
			}
		};
		$.ajax(obj);
	});
	
	setInterval(function(){
		getList(1);
	},1000);
	
</script>











