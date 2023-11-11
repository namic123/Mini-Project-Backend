package com.example.miniprojectbackend.service;

import com.example.miniprojectbackend.domain.Member;
import com.example.miniprojectbackend.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper mapper;

    // 회원 등록 로직
    // 성공 여부를 반환
    public boolean add(Member member) {
        return mapper.insert(member) == 1;

    }

    // ID 중복 체크 로직
    public String getId(String id) {
        return mapper.selectId(id);
    }

    // Email 중복 체크 로직
    public String getEmail(String email) {
        return mapper.selectEmail(email);
    }

    // 회원 등록 요청 시 null 값 검증 로직
    public boolean validate(Member member) {
        if (member == null) {
            return false;
        }
        if (member.getPassword().isBlank()) {
            return false;
        }
        if (member.getEmail().isBlank()) {
            return false;
        }
        if (member.getId().isBlank()) {
            return false;
        }
        return true;
    }

    // 회원 목록 로직
    public List<Member> list() {
        return mapper.selectAll();
    }
}
