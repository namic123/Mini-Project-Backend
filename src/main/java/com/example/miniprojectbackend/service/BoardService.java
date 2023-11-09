package com.example.miniprojectbackend.service;

import com.example.miniprojectbackend.domain.Board;
import com.example.miniprojectbackend.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper mapper;

    public void save(Board board) {
    mapper.insert(board);
    }
}
