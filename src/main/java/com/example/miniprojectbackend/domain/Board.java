package com.example.miniprojectbackend.domain;

import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

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

    // 날짜 형식 변환
    public String getAgo() {
        // 현재 날짜를 저장
        LocalDateTime now = LocalDateTime.now();
        // inserted가 now와 비교했을 때 1년 이상인지 확인
        // 1년 이상 지났다면 참
        if (inserted.isBefore(now.minusYears(1))) {
            // inserted 날짜와 현재 날짜 'now'사이의 시간 차이를 계산
            // 객체 사이 기간을 Period 객체로 반환
            Period between = Period.between(inserted.toLocalDate(), now.toLocalDate());
            // 마지막으로 계산된 Period객체에서 연도 차이를 추출
            // between.get(ChronoUnit.YEARS)는 계산된 기간 중 연도의 수를 반환
            return between.get(ChronoUnit.YEARS) + "년 전";
        } else if (inserted.isBefore(now.minusMonths(1))) {
            Period between = Period.between(inserted.toLocalDate(), now.toLocalDate());
            return between.get(ChronoUnit.MONTHS) + "달 전";
        } else if (inserted.isBefore(now.minusDays(1))) {
            Period between = Period.between(inserted.toLocalDate(), now.toLocalDate());
            return between.get(ChronoUnit.DAYS) + "일 전";

            // inserted가 now와 비교했을 때 1시간 이상인지 확인
            // 1시간 이상 지났다면 참
        } else if (inserted.isBefore(now.minusHours(1))) {
            // inserted의 시간과 now 사이의 차이를 계산
            // Duration 객체로 반환
            Duration between = Duration.between(inserted, now);
            // inserted와 now 사이의 전체 초 수를 구한 다음 /60/60으로 나누어
            // 전체 시간수를 구함.
            return (between.getSeconds()/60/60) + "시간 전";
        } else if (inserted.isBefore(now.minusMinutes(1))){
            Duration between = Duration.between(inserted, now);
            return (between.getSeconds()/60) +"분 전";
        }else {
            Duration between = Duration.between(inserted, now);
            return between.getSeconds() +"초 전";
        }
    }
}
