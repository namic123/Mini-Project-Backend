package com.example.miniprojectbackend.domain;

import com.example.miniprojectbackend.util.AppUtil;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Data
public class Board {
    private Integer id;
    private Integer commentNum;
    private String title;
    private String content;
    private String writer;
    private String nickName;
    private LocalDateTime inserted;
    private Integer countLike;
    private List<String> fileNames;
    public String getAgo(){
        return AppUtil.getAgo(inserted, LocalDateTime.now());
    }
}
