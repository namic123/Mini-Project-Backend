package com.example.miniprojectbackend.mapper;

import com.example.miniprojectbackend.domain.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {


    @Insert("""
        INSERT INTO comment (boardId, comment, memberId) 
        VALUES (#{boardId}, #{comment}, #{memberId})
        """)
    int insert(Comment comment);

    @Select("""
        SELECT * FROM comment
        WHERE boardId = #{boardId}
        ORDER BY inserted DESC 
            """)
    List<Comment> selectByBoardId(Integer boardId);

    @Delete("""
            DELETE FROM comment WHERE id = #{id}
            """)

    boolean remove(Integer id);

    @Select("""
            SELECT * FROM comment
            WHERE id= #{id}
            """)
    Comment selectById(Integer id);

    @Update("""
            UPDATE comment
            SET comment = #{comment}
            WHERE id = #{id}
            """)
    int update(Comment comment);
}
