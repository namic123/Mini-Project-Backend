package com.example.miniprojectbackend.mapper;

import com.example.miniprojectbackend.domain.Board;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.Map;

@Mapper
public interface BoardMapper {

    @Insert("""
    INSERT INTO board (title, content, writer)
    VALUES (#{title}, #{content}, #{writer})  /* DTO와 Mapping */    
""")
    int insert(Board board);

    @Select("""
    SELECT id, title, writer, inserted FROM board
    ORDER BY id DESC ;
""")
    List<Board> loadList();

    @Select("""
    SELECT id, title, content, writer, inserted
    FROM board
    WHERE id = #{id}
""")
    Board selectById(Integer id);
@Delete("""
    DELETE FROM board WHERE id = #{id}
""")
    boolean deleteById(Integer id);

@Update("""
        UPDATE board 
        SET title =#{title}, 
        content = #{content},
        writer = #{writer}
        WHERE id = #{id}
        """)
    boolean update(Board board);
}
