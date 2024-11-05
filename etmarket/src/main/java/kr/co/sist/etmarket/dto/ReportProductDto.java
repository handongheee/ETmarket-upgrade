package kr.co.sist.etmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class ReportProductDto {
    // 신고된 상품의 ID
    private Long reportProductId;

    // 신고를 한 사용자의 ID
    private Long userId;

    // 신고를 한 사용자의 이름
    private String userName;

    // 신고된 상품의 ID
    private Long itemId;

    // 신고된 상품의 이름
    private String itemName;

    // 신고 제목
    private String title;

    // 신고 내용
    private String content;

    // 신고 생성일
    private Timestamp createDate;
}