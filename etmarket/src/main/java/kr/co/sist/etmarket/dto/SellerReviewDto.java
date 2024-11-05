package kr.co.sist.etmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class SellerReviewDto {

    private Long reviewerId; // 리뷰어 id
    private String reviewerName; // 리뷰어 닉네임
    private String reviewerImgUrl; // 리뷰어 프사 이미지
    private Double reviewScore; // 리뷰 점수(소수)
    private Long reviewItemId; // 리뷰 상품 id
    private String reviewItemName; // 리뷰 상품 제목
    private String reviewComment; // 리뷰 내용
    private String reviewDate; // 리뷰 날짜(2일전, 1시간전 등)

}
