package com.example.miniprojectbackend.mapper;

import com.example.miniprojectbackend.domain.Like;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LikeMapper {
    // 좋아요 취소 로직
    @Delete("""
    DELETE FROM boardLike
    WHERE boardId = #{boardId} AND
    memberId = #{memberId}
    """)
    int delete(Like like);

    // 좋아요를 한적 없는 사용자의 아이디와 게시판 ID를 삽입
    @Insert("""
            INSERT INTO boardLike(boardId, memberId)
            VALUES (#{boardId},#{memberId})
            """)
    int insert(Like like);

    // 좋아요 갯수를 반환하는 쿼리
    @Select("""
        SELECT COUNT(id) FROM boardlike
        WHERE boardId = #{boardId}
            """)
    int countByBoardId(Integer boardId);

    // 해당 board에 대해 사용자의 좋아요 여부 확인 쿼리
    @Select("""
            SELECT * 
            FROM boardlike
            WHERE boardId = #{boardId}
                AND memberId = #{memberId}
            """)
    Like selectByBoardIdAndMemberId(Integer boardId, String memberId);
}
