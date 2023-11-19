package com.example.miniprojectbackend.controller;

import com.example.miniprojectbackend.domain.Board;
import com.example.miniprojectbackend.domain.Member;
import com.example.miniprojectbackend.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// 게시판 컨트롤러
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService service;

    // 게시글 저장 요청
    @PostMapping("add")
    public ResponseEntity add(@RequestBody Board board,
                              // HTTP 세션에서 login 속성에 저장된 Member 객체를 가져온다.
                              // 그 세션에서 추출한 LOGIN 속성의 값이 login 매개변수에 주입
                              // 매개변수 login을 통해 권한 여부를 결정
                              @SessionAttribute(value = "login", required = false) Member login) {
        // 비로그인 상태인 경우
        if(login == null){
            // 권한 없음 상태코드 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if(!service.validate(board)){  // 검증 코드
            return ResponseEntity.badRequest().build(); // 400 상태코드
        }

        if (service.save(board, login)) { // 저장 성공
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
    public ResponseEntity remove(@PathVariable Integer id,
                                 @SessionAttribute(value = "login",required = false) Member login){
        // 비 로그인 상태일 경우
        if(login == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // 접근 권한이 없는 경우
        if (!service.hasAccess(id, login)) {
            // 403 응답
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(service.remove(id)){
            return ResponseEntity.ok().build();     // 200 상태코드 (요청 성공)
        }else{
            return ResponseEntity.internalServerError().build();    // 500 상태코드 (서버 에러)
        }
    }

    // 게시글 수정
    @PutMapping("edit")
    public ResponseEntity edit(@RequestBody Board board,
                               @SessionAttribute(value = "login",required = false) Member login){

        if (login == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //401
        }
        // 권한이 없는 경우
        if(!service.hasAccess(board.getId(),login)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
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
