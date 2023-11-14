package com.example.miniprojectbackend.controller;

import com.example.miniprojectbackend.domain.Member;
import com.example.miniprojectbackend.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

// 회원 컨트롤러
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService service;

    // 회원 가입
    @PostMapping("signup")
    public ResponseEntity signup(@RequestBody Member member){
        if(service.validate(member)){
            if(service.add(member)){
                return ResponseEntity.ok().build();
            }else {
                return ResponseEntity.internalServerError().build();
            }
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    // ID 중복 체크
    @GetMapping(value = "check", params = "id")
    public ResponseEntity checkDuplicates(String id){
        if(service.getId(id) == null){  // 존재하지 않는 ID인 경우, 404
            return ResponseEntity.notFound().build();
        }else{      // 존재하는 경우 200 ok
            return ResponseEntity.ok().build();
        }
    }

    // email 중복 체크
    @GetMapping(value = "check", params = "email")
    public ResponseEntity checkEmail(String email){
        if(service.getEmail(email) == null){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping(value = "check", params = "nickName")
    public ResponseEntity checkNickName(String nickName){
        if(service.getNickName(nickName) == null){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok().build();
        }
    }

    // 회원 목록
    @GetMapping("list")
    public List<Member> list(){
        return service.list();
    }

    // 회원 보기
    @GetMapping
    public ResponseEntity<Member> view(String id,
                                       @SessionAttribute(value = "login", required = false) Member login){
        // 비로그인 상태
        if(login==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 접근 권한 가능 여부
        if(!service.hasAccess(id, login)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Member member = service.getMember(id);
        return ResponseEntity.ok(member);
    }


    // 회원 탈퇴
    @DeleteMapping
    public ResponseEntity delete(String id,
                                 @SessionAttribute(value = "login", required = false) Member login){

        // 비로그인 상태
        if(login == null){
            // 401 에러
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 접근 권한 가능 여부
        if(!service.hasAccess(id,login)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        /* 성공 값 */
        if(service.deleteMember(id)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.internalServerError().build();
    }

    // 회원 수정
    @PutMapping("edit")
    public ResponseEntity edit(@RequestBody Member member,
                               @SessionAttribute(value = "login", required = false) Member login){
        // 비로그인 상태
        if(login == null){
            // 401 에러
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 접근 권한 가능 여부
        if(!service.hasAccess(member.getId(),login)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if(service.update(member)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.internalServerError().build();
        }
    }

    // 로그인
    // WebRequest는 HTTP 요청에 대한 정보를 담고 있음.
    @PostMapping("login")
    public ResponseEntity login(@RequestBody Member member, WebRequest request){
        // 로그인 성공 여부 로직
        if(service.login(member, request)){
            // 성공 시 200
            return ResponseEntity.ok().build();
        }else {
            // 실패 시 401(Unauthorized)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("logout")
    public void logout(HttpSession session){
        // 세션이 비어있지 않은 경우
        if(session != null){
            // HTTP 세션 무효화, 세션에 저장된 모든 데이터 제거
            session.invalidate();
        }
    }

}
