package kr.co.sist.etmarket.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class SimpleSellerDto {

    private Long sellerId;
    private String sellerName;
    private String sellerImgUrl;
    private int sellItemCount;
    private int transactionCount;
    private int reviewCount;
    private List<SimpleItemDto> simpleItemDtoList = new ArrayList<>();
    private List<ReviewDto> revieweDtoList = new ArrayList<>();

}
