package com.example.miniprojectbackend.domain;

import com.example.miniprojectbackend.util.AppUtil;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Integer id; // PK
    private Integer boardId;
    private String memberId;
    private String comment;
    private LocalDateTime inserted;
    private String nickName;

    public String getAgo(){
        return AppUtil.getAgo(inserted);
    }
}
