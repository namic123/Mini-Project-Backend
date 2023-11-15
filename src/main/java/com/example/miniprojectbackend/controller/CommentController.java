package com.example.miniprojectbackend.controller;

import com.example.miniprojectbackend.domain.Comment;
import com.example.miniprojectbackend.domain.Member;
import com.example.miniprojectbackend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService service;

    @PostMapping("add")
    public ResponseEntity add(@RequestBody Comment comment,
                              @SessionAttribute(value = "login", required = false) Member login) {

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (service.validate(comment)) {
            if (service.add(comment, login)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }

        } else {
            return ResponseEntity.badRequest().build();
        }

    }
}
// 댓글 컨트롤러
