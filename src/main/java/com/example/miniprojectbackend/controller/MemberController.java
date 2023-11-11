package com.example.miniprojectbackend.controller;

import com.example.miniprojectbackend.domain.Member;
import com.example.miniprojectbackend.service.MemberService;
import lombok.RequiredArgsConstructor;
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

    // 회원 목록
    @GetMapping("list")
    public List<Member> list(){
        return service.list();
    }


}
