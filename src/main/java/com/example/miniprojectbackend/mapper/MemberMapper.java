package com.example.miniprojectbackend.mapper;

import com.example.miniprojectbackend.domain.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface MemberMapper {

        @Insert("""
    INSERT INTO member (id,password,email)VALUES (#{id},#{password},#{email})
""")
        int insert(Member member);
}
