package com.example.miniprojectbackend.service;

import com.example.miniprojectbackend.domain.Like;
import com.example.miniprojectbackend.domain.Member;
import com.example.miniprojectbackend.mapper.LikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class) // checked 포함 문제 발생 시 rollback
public class LikeService {
    private final LikeMapper mapper;

    // 좋아요 클릭 로직
    public Map<String, Object> update(Like like, Member login) {
        // 좋아요를 누른 멤버의 아이디를 할당
        like.setMemberId(login.getId());
        // 처음 좋아요 누를 때 : insert
        // 다시 누르면 delete
        int count = 0;
        // 해당 멤버가 가진 좋아요를 삭제시켰을 때 0이면
        // 좋아요를 누른적이 없으므로, 좋아요를 insert
        if(mapper.delete(like) == 0){
            count = mapper.insert(like);
        }
        // 해당 게시글의 좋아요가 몇개인지 반환
        int countLike = mapper.countByBoardId(like.getBoardId());
        
        return Map.of("like", count == 1,
                "countLike", countLike);
    }

    // 게시글의 좋아요 총 개수 로직
    public Map<String, Object> get(Integer boardId, Member login) {
        int countLike = mapper.countByBoardId(boardId);
        Like like = null;
        // 로그인 상태인 경우
        if(login != null){
            // 로그인한 사용자의 좋아요 클릭 여부를 확인
           like = mapper.selectByBoardIdAndMemberId(boardId, login.getId());
        }

        return Map.of("like", like != null, "countLike", countLike);
    }
}
