package com.example.miniprojectbackend.controller;

import com.example.miniprojectbackend.domain.Board;
import com.example.miniprojectbackend.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


// 게시판 컨트롤러
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService service;

    // 게시글 저장 요청
    @PostMapping("add")
    public ResponseEntity add(@RequestBody Board board) {
        if(!service.validate(board)){  // 검증 코드
            return ResponseEntity.badRequest().build(); // 400 상태코드
        }

        if (service.save(board)) { // 저장 성공
            return ResponseEntity.ok().build(); // 200 응답 상태
        }
        return ResponseEntity.internalServerError().build(); // 500 상태코드

    }

    // 게시글 목록 요청
    @GetMapping("list")
    public List<Board> list(){
        return service.list();
    }

    // 게시글 보기 요청
    @GetMapping("id/{id}")
    public Board get(@PathVariable Integer id){
        return service.get(id);
    }

    // 게시글 삭제 요청
    @DeleteMapping("remove/{id}")
    public ResponseEntity remove(@PathVariable Integer id){
        if(service.remove(id)){
            return ResponseEntity.ok().build();     // 200 상태코드 (요청 성공)
        }else{
            return ResponseEntity.internalServerError().build();    // 500 상태코드 (서버 에러)
        }
    }

    // 게시글 수정
    @PutMapping("edit")
    public ResponseEntity edit(@RequestBody Board board){
        if(service.validate(board)) {       // null 값 검증 service 로직
            if (service.update(board)) {        // 업데이트 로직
                return ResponseEntity.ok().build();         // 200 성공
            } else {
                return ResponseEntity.internalServerError().build();    // 500 서버 에러
            }
        }else {
            return ResponseEntity.badRequest().build();     // 400 클라이언트 에러
        }
    }
}
