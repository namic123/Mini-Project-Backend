package com.example.miniprojectbackend.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Board {
    private Integer id;
    private Integer commentNum;
    private String title;
    private String content;
    private String writer;
    private String nickName;
    private LocalDateTime inserted;

}
