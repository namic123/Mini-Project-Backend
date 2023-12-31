package com.example.miniprojectbackend.mapper;

import com.example.miniprojectbackend.domain.Board;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BoardMapper {

    // 게시글 등록 쿼리
    @Insert("""
                INSERT INTO board (title, content, writer)
                VALUES (#{title}, #{content}, #{writer})  /* DTO와 Mapping */    
            """)
    // keyProperty = "id"는 생성된 키를 Board 객체의 id 속성에 매핑하도록 지정
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Board board);

    // 게시글 목록 쿼리
    // 게시글 목록에 nickname, 댓글, 좋아요 수를 보여주기 위한 조인

    @Select("""
                <script>
                    SELECT b.id, 
                    b.title, 
                    b.writer,
                    m.nickName, 
                    b.inserted,
                    COUNT(DISTINCT c.id) commentNum,
                    COUNT(DISTINCT l.id) countLike,
                    COUNT(DISTINCT f.id) countFile
                    FROM board b
                    JOIN member m ON b.writer = m.id
                    LEFT JOIN comment c on b.id =c.boardId
                    LEFT JOIN boardlike l on b.id = l.boardId
                    LEFT JOIN boardfile f ON b.id = f.boardId
                    WHERE 
                
                /* OR가 있으면 삭제 */
                    <trim prefixOverrides="OR">
                        <if test="category == 'all' or category == 'title'">
                            OR title LIKE #{keyword}
                        </if>
                        <if test="category == 'all' or category == 'content'">
                            OR content LIKE #{keyword} 
                        </if>
                    </trim>
                    GROUP BY b.id
                    ORDER BY b.id DESC
                    /* 페이징 처리*/
                    LIMIT #{from}, 10;
                </script>
            """)
    List<Board> loadList(Integer from, String keyword, String category);

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
            content = #{content}
            WHERE id = #{id}
            """)
    boolean update(Board board);

    // 회원 탈퇴를 위한 게시글 삭제 쿼리
    @Delete("""
    DELETE FROM board WHERE writer = #{writer}
""")
    int deleteByWriter(String writer);

    // 게시글 총 개수 로직
    @Select("""
                <script>
                SELECT COUNT(*) from board
                WHERE 
                
                /* OR가 있으면 삭제 */
                <trim prefixOverrides="OR">
                    <if test="category == 'all' or category == 'title'">
                        OR title LIKE #{keyword}
                    </if>
                    <if test="category == 'all' or category == 'content'">
                        OR content LIKE #{keyword} 
                    </if>
                </trim>
                </script>
            """)
    int countAll(String keyword, String category);
}
