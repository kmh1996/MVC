package service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.MemberDAO;
import dao.MemberDAOImpl;
import vo.MemberVO;

public class MemberServiceImpl implements MemberService {

	MemberDAO dao = new MemberDAOImpl();
	
	@Override
	public void memberJoin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String id = request.getParameter("id");
		String pass = request.getParameter("pass");
		String name = request.getParameter("name");
		
		MemberVO vo = new MemberVO(id,pass,name);
		// database 삽입 요청
		boolean isCheck = dao.memberJoin(vo);
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print("<script>");
		if(isCheck) {
			out.print("alert('회원가입 성공');");
			out.print("location.href='login.do';");
		}else {
			out.print("alert('회원가입 실패');");
			out.print("history.back();");
		}
		out.print("</script>");
	}

	@Override
	public boolean memberLogin(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String pass = request.getParameter("pass");
		String check = request.getParameter("check");
		
		MemberVO member = dao.memberLogin(id,pass);
		if(member != null) {
			HttpSession session = request.getSession();
			session.setAttribute("member", member);
			if(check != null) {
				Cookie cookie = new Cookie("id",member.getId());
				cookie.setMaxAge(60*60*24*7);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
			return true;
		}
		return false;
	}

	@Override
	public void logOut(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		session.removeAttribute("member");
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals("id")) {
					cookie.setMaxAge(0);
					cookie.setPath("/");
					response.addCookie(cookie);
					break;
				}
			}
		}
	}
}











