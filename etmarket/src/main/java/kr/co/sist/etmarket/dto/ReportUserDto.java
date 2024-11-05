package kr.co.sist.etmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class ReportUserDto {
    // 사용자 신고의 ID
    private Long reportUserId;

    // 신고를 한 사용자의 ID
    private Long reporterId;

    // 신고를 한 사용자의 이름
    private String reporterName;

    // 신고된 사용자의 ID
    private Long reportedId;

    // 신고된 사용자의 이름
    private String reportedName;

    // 신고 내용
    private String content;

    // 신고 생성일
    private Timestamp createDate;
}