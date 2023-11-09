package com.example.miniprojectbackend.controller;

import com.example.miniprojectbackend.domain.Board;
import com.example.miniprojectbackend.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController     // Controller + ResponseBody
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService service;

    @PostMapping("add") // 글 저장
    public ResponseEntity add(@RequestBody Board board) {
        if(!service.validate(board)){   // 검증 코드
            return ResponseEntity.badRequest().build(); // 400 상태코드
        }

        if (service.save(board)) { // true - 200 응답 코드
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.internalServerError().build(); // 500 상태코드

    }

    @GetMapping("list")     // 리스트
    public List<Board> list(){
        return service.list();
    }

    @GetMapping("id/{id}")  // 작성 글 view
    public Board get(@PathVariable Integer id){
        return service.get(id);
    }
    @DeleteMapping("remove/{id}")   // 글 삭제
    public ResponseEntity remove(@PathVariable Integer id){
        if(service.remove(id)){
            return ResponseEntity.ok().build();     // 200 상태코드
        }else{
            return ResponseEntity.internalServerError().build();    // 500 상태코드
        }
    }
}
