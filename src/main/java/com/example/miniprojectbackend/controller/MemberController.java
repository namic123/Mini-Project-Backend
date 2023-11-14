package com.example.miniprojectbackend.controller;

import com.example.miniprojectbackend.domain.Member;
import com.example.miniprojectbackend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Member> view(String id){
        Member member = service.getMember(id);
        return ResponseEntity.ok(member);
    }


    // 회원 탈퇴
    @DeleteMapping
    public ResponseEntity delete(String id){
        // TODO: 로그인 여부 -> 안한경우 : 401
        // TODO: 본인 정보 여부 -> 아닌경우: 403

        /* 성공 값 */
        if(service.deleteMember(id)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.internalServerError().build();
    }

    // 회원 수정
    @PutMapping("edit")
    public ResponseEntity edit(@RequestBody Member member){
        // TODO: 로그인 여부 및 본인 정보 여부 확인
        if(service.update(member)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody Member member){
        System.out.println("MemberController.login");
        if(service.login(member)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
