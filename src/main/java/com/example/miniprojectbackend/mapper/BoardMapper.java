package com.example.miniprojectbackend.mapper;

import com.example.miniprojectbackend.domain.Board;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.Map;

@Mapper
public interface BoardMapper {

    // 게시글 등록 쿼리
    @Insert("""
                INSERT INTO board (title, content, writer)
                VALUES (#{title}, #{content}, #{writer})  /* DTO와 Mapping */    
            """)
    int insert(Board board);

    // 게시글 목록 쿼리
    // 게시글 목록에 nickname, 댓글, 좋아요 수를 보여주기 위한 조인

    @Select("""
                SELECT b.id, 
                b.title, 
                b.writer,
                m.nickName, 
                b.inserted,
                COUNT(DISTINCT c.id) commentNum,
                COUNT(DISTINCT l.id) countLike
                FROM board b
                JOIN member m ON b.writer = m.id
                LEFT JOIN comment c on b.id =c.boardId
                LEFT JOIN boardlike l on b.id = l.boardId
                GROUP BY b.id
                ORDER BY b.id DESC ;
                /* 페이징 처리*/
                LIMIT #{from}, 10;
            """)
    List<Board> loadList(Integer from);

    // 게시글 보기 쿼리
    // 게시글에 nickname을 보여주기 위한 조인
    @Select("""
            SELECT b.id,
                   b.title, 
                   b.content, 
                   b.writer, 
                   m.nickName,
                   b.inserted
                FROM board b JOIN member m ON b.writer = m.id
                WHERE b.id = #{id}
                """)
    Board selectById(Integer id);

    // 게시글 삭제 쿼리
    @Delete("""
            
                DELETE FROM board WHERE id = #{id}          
            """)
    boolean deleteById(Integer id);

    // 게시글 수정 쿼리
    @Update("""
            UPDATE board 
            SET title =#{title}, 
            content = #{content},
            writer = #{writer}
            WHERE id = #{id}
            """)
    boolean update(Board board);

    // 회원 탈퇴를 위한 게시글 삭제 쿼리
    @Delete("""
    DELETE FROM board WHERE writer = #{writer}
""")
    int deleteByWriter(String writer);
}
