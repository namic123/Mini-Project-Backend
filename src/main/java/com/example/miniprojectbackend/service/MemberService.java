package com.example.miniprojectbackend.service;

import com.example.miniprojectbackend.domain.Member;
import com.example.miniprojectbackend.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper mapper;
    public void add(Member member){
        mapper.insert(member);
    }

    public String getId(String id){
        return mapper.selectId(id);
    }
}
