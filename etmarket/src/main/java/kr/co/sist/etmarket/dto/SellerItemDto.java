package kr.co.sist.etmarket.dto;


import kr.co.sist.etmarket.etenum.DealStatus;
import kr.co.sist.etmarket.etenum.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
public class SellerItemDto {
    private Long itemId;
    private String itemMainImg;
    private String itemTitle; //상품명
    private String itemPrice;
    private String itemAddress; // 직거래 지역
    private String itemUpdateDate; // 수정날짜
    private DealStatus dealStatus; // 거래 상태
    private DeliveryStatus deliveryStatus; // 배송비 포함여부(포함, 비포함)
}
