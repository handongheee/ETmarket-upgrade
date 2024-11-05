package kr.co.sist.etmarket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SimilarItemDto {

    private Long itemId; // 상품 id
    private String itemTitle; //상품명
    private String itemPrice; //상품가격
    private String imgUrl; //상품 메인 이미지 url
    private Long SellerId; // 판매자 id
    private String sellerName; //판매자 명
}
