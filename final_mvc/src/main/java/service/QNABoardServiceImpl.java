package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import dao.QNABoardDAO;
import dao.QNABoardDAOImpl;
import util.Criteria;
import util.DBCPUtil;
import util.PageMaker;
import vo.BoardVO;
import vo.MemberVO;

public class QNABoardServiceImpl implements QNABoardService {
	
	String saveDir = "/resources/upload";
	
	QNABoardDAO dao = new QNABoardDAOImpl();

	@Override
	public ArrayList<BoardVO> getBoardList(HttpServletRequest request) {
		String page = request.getParameter("page");
		int paramPage = 1;
		if(page != null) {
			paramPage = Integer.parseInt(page);
		}
		// qna_board 전체 게시물 수 검색
		int totalCount = dao.getTotalCount();
		System.out.println("totalCount : " + totalCount);
		
		Criteria cri = new Criteria(paramPage, 10);
		PageMaker pm = new PageMaker();
		pm.setCri(cri);
		pm.setTotalCount(totalCount);
		request.setAttribute("pm", pm);
		ArrayList<BoardVO> list = dao.getBoardList(cri);
		return list;
	}

	@Override
	public void boardWrite(HttpServletRequest request) {
		String qna_name = request.getParameter("qna_name");
		int qna_writer_num = Integer.parseInt(
			request.getParameter("qna_writer_num")
		);
		String qna_title = request.getParameter("qna_title");
		String qna_content = request.getParameter("qna_content");
		
		BoardVO board = new BoardVO();
		board.setQna_name(qna_name);
		board.setQna_writer_num(qna_writer_num);
		board.setQna_title(qna_title);
		board.setQna_content(qna_content);
		
		dao.boardWrite(board);
	}

	@Override
	public void boardWriteFile(HttpServletRequest request) {
		// 파일 정보와 함께 게시글 정보 등록
		String realPath = 
		request.getServletContext().getRealPath(saveDir);
		System.out.println("realPath : "+realPath);
		File f = new File(realPath);
		if(!f.exists()) {
			f.mkdirs();
			System.out.println("폴더 생성 완료");
		}
		
		try {
			MultipartRequest multi = new MultipartRequest(
				request,
				realPath,
				1024*1024*50,		// MAX POST SIZE 50MB
				"utf-8",
				new DefaultFileRenamePolicy()
			);  
			// img.png 
			// img1.png
			String qna_name = multi.getParameter("qna_name");
			int qna_writer_num
			= Integer.parseInt(multi.getParameter("qna_writer_num"));
			String qna_title = multi.getParameter("qna_title");
			String qna_content = multi.getParameter("qna_content");
			
			String file = (String)multi.getFileNames().nextElement();
			System.out.println("name : "+file);
			// 업로드 된 파일 이름
			String qna_file = multi.getFilesystemName(file);
			String qna_file_origin = multi.getOriginalFileName(file);
			BoardVO vo = new BoardVO();
			vo.setQna_name(qna_name);
			vo.setQna_title(qna_title);
			vo.setQna_content(qna_content);
			vo.setQna_writer_num(qna_writer_num);
			vo.setQna_file(qna_file);
			vo.setQna_file_origin(qna_file_origin);
			System.out.println(vo);
			
			dao.boardWrite(vo);
			
		} catch (Exception e) {}
	}

	
	
	
	
	@Override
	public BoardVO getBoardVO(HttpServletRequest request) {
		int qna_num 
		= Integer.parseInt(request.getParameter("qna_num"));
		return dao.getBoardVO(qna_num);
	}

	@Override
	public void updateReadCount(HttpServletRequest request) {
		int qna_num 
		= Integer.parseInt(request.getParameter("qna_num"));
		dao.updateReadCount(qna_num);
	}

