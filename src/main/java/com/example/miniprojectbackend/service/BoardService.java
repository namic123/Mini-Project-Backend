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

    public boolean save(Board board) {
        return mapper.insert(board) == 1;
    }

    // 게시물이 비었는지 검증하는 서비스
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

    public List<Board> list() {
        return mapper.loadList();
    }

    public Board get(Integer id) {  // view
        return mapper.selectById(id);
    }

    public boolean remove(Integer id) {
        return mapper.deleteById(id);
    }
}
