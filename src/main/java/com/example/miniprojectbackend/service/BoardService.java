package com.example.miniprojectbackend.service;

import com.example.miniprojectbackend.domain.Board;
import com.example.miniprojectbackend.domain.Member;
import com.example.miniprojectbackend.mapper.BoardMapper;
import com.example.miniprojectbackend.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper mapper;
    private final MemberService memberService;

    // 게시글 등록 로직
    public boolean save(Board board, Member login) {
        // 글 작성 시 작성자를 ID로 서정
        board.setWriter(login.getId());
        return mapper.insert(board) == 1;
    }

    // 게시글 null 값 여부 검증 로직
    public boolean validate(Board board) {
        if (board == null) {
            return false;
        }
        // 본문 검증
        if (board.getContent() == null || board.getContent().isBlank()) {
            return false;
        }

        // 제목 검증
        if (board.getTitle() == null || board.getTitle().isBlank()) {
            return false;

        }

        return true;
    }
    // 게시글 목록 로직
    public List<Board> list() {
        return mapper.loadList();
    }

    // 게시글 보기 로직
    public Board get(Integer id) {  // view
        return mapper.selectById(id);
    }

    // 게시글 삭제 로직
    public boolean remove(Integer id) {
        return mapper.deleteById(id);
    }

    // 게시글 수정 로직
    public boolean update(Board board) {
        return mapper.update(board);
    }

    // 접근 권한 가능 여부
    public boolean hasAccess(Integer id, Member login) {
        if (login == null) {
            return false;
        }

        if (login.isAdmin()) {
            return true;
        }
        if( memberService.isAdmin(login)){
            return true;
        }
        // 해당 글의 작성자 id를 가져옴
        Board board = mapper.selectById(id);
        // 작성자와 로그인 아이디가 동일한지 검증
        return board.getWriter().equals(login.getId());
    }

}
