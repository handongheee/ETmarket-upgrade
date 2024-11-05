package kr.co.sist.etmarket.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class SellerDetailDto {

    private Long sellerId; // 회원 id
    private String sellerName; // 회원 닉네임
    private String sellerImgUrl; // 회원 프로필 이미지url
    private int totalItemCount; // 전체 상품 개수
    private int transactionCount; // 거래 횟수
    private int sellCount; // 판매 횟수
    private int reviewCount; // 리뷰 개수
    private double avgReviewScore; // 평균 리뷰 점수
    private int reviewScorePercentage; // 평균 리뷰점수 퍼센트
    private String salesStartDate; // 판매 시작일
    private String introDescription; // 소개글
    private List<SellerItemDto> sellerItemDtoList = new ArrayList<>();
    private List<SellerReviewDto> reviewDtoList = new ArrayList<>();

}
