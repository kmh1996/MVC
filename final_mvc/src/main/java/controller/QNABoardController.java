package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.CommentService;
import service.CommentServiceImpl;
import service.MemberService;
import service.QNABoardService;
import service.QNABoardServiceImpl;
import vo.BoardVO;

@WebServlet("*.bo")
public class QNABoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	QNABoardService service = new QNABoardServiceImpl();
	CommentService cs = new CommentServiceImpl();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		// 자동 로그인 쿠키 체크
		MemberService.loginCheck(request);
		
		String command = request.getRequestURI()
				.substring(request.getContextPath().length()+1);
		
		String nextPage = null;
		
		if(command.equals("boardList.bo")) {
			// 목록 페이지 요청
			ArrayList<BoardVO> list = service.getBoardList(request);
			request.setAttribute("list", list);
			nextPage = "/board/qna/qna_list.jsp";
		}else if(command.equals("boardWrite.bo")) {
			// 목록 작성 페이지 요청
			nextPage = "/board/qna/qna_write.jsp";
			
		}else if(command.equals("boardWriteSubmit.bo")) {
			System.out.println("게시글 등록 요청-원본글");
			// 일반 for data 게시글 등록
			//service.boardWrite(request);
			// 첨부파일과 함께 게시글 등록
			service.boardWriteFile(request);
			response.sendRedirect("boardList.bo");
		}else if(command.equals("boardDetail.bo")) {
			// 게시글 상세보기 요청
			// 조회수 증가
			service.updateReadCount(request);
			String page = "boardRead.bo?qna_num="+request.getParameter("qna_num");
			System.out.println("detail : " + page);
			response.sendRedirect(page);
		}else if(command.equals("boardRead.bo")) {
			// 게시물 정보 전달
			BoardVO vo = service.getBoardVO(request);
			request.setAttribute("boardVO", vo);
			
			// 게시글의 댓글 정보
			Map<String , Object> hm = cs.getCommentList(request);
			request.setAttribute("hm", hm);
			
			nextPage = "/board/qna/qna_detail.jsp";
		}else if(command.equals("boardReplyForm.bo")) {
			// 답변글 작성 페이지 요청
			// 답변을 달려는 원본글의 정보를 가지고 옴.
			BoardVO boardVO = service.boardReply(request);
			request.setAttribute("boardVO", boardVO);
			// 답변글 작성 페이지
			nextPage = "/board/qna/qna_reply.jsp";
		}else if(command.equals("boardReplySubmit.bo")) {
			// 답변글 작성 요청
			service.boardReplySubmit(request);
			response.sendRedirect("boardList.bo");
		}else if(command.equals("boardUpdateForm.bo")) {
			// 게시글 수정 페이지 요청
			BoardVO board = service.getBoardVOByUpdate(request);
			request.setAttribute("boardVO", board);
			nextPage = "/board/qna/qna_update.jsp";
		}else if(command.equals("boardUpdate.bo")) {
			// 게시글 수정 요청
			service.boardUpdate(request, response);
		}else if(command.equals("boardDelete.bo")) {
			// 게시글 삭제 요청
			service.boardDelete(request, response);
		}else if(command.equals("file_down.bo")) {
			System.out.println("file 다운로드 요청");
			service.fileDown(request, response);
		}else if(command.equals("commentWrite.bo")) {
			// 댓글 삽입 요청
			String qna_num = request.getParameter("qna_num");
			boolean isSuccess = cs.insertComment(request);
			if(isSuccess) {
				// 삽입 성공
				response.sendRedirect("boardDetail.bo?qna_num="+qna_num);
			}else {
				response.setContentType("text/htm;charset=utf-8");
				PrintWriter out = response.getWriter();
				out.print("<script>");
				out.print("alert('삽입 실패');");
				out.print("history.back();");
				out.print("</script>");
			}
			return;
		}else if(command.equals("commentDelete.bo")) {
			// 댓글 삭제 요청
			String qna_num = request.getParameter("qna_num");
			boolean isSuccess = cs.deleteComment(request);
			if(isSuccess) {
				response.sendRedirect("boardRead.bo?qna_num="+qna_num);
			}else {
				response.setContentType("text/htm;charset=utf-8");
				PrintWriter out = response.getWriter();
				out.print("<script>");
				out.print("alert('삭제 실패');");
				out.print("history.back();");
				out.print("</script>");
			}
			return;
		}
		
		if(nextPage != null) {
			request.getRequestDispatcher(nextPage)
			.forward(request, response);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}








