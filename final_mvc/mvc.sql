
DROP TABLE mvc_member;

CREATE TABLE mvc_member(
	num INT primary key auto_increment,		--회원번호
	id VARCHAR(50) UNIQUE NOT NULL,			--아이디(email)
	pass VARCHAR(100) NOT NULL,				--비밀번호
	name VARCHAR(50) ,						--이름
	age INT(3) default 1,					--나이
	gender VARCHAR(10) ,					--성별
	joinYN char(1) default 'Y',				--탈퇴여부
	regdate TIMESTAMP default now(),		--회원가입일
	updatedate TIMESTAMP default now()		--회원정보수정일
);

INSERT INTO mvc_member(id,pass,name,gender) 
VALUES('admin','admin','MASTER','male');

SELECT * FROM mvc_member;

UPDATE mvc_member SET joinYN = 'Y';
commit;

-- 비밀번호 찾기 code 저장 테이블
DROP TABLE test_code;
CREATE TABLE test_code(
	id VARCHAR(50),
	code char(5)
);

DESC test_code;

SELECT * FROM test_code;

-- 공지형 게시판 table
DROP TABLE notice_board;
CREATE TABLE notice_board(
	notice_num int primary key auto_increment,  -- 게시물 번호
	notice_category VARCHAR(20) ,				-- 게시물 분류
	notice_author VARCHAR(50),					-- 게시글 작성자
	notice_title VARCHAR(100),					-- 게시글 제목
	notice_content LONGTEXT ,					-- 게시글 내용
	notice_date TIMESTAMP default now() 		-- 게시글 작성 시간
);

SELECT * FROM notice_board ORDER BY notice_num DESC limit 1;

DELETE FROM notice_board WHERE notice_num = 2 OR notice_num = 3;
commit;

INSERT INTO notice_board(
notice_category,
notice_author,
notice_title,
notice_content)
SELECT notice_category,
notice_author,
notice_title,
notice_content FROM notice_board; 

SELECT * FROM notice_board 
WHERE notice_title LIKE '%게시글%' 
ORDER BY notice_num DESC limit 0, 10;

-- 질문과 답변 - 답변형 게시판
DROP TABLE qna_board;
CREATE TABLE qna_board(
	qna_num INT PRIMARY KEY auto_increment,			-- 글번호
	qna_name VARCHAR(30) NOT NULL,					-- 작성자
	qna_title VARCHAR(100) NOT NULL,				-- 글제목
	qna_content LONGTEXT NOT NULL,					-- 글내용
	qna_file VARCHAR(100),							-- 저장된 파일이름
	qna_file_origin VARCHAR(100),					-- 원본파일 이름
	qna_re_ref INT NOT NULL,						-- 원본 글 번호
	qna_re_lev INT NOT NULL,						-- 답변글 view 번호
	qna_re_seq INT NOT NULL,						-- 답변글 정렬 번호
	qna_writer_num INT,								-- 글 작성자 번호
	qna_readcount INT DEFAULT 0,					-- 조회수
	qna_delete char(1) DEFAULT 'N',					-- 삭제 여부
	qna_date TIMESTAMP DEFAULT now(),				-- 작성 시간
	FOREIGN KEY(qna_writer_num) REFERENCES mvc_member(num)
);

ALTER TABLE qna_board 
ADD qna_delete char(1) DEFAULT 'N' AFTER qna_readcount;

DESC qna_board;

SELECT * FROM qna_board ORDER BY qna_num DESC limit 1;

-- 게시글에 대한 댓글을 저장할 테이블
DROP TABLE qna_comment;

CREATE TABLE qna_comment(
	comment_num INT auto_increment,
	comment_id VARCHAR(50),
	comment_name VARCHAR(50),
	comment_content VARCHAR(300),
	comment_date TIMESTAMP DEFAULT now(),
	comment_delete char(1),
	comment_board_num INT,
	PRIMARY KEY (comment_num),
	FOREIGN KEY(comment_board_num)
	REFERENCES qna_board(qna_num)
);

SELECT * FROM qna_comment WHERE comment_board_num = 10;









