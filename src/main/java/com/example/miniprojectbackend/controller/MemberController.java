package com.example.miniprojectbackend.controller;

import com.example.miniprojectbackend.domain.Member;
import com.example.miniprojectbackend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService service;
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
    @GetMapping(value = "check", params = "id")
    public ResponseEntity checkDuplicates(String id){
        if(service.getId(id) == null){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok().build();
        }
    }
    @GetMapping(value = "check", params = "email")
    public ResponseEntity checkEmail(String email){
        if(service.getEmail(email) == null){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok().build();
        }
    }
    @GetMapping("list")
    public List<Member> list(){
        return service.list();
    }


}
