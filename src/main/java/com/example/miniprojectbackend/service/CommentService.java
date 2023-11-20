package com.example.miniprojectbackend.service;

import com.example.miniprojectbackend.domain.Comment;
import com.example.miniprojectbackend.domain.Member;
import com.example.miniprojectbackend.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class) // checked 포함 문제 발생 시 rollback
public class CommentService {
    private final CommentMapper mapper;

    public boolean add(Comment comment, Member login) {
        comment.setMemberId(login.getId());
        return mapper.insert(comment) == 1;
    }

    public boolean validate(Comment comment) {
        if (comment == null) {
            return false;
        }

        if (comment.getBoardId() == null || comment.getBoardId() < 1) {
            return false;
        }

        if (comment.getComment() == null || comment.getComment().isBlank()) {
            return false;
        }

        return true;
    }

    public List<Comment> list(Integer boardId) {
        return mapper.selectByBoardId(boardId);
    }


    public boolean remove(Integer id) {
        return mapper.remove(id);
    }

    public boolean hasAccess(Integer id, Member login) {
        Comment comment = mapper.selectById(id);
        return comment.getMemberId().equals(login.getId());
    }

    public boolean update(Comment comment) {
        return mapper.update(comment) == 1;
    }


    // 수정 댓글의 유효성 검증
    public boolean updateValidate(Comment comment) {
        if (comment == null) {
            return false;
        }
        if(comment.getId() == null){
            return false;
        }
        if (comment.getComment() == null || comment.getComment().isBlank()){
            return false;
        }
        return true;
    }
}

