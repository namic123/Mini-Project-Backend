package com.example.miniprojectbackend.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Integer id; // PK
    private Integer boardId;
    private String memberId;
    private String comment;
    private LocalDateTime inserted;

}
