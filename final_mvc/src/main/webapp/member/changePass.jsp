<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- changePass.jsp -->
<jsp:include page="../common/header.jsp"/>
<section>
	<article>
		<h1>비밀번호 변경</h1>
		<form action="chagePass.mc" method="POST">
			<input type="hidden" name="id" value="${id}"/>
			<input type="hidden" name="code" value="${code}"/>
			<input type="password" name="pass" required/>
			<input type="submit" value="변경"/>
		</form>
	</article>
</section>
<jsp:include page="../common/footer.jsp"/>










