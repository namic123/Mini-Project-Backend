package com.example.miniprojectbackend.controller;

import com.example.miniprojectbackend.domain.Board;
import com.example.miniprojectbackend.domain.Member;
import com.example.miniprojectbackend.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ResponseEntity add(Board board, // 인코딩 타입이 multipart-formdata인 경우 request body로 받을 수 없다.
                              @RequestParam(value = "uploadFiles[]", required = false) MultipartFile[] files,
                              // HTTP 세션에서 login 속성에 저장된 Member 객체를 가져온다.
                              // 그 세션에서 추출한 LOGIN 속성의 값이 login 매개변수에 주입
                              // 매개변수 login을 통해 권한 여부를 결정
                              @SessionAttribute(value = "login", required = false) Member login) throws IOException {

        // 비로그인 상태인 경우
        if(login == null){
            // 권한 없음 상태코드 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if(!service.validate(board)){  // 검증 코드
            return ResponseEntity.badRequest().build(); // 400 상태코드
        }

        if (service.save(board, files, login)) { // 저장 성공
            return ResponseEntity.ok().build(); // 200 응답 상태
        }
        return ResponseEntity.internalServerError().build(); // 500 상태코드

    }

    // 게시글 목록 요청
    @GetMapping("list")
    // 게시글 리스트 반환
    // 페이지 정보(페이지 그룹, 현재 페이지 정보)를 반환
    public Map<String, Object> list(@RequestParam(value = "pg", defaultValue = "1") Integer page,
                                    @RequestParam(value = "k", defaultValue = "") String keyword){
        return service.list(page, keyword);
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
        if(service.remove(id)){
            return ResponseEntity.ok().build();     // 200 상태코드 (요청 성공)
        }else{
            return ResponseEntity.internalServerError().build();    // 500 상태코드 (서버 에러)
        }
    }

    @DeleteMapping("deleteFile/{id}")
    public ResponseEntity deleteFile(@PathVariable Integer id,
                                     @SessionAttribute(value = "login", required = false) Member login){

        System.out.println("BoardController.deleteFile");
        // 비 로그인 상태일 경우
        if(login == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // 접근 권한이 없는 경우
        if (!service.hasAccess(id, login)) {
            // 403 응답
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(service.deleteFile(id)){
            return ResponseEntity.ok().build();     // 200 상태코드 (요청 성공)
        }else{
            return ResponseEntity.internalServerError().build();    // 500 상태코드 (서버 에러)
        }
    }
    // 게시글 수정
    @PutMapping("edit")
    public ResponseEntity edit(Board board, @RequestParam(value = "uploadFiles[]", required = false) MultipartFile[] files,
                               @SessionAttribute(value = "login",required = false) Member login) throws IOException {

        if (login == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //401
        }
        // 권한이 없는 경우
        if(!service.hasAccess(board.getId(),login)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(service.validate(board)) {       // null 값 검증 service 로직
            if (service.update(board, files)) {        // 업데이트 로직
                return ResponseEntity.ok().build();         // 200 성공
            } else {
                return ResponseEntity.internalServerError().build();    // 500 서버 에러
            }
        }else {
            return ResponseEntity.badRequest().build();     // 400 클라이언트 에러
        }
    }
}
