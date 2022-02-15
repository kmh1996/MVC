package service;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.MemberDAO;
import dao.MemberDAOImpl;
import vo.MemberVO;

public interface MemberService {
	
	// 회원 가입 처리
	void memberJoin(HttpServletRequest requst,
			HttpServletResponse response) throws IOException;
	
	// 로그인 처리
	/**
	 * @return true = 로그인성공 , false 로그인실패
	 */
	boolean memberLogin(HttpServletRequest requst,
			HttpServletResponse response);
	
	// 로그아웃 처리
	void logOut(HttpServletRequest requst,
			HttpServletResponse response);
	
	public static void loginCheck(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("member") != null) {
			System.out.println("이미 로그인");
			return;
		}
		
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			System.out.println("쿠키 존재");
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals("id")) {
					System.out.println("id 존재");
					String value = cookie.getValue();
					System.out.println("value : " + value);
					MemberDAO dao = new MemberDAOImpl();
					MemberVO vo = dao.getMemberById(value);
					if(vo != null) {
						System.out.println("vo : " + vo);
						session.setAttribute("member", vo);
					}
					break;
				}
			}
		}
	}
}













