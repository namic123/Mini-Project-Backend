package com.example.miniprojectbackend.mapper;

import com.example.miniprojectbackend.domain.Board;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {

    @Insert("""
    INSERT INTO board (title, content, writer)
    VALUES (#{title}, #{content}, #{writer})  /* DTO와 Mapping */    
""")
    int insert(Board board);
}
