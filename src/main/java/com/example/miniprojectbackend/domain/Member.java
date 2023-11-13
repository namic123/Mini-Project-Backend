package com.example.miniprojectbackend.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Member {
private String id;
private String nickName;
private String password;
private String email;
private LocalDateTime inserted;
}
