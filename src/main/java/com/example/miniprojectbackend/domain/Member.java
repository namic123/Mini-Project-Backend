package com.example.miniprojectbackend.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Member {
private String id;
private String nickName;
private String password;
private String email;
private LocalDateTime inserted;
private List<Auth> auth;
}
