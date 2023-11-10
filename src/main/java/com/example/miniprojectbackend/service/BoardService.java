package com.example.miniprojectbackend.service;

import com.example.miniprojectbackend.domain.Board;
import com.example.miniprojectbackend.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper mapper;

    // 저장 로직
    public boolean save(Board board) {
        return mapper.insert(board) == 1;
    }

    // null 값 여부 검증 로직
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

        // 작성자 검증
        if (board.getWriter() == null || board.getWriter().isBlank()) {
            return false;

        }
        return true;
    }
    // 리스트 로직
    public List<Board> list() {
        return mapper.loadList();
    }

    // 글 가져오는 로직
    public Board get(Integer id) {  // view
        return mapper.selectById(id);
    }

    // 글 삭제 로직
    public boolean remove(Integer id) {
        return mapper.deleteById(id);
    }

    public boolean update(Board board) {
        return mapper.update(board);
    }
}
