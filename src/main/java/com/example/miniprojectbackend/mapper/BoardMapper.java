package com.example.miniprojectbackend.mapper;

import com.example.miniprojectbackend.domain.Board;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}
