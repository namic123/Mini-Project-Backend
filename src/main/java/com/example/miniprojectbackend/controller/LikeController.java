package com.example.miniprojectbackend.controller;

import com.example.miniprojectbackend.domain.Like;
import com.example.miniprojectbackend.domain.Member;
import com.example.miniprojectbackend.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
public class LikeController {
    private final LikeService service;

    // 좋아요 기능
    @PostMapping
    public ResponseEntity<Map<String, Object>> like(@RequestBody Like like,
                                                    @SessionAttribute(value = "login", required = false) Member login) {
        // 사용자 정보를 받기 위해 로그인 여부를 확인
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 응답 본문에 포함
        return ResponseEntity.ok(service.update(like, login));
    }

    // 게시글의 총 좋아요 수를 요청
    @GetMapping("board/{boardId}")
    public ResponseEntity<Map<String, Object>> get(
            @PathVariable Integer boardId,
            @SessionAttribute(value = "login", required = false) Member login) {
    return ResponseEntity.ok(service.get(boardId, login));
    }
}
