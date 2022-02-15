package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import util.Criteria;
import util.DBCPUtil;
import vo.BoardVO;

public class QNABoardDAOImpl implements QNABoardDAO {
	
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;

	@Override
	public int getTotalCount() {
		conn = DBCPUtil.getConnection();
		String sql = "SELECT count(*) FROM qna_board";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBCPUtil.close(rs,pstmt,conn);
		}
		return 0;
	}

	@Override
	public ArrayList<BoardVO> getBoardList(Criteria cri) {
		ArrayList<BoardVO> list = new ArrayList<>();
		
		conn = DBCPUtil.getConnection();
		String sql = "SELECT * FROM qna_board "
					+" ORDER BY qna_re_ref DESC , qna_re_seq ASC limit ?, ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cri.getStartRow());
			pstmt.setInt(2, cri.getPerPageNum());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(getBoardVO(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBCPUtil.close(rs,pstmt,conn);
		}
		return list;
	}
	
	// ResultSet 객체에서 qna_board 테이블의 행 정보를 BoardVO type으로 반환
	BoardVO getBoardVO(ResultSet rs) throws SQLException{
		BoardVO vo = new BoardVO();
		vo.setQna_num(rs.getInt("qna_num"));
		vo.setQna_name(rs.getString("qna_name"));
		vo.setQna_title(rs.getString("qna_title"));
		vo.setQna_content(rs.getString("qna_content"));
		vo.setQna_file(rs.getString("qna_file"));
		vo.setQna_file_origin(rs.getString("qna_file_origin"));
		vo.setQna_re_ref(rs.getInt("qna_re_ref"));
		vo.setQna_re_lev(rs.getInt("qna_re_lev"));
		vo.setQna_re_seq(rs.getInt("qna_re_seq"));
		vo.setQna_writer_num(rs.getInt("qna_writer_num"));
		// 게시글 삭제 여부 확인 추가
		vo.setQna_delete(rs.getString("qna_delete"));
		vo.setQna_readcount(rs.getInt("qna_readcount"));
		vo.setQna_date(rs.getTimestamp("qna_date"));
		return vo;
	}

	@Override
	public void boardWrite(BoardVO vo) {
		conn = DBCPUtil.getConnection();
		
		try {
			String sql ="INSERT INTO qna_board "
					+ " VALUES(null,?,?,?,?,?,0,0,0,?,0,'N',now())";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getQna_name());
			pstmt.setString(2, vo.getQna_title());
			pstmt.setString(3, vo.getQna_content());
			pstmt.setString(4, vo.getQna_file());
			pstmt.setString(5, vo.getQna_file_origin());
			pstmt.setInt(6, vo.getQna_writer_num());
			
			pstmt.executeUpdate();
			
			sql = "SELECT LAST_INSERT_ID()";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int qna_num = 0;
			if(rs.next()) {
				qna_num = rs.getInt(1);
			}
			System.out.println("qna_num : " + qna_num);
			
			sql = "UPDATE qna_board SET qna_re_ref = ? WHERE qna_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, qna_num);
			pstmt.setInt(2, qna_num);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBCPUtil.close(rs,pstmt,conn);
		}
	}

	@Override
	public BoardVO getBoardVO(int qna_num) {
		conn = DBCPUtil.getConnection();
		String sql = "SELECT * FROM qna_board WHERE qna_num = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, qna_num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return getBoardVO(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBCPUtil.close(rs,pstmt,conn);
		}
		return null;
	}

	@Override
	public void updateReadCount(int qna_num) {
		conn = DBCPUtil.getConnection();
		String sql = "UPDATE qna_board SET "
					+" qna_readcount = qna_readcount + 1 "
					+" WHERE qna_num = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, qna_num);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBCPUtil.close(pstmt,conn);
		}
	}

	@Override
	public void boardReplySubmit(BoardVO board) {
		int ref = board.getQna_re_ref();
		int lev = board.getQna_re_lev();
		int seq = board.getQna_re_seq();
		
		String sql = "UPDATE qna_board SET qna_re_seq = qna_re_seq +1 "
					+" WHERE qna_re_ref = ? AND qna_re_seq > ?";
		conn = DBCPUtil.getConnection();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ref);
			pstmt.setInt(2, seq);
			pstmt.executeUpdate();
			
			sql = "INSERT INTO qna_board VALUES(null,?,?,?,?,?,?,?,?,?,0,'N',now())";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getQna_name());
			pstmt.setString(2, board.getQna_title());
			pstmt.setString(3, board.getQna_content());
			pstmt.setString(4, "");
			pstmt.setString(5, "");
			pstmt.setInt(6, ref);
			pstmt.setInt(7, lev+1);
			pstmt.setInt(8, seq+1);
			pstmt.setInt(9, board.getQna_writer_num());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBCPUtil.close(pstmt, conn);
		}
	}

	@Override
	public boolean boardUpdate(BoardVO vo) {
		// 게시글 수정 요청 DAO
		conn = DBCPUtil.getConnection();
		String sql = "UPDATE qna_board SET "
				   + " qna_name = ? , "
				   + " qna_title = ? , "
				   + " qna_content = ? "
				   + " WHERE qna_num = ? ";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getQna_name());
			pstmt.setString(2, vo.getQna_title());
			pstmt.setString(3, vo.getQna_content());
			pstmt.setInt(4, vo.getQna_num());
			if(pstmt.executeUpdate() > 0) {
				return true;
			};
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBCPUtil.close(pstmt,conn);
		}
		return false;
	}

	@Override
	public boolean boardDelete(int qna_num, int qna_writer_num) {
		conn = DBCPUtil.getConnection();
		String sql = "UPDATE qna_board SET "
					+" qna_delete = 'Y' "
					+" WHERE qna_num = ? "
					+" AND qna_writer_num = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, qna_num);
			pstmt.setInt(2, qna_writer_num);
			if(pstmt.executeUpdate() > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBCPUtil.close(pstmt,conn);
		}
		return false;
	}

}








