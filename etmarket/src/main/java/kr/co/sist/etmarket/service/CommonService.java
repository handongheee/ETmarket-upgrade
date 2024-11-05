package kr.co.sist.etmarket.service;


import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommonService {

    public String convertTime(Timestamp timestamp) {

        LocalDateTime inputTime = timestamp.toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();

        return calculateTimeAgo(inputTime, now);
    }

    private String calculateTimeAgo(LocalDateTime resistTime, LocalDateTime now) {
        Duration duration = Duration.between(resistTime, now);

        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long weeks = days / 7;
        long months = days / 30;
        long years = days / 365;

        if (seconds < 60) {
            return seconds + "초 전";
        } else if (minutes < 60) {
            return minutes + "분 전";
        } else if (hours < 24) {
            return hours + "시간 전";
        } else if (days < 7) {
            return days + "일 전";
        } else if (weeks < 4) {
            return weeks + "주 전";
        } else if (months < 12) {
            return months + "달 전";
        } else {
            return years + "년 전";
        }
    }

    public String trimAddress(String inputAddress) {
        if (inputAddress == null || inputAddress.isEmpty()) {
            return inputAddress;
        }

        int index = inputAddress.indexOf('(');
        if (index != -1) {
            inputAddress = inputAddress.substring(0, index);
        }

        return inputAddress.trim();
    }


    public int calPercentScore(Double avgScore) {

        double percentageScore = (avgScore / 5) * 100;

        return (int) percentageScore;
    }
}
