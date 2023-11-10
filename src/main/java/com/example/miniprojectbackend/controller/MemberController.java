package com.example.miniprojectbackend.controller;

import com.example.miniprojectbackend.domain.Member;
import com.example.miniprojectbackend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService service;
    @PostMapping("signup")
    public void signup(@RequestBody Member member){
        service.add(member);
    }
    @GetMapping(value = "check", params = "id")
    public ResponseEntity checkDuplicates(String id){
        if(service.getId(id) == null){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok().build();
        }
    }
}
