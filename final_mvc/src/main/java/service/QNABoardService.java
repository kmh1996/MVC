package service;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vo.BoardVO;

public interface QNABoardService {

	// 페이징 처리된 전체 게시물 목록 정보
	public ArrayList<BoardVO> getBoardList(HttpServletRequest request);
	
	// 게시물 등록 요청 - 원본글
	public void boardWrite(HttpServletRequest request);
	
	// 게시물 등록 요청 - 원본글, 파일 업로드
	public void boardWriteFile(HttpServletRequest request);
	
	// 게시물 상세보기 페이지 요청
	public BoardVO getBoardVO(HttpServletRequest request);
	
	// 게시물 조회수 증가
	public void updateReadCount(HttpServletRequest request);
	
	// 파일 다운로드 요청 처리
	public void fileDown(HttpServletRequest request,
					     HttpServletResponse response);
	
	// 답변글 작성 화면 요청
	public BoardVO boardReply(HttpServletRequest request);
	
	// 답변글 작성 요청 처리
	public BoardVO boardReplySubmit(HttpServletRequest request);
	
	// 수정 페이지 요청 시 수정할 게시물 정보 전달
	public BoardVO getBoardVOByUpdate(HttpServletRequest request); 
	
	// 게시글 수정 요청
	public void boardUpdate(HttpServletRequest request, 
							HttpServletResponse response);
	
	// 게시글 삭제 요청
	public void boardDelete(HttpServletRequest request, 
							HttpServletResponse response);
}
















