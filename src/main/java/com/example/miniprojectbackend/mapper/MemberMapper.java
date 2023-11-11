package com.example.miniprojectbackend.mapper;

import com.example.miniprojectbackend.domain.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MemberMapper {

        // 회원 등록 쿼리
        @Insert("""
    INSERT INTO member (id,password,email)VALUES (#{id},#{password},#{email})
""")
        int insert(Member member);

        // ID 중복 체크 쿼리
        @Select("""
        SELECT id FROM member
        WHERE id = #{id}
""")
        String selectId(String id);

        // Email 중복 체크 쿼리
        @Select("""
        SELECT email FROM member
        WHERE email = #{email}
""")
        String selectEmail(String email);

        // 전체 회원 목록 쿼리
        @Select("""
                SELECT id, password, email, inserted FROM member ORDER BY inserted DESC;
                """)
        List<Member> selectAll();
}
