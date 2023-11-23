package com.example.miniprojectbackend.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

// 유틸리티 패키지는
// 프로젝트의 다른 부분에서 공통적으로 사용될 수 있는 도우미 클래스나 메서드를 포함
public class AppUtil {

    // 날짜 형식 변환 유틸리티
    public static String getAgo(LocalDateTime a) {
        LocalDateTime b = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        // inserted가 now와 비교했을 때 1년 이상인지 확인
        // 1년 이상 지났다면 참
        if (a.isBefore(b.minusYears(1))) {
            // inserted 날짜와 현재 날짜 'now'사이의 시간 차이를 계산
            // 객체 사이 기간을 Period 객체로 반환
            Period between = Period.between(a.toLocalDate(), b.toLocalDate());
            // 마지막으로 계산된 Period객체에서 연도 차이를 추출
            // between.get(ChronoUnit.YEARS)는 계산된 기간 중 연도의 수를 반환
            return between.get(ChronoUnit.YEARS) + "년 전";
        } else if (a.isBefore(b.minusMonths(1))) {
            Period between = Period.between(a.toLocalDate(), b.toLocalDate());
            return between.get(ChronoUnit.MONTHS) + "달 전";
        } else if (a.isBefore(b.minusDays(1))) {
            Period between = Period.between(a.toLocalDate(), b.toLocalDate());
            return between.get(ChronoUnit.DAYS) + "일 전";

            // inserted가 now와 비교했을 때 1시간 이상인지 확인
            // 1시간 이상 지났다면 참
        } else if (a.isBefore(b.minusHours(1))) {
            // inserted의 시간과 now 사이의 차이를 계산
            // Duration 객체로 반환
            Duration between = Duration.between(a, b);
            // inserted와 now 사이의 전체 초 수를 구한 다음 /60/60으로 나누어
            // 전체 시간수를 구함.
            return (between.getSeconds()/60/60) + "시간 전";
        } else if (a.isBefore(b.minusMinutes(1))){
            Duration between = Duration.between(a, b);
            return (between.getSeconds()/60) +"분 전";
        }else {
            Duration between = Duration.between(a, b);
            return between.getSeconds() +"초 전";
        }
    }
}
