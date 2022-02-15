package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.MemberService;
import service.MemberServiceImpl;

public class MemberServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	MemberService ms = new MemberServiceImpl();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		MemberService.loginCheck(request);
		
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String command = requestURI.substring(contextPath.length()+1);
		//if(requestURI.equals(request.getContextPath()+"/login.do")) {
		
		String nextPage = null;
		if(command.equals("login.do")) {
			nextPage = "/member/login.jsp";
		}else if(command.equals("join.do")) {
			nextPage = "/member/join.jsp";
		}else if(command.equals("joinSubmit.do")) {
			ms.memberJoin(request, response);
		}else if(command.equals("loginSubmit.do")) {
			boolean isCheck = ms.memberLogin(request, response);
			if(isCheck) {
				response.sendRedirect(request.getContextPath());
			}else {
				response.sendRedirect(request.getContextPath()+"/login.do");
			}
		}else if(command.equals("logOut.do")) {
			ms.logOut(request, response);
			nextPage = "/common/main.jsp";
		}
		
		if(nextPage != null) {
			request.getRequestDispatcher(nextPage)
			.forward(request, response);
		};
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}






