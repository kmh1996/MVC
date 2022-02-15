<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- /test/javascript.jsp -->
<jsp:include page="../common/header.jsp"/>
<section>
	<div>
		<h1>자바스크립트</h1>
		<input type="text" name="name" id="name"/> <br/>
		<input type="number" name="age" id="age"/> <br/>
		<input type="button" id="btn" value="확인"/> <br/>
		<div id="box"></div>
		<div>
			<table border="1" id="boxTable">
				<tr>
					<th>이름</th>
					<th>나이</th>
				</tr>
				
			</table>
		</div>
	</div>
</section>
<script>
	/*
		Ajax Asynchronous Javascript And XML 의 약자
		전체 페이지를 다시 로드 하지 않고 부분적으로 서버와 데이터를 교환하는 것.
		 동기적으로 처리한다는 것
		 요청을 하고 응답이 오기를 기다렸다가 다음 작업을 처리하는 방식.
		 
		 비동기적
		 요청을 보내고
		 응답 결과가 전달되기 전에 바로 다음일을 수행하는 방식
		 
		 ajax 이 핵심 기술은 마소에서 개발한 XMLHttpRequest 
	*/
	
	var httpRequest;
	document.getElementById("btn").onclick = function(){
		console.log('makeRequest function 호출');
		makeRequest('user.test');
		console.log('makeRequest function 종료');
		
		console.log('test function 호출');
		testFunc();
		console.log('test function 종료');
	};
	
	function testFunc(){
		for(var i=1; i<100; i++){
			console.log("testFunc : "+i);
		}
	}
	
	function makeRequest(url){
		httpRequest = new XMLHttpRequest();
		if(!httpRequest){
			alert('XMLHttp 객체 생성 실패');
			return false;
		}
		
		console.log('생성완료');
		
		var name = document.getElementById("name").value;
		var age = document.getElementById("age").value;
		
		console.log(httpRequest.readyState);
		httpRequest.onreadystatechange = setContents;			
		httpRequest.open('GET',url+"?name="+name+"&age="+age,false);
		httpRequest.send();
	}
	
	function setContents(){
		/*
			0 (uninitialized) - request가 아직 초기화 되지 않음
			1 (loading)       - 서버와 연결이 성사됨
			2 (loaded)		  - 서버가 request를 받음
			3 (interactive)	  - request에 대해서 처리하는 중
			4 (complate)      - request에 대한 처리가 끝났고 
					            응답을 할 준비가 완료됨
		*/
		console.log(httpRequest.readyState);
		console.log(httpRequest.status);
		if(httpRequest.readyState === 4){
			if(httpRequest.status === 200){
			// httpRequest.responseText - 서버의 응답을 텍스트 문자열로 반환
			// httpRequest.responseXML - 서버의 응답을 XML Document 반환
			var result = httpRequest.responseText;
			console.log("result : " + result);
			document.getElementById("box").innerHTML = result;
			// JSON 형태의 문자열 데이터를 javascript object 타입으로 변환
			var obj = JSON.parse(result);
			console.log(obj);
			printBox(obj);
			}else{
				console.log('실패');
			}
		}
	}

	function printBox(obj){
		var html = "";
		html += "<td>";
		html += obj.name;
		html +="</td>";
		html += "<td>";
		html += obj.age;
		html +="</td>";
		var trElement = document.createElement("tr");
		trElement.innerHTML = html;
		document.getElementById("boxTable").appendChild(trElement);
	}
	

</script>
<jsp:include page="../common/footer.jsp"/>









