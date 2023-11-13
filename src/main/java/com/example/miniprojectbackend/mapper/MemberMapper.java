package com.example.miniprojectbackend.mapper;

import com.example.miniprojectbackend.domain.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MemberMapper {

    // 회원 등록 쿼리
    @Insert("""
                INSERT INTO member (id,password,email,nickName)VALUES (#{id},#{password},#{email}, #{nickName})
            """)
    int insert(Member member);

    // ID 중복 체크 쿼리
    @Select("""
                    SELECT id FROM member
                    WHERE id = #{id}
            """)
    String selectId(String id);

    // 닉네임 중복 체크 쿼리
    @Select("""
                    SELECT nickName FROM member
                    WHERE nickName = #{nickName}
            """)
    String selectNickName(String nickName);


    // Email 중복 체크 쿼리

    @Select("""
                    SELECT email FROM member
                    WHERE email = #{email}
            """)
    String selectEmail(String email);
    // 전체 회원 목록 쿼리

    @Select("""
            SELECT * FROM member ORDER BY inserted DESC;
            """)
    List<Member> selectAll();
    // 회원 정보 보기 쿼리

    @Select("""
            SELECT id,nickName, password, email FROM member WHERE id = #{id};
            """)
    Member getMemberById(String id);
    // 회원 탈퇴 쿼리

    @Delete("""
                DELETE FROM member
                WHERE id = #{id};
            """)
    int deleteById(String id);
    // 회원 수정 쿼리
    // Dynamic SQL 쿼리문

    @Update("""
        <script>
        UPDATE member
        SET 
          <if test="password != ''">
          password = #{password},
          </if>
          email = #{email},
          nickName = #{nickName}
        WHERE id = #{id}
        </script>
        """)

    int updateMember(Member member);
}
