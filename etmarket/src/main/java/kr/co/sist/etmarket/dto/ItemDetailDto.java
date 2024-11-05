package kr.co.sist.etmarket.dto;

import kr.co.sist.etmarket.etenum.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;



@Setter @Getter
public class ItemDetailDto {

    private Long itemId; //상품번호
    private Long sellerId; // 판매자 id
    private CategoryName categoryName; //카테고리
    private String itemTitle; //상품명
    private String itemContent; //상품 상세설명
    private String itemPrice; // 상품 가격(쉼표 포함 스트링)
    private int wishCount; // 찜 개수
    private int viewCount; // 조회수
    private ItemStatus itemStatus; // 상품상태(새상품, 사용감없음, 사용감적음, 사용감많음,고장파손상품)
    private DealStatus dealStatus; // 거래상태(예약중, 판매중, 거래완료)
    private ItemHidden itemHidden; // 상품 숨김여부(숨김, 보임)
    private DealHow dealHow; // 직거래 가능여부(가능, 불가능)
    private String itemAddress; // 직거래 지역
    private DeliveryStatus deliveryStatus; // 배송비 포함여부(포함, 비포함)
    private int itemDeliveryPrice; // 배송비
    private Timestamp itemResistDate; // 등록날짜
    private Timestamp itemUpdateDate; // 수정날짜
    private List<DetailTagDto> detailTagDtoList = new ArrayList<>(); // 상품태그 리스트
    private List<ItemDetailImgDto> itemImgDtoList = new ArrayList<>(); //상품 이미지 리스트


}

