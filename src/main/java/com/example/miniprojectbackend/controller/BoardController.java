package com.example.miniprojectbackend.controller;

import com.example.miniprojectbackend.domain.Board;
import com.example.miniprojectbackend.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController     // Controller + ResponseBody
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService service;

    @PostMapping("add")
    public ResponseEntity add(@RequestBody Board board) {  // 요청 본문을 가져옴
        if (service.save(board)) {
            ResponseEntity.ok().build();
        }
        return ResponseEntity.internalServerError().build();

    }
}