	@Override
	public void fileDown(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("file down 요청 Service");
		
		String realPath 
		= request.getServletContext().getRealPath(saveDir);
		String fileName = request.getParameter("qna_file");
		System.out.println("fileName : " + fileName);
		// \\     /
		String filePath = realPath+File.separator+fileName;
		System.out.println("filePath : "+filePath);
		
		String mimeType 
		= request.getServletContext().getMimeType(filePath);
		System.out.println("mimeType : " + mimeType);
		
		if(mimeType == null) {
			// 8비트 바이너리 배열의 의미
			// http 또는 이메일 상에서 application 형식이
			// 지정되지 않았거나 형식을 모를 때 사용
			// 브라우저는 actet-stream 으로 MIMEType이 
			// 지정된 경우 단지 바이너리 데이터로서 다운로드만
			// 가능하게 처리된다.
			mimeType = "application/actet-stream";
		}
		response.setContentType(mimeType);
		// 사용자의 브라우저 정보확인
		try {
			String agent = request.getHeader("User-Agent");
			boolean isBrowser 
			= (agent.indexOf("MSIE") > -1 || agent.indexOf("Trident") > -1);
			// 브라우저에 따라 한글 파일명 인코딩을 지정
			if(isBrowser) {
				// IE 일 경우 인코딩을 URL이 이해 할 수 있는 인코딩으로 변경
				// 공백 표현을 이스케이프 문자열을 이용하여 변환
				// 공백을 + 표현 -> %20(ASCII 공백을 표현)으로 변경
				fileName = URLEncoder.encode(fileName,"utf-8")
						.replaceAll("\\+", "%20");
				// 운영체제마다 일부문자를 인식하는게 상이합니다.
				// URLEncoder 클래스는 일반 문자열을 웹에서 사용하는
				// x-www-form-urlcoded 방식으로 변환하는 역할을 담당
			}else {
				// IE가 아닐 경우 브라우저에 인코딩된(utf-8) 형태의 URL 값을
				// latin1 == ISO-8859-1 형태로 변경해야 한다.
				fileName = new String(fileName.getBytes("utf-8"),"iso-8859-1");
			}
			System.out.println("fileName : " + fileName);
			// Content-Disposition 헤더는 본문 부분에 대한 표시 정보를 제공
			// 이 헤더를 첨부파일에 추가하여 첨부파일의 본문 부분을 표시할지
			// 복사할 파일 이름으로 표시 할지를 지정
			response.setHeader("Content-Disposition", "attachment;fileName="+fileName);
			// 본문내용을 바로 표시
			//response.setHeader("Content-Disposition", "inline");
			
			// 이하는 전달할 파일 정보를 읽어와서 client에 출력한다.
			FileInputStream fis = new FileInputStream(filePath);
			OutputStream out = response.getOutputStream();
			int numRead;
			byte[] bytes = new byte[4096];
			while((numRead = fis.read(bytes,0,bytes.length)) != -1) {
				out.write(bytes,0,numRead);
			}
			out.flush();
			DBCPUtil.close(out,fis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public BoardVO boardReply(HttpServletRequest request) {
		int qna_num = 
			Integer.parseInt(request.getParameter("qna_num"));
		return dao.getBoardVO(qna_num);
	}

	@Override
	public BoardVO boardReplySubmit(HttpServletRequest request) {
		int qna_writer_num = Integer.parseInt(
			request.getParameter("qna_writer_num")
		);
		String qna_name = request.getParameter("qna_name");
		String qna_title = request.getParameter("qna_title");
		String qna_content = request.getParameter("qna_content");
		int qna_re_ref = Integer.parseInt(
				request.getParameter("qna_re_ref")
			);
		int qna_re_lev = Integer.parseInt(
				request.getParameter("qna_re_lev")
			);
		int qna_re_seq = Integer.parseInt(
				request.getParameter("qna_re_seq")
			);
		BoardVO board = new BoardVO();
		board.setQna_name(qna_name);
		board.setQna_title(qna_title);
		board.setQna_content(qna_content);
		board.setQna_re_ref(qna_re_ref);
		board.setQna_re_lev(qna_re_lev);
		board.setQna_re_seq(qna_re_seq);
		board.setQna_writer_num(qna_writer_num);
		System.out.println(board);
		dao.boardReplySubmit(board);
		return null;
	}

	@Override
	public BoardVO getBoardVOByUpdate(HttpServletRequest request) {
		int qna_num 
		= Integer.parseInt(request.getParameter("qna_num"));
		return dao.getBoardVO(qna_num);
	}

	@Override
	public void boardUpdate(HttpServletRequest request, HttpServletResponse response) {
		// 게시글 수정 요청 Service
		int qna_num 
		= Integer.parseInt(request.getParameter("qna_num"));
		String qna_name = request.getParameter("qna_name");
		String qna_title = request.getParameter("qna_title");
		String qna_content = request.getParameter("qna_content");
		int qna_writer_num  
		= Integer.parseInt(request.getParameter("qna_writer_num"));
		
		BoardVO vo = dao.getBoardVO(qna_num);
		if(vo.getQna_writer_num() != qna_writer_num) {
			try {
				response.setContentType("text/html;charset=utf-8");
				PrintWriter out = response.getWriter();
				out.print("<script>");
				out.print("alert('잘못된 접근 입니다.');");
				out.print("history.go(-1);");
				out.print("</script>");
				return;
			} catch (IOException e) {}
		}
		
		vo.setQna_name(qna_name);
		vo.setQna_title(qna_title);
		vo.setQna_content(qna_content);

		try {
			boolean isSuccess = dao.boardUpdate(vo);
			if(!isSuccess) {
				response.sendRedirect("boardUpdateForm.bo?qna_num="+qna_num);
				return;
			}
			response.sendRedirect("boardRead.bo?qna_num="+qna_num);
		} catch (IOException e) {}
		
	}

	@Override
	public void boardDelete(HttpServletRequest request, HttpServletResponse response) {
		// 게시글 삭제요청 Service
		int qna_num 
		= Integer.parseInt(request.getParameter("qna_num"));
		MemberVO vo = (MemberVO)request.getSession().getAttribute("member");
		try {
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			if(vo == null) {
				out.print("<script>");
				out.print("alert('로그인 이후 사용가능합니다.');");
				out.print("location.href='login.mc';");
				out.print("</script>");
				return;
			}
			
			boolean isSuccess = dao.boardDelete(qna_num, vo.getNum());
			if(isSuccess) {
				response.sendRedirect("boardList.bo");
			}else {
				response.sendRedirect("boardRead.bo?qna_num="+qna_num);
			}
			
		} catch (IOException e) {}
	}

}








