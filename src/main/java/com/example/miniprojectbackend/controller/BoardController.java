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

    @PostMapping("add")
    public ResponseEntity add(@RequestBody Board board) {  // 요청 본문을 가져옴
        if(!service.validate(board)){   // 검증 코드
            return ResponseEntity.badRequest().build();
        }

        if (service.save(board)) { // true - 200 응답 코드
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.internalServerError().build();

    }

    @GetMapping("list")
    public List<Board> list(){
        return service.list();
    }

    @GetMapping("id/{id}")
    public Board get(@PathVariable Integer id){
        return service.get(id);
    }
    @DeleteMapping("remove/{id}")
    public ResponseEntity remove(@PathVariable Integer id){
        if(service.remove(id)){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.internalServerError().build();
        }
    }
}
