package kr.co.sist.etmarket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.sist.etmarket.entity.*;

import kr.co.sist.etmarket.etenum.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ItemDto {
    private Long itemId;

    private Long userId;

    private String itemTitle;

    private String itemContent;

    private  String itemPriceText;

    private int itemPrice;

    private String roadAddress;

    private String detailAddress;

    private String itemAddress;

    private ItemStatus itemStatus;

    private DealStatus dealStatus;

    private DealHow dealHow;

    private DeliveryStatus deliveryStatus;

    private  String itemDeliveryPriceText;

    private int itemDeliveryPrice;

    private boolean priceStatusCheck;

    private PriceStatus priceStatus;

    private CategoryName categoryName;

    private  String itemCountText;

    private int itemCount;

    private ItemHidden itemHidden;

    private Timestamp itemResistDate;

    private Timestamp itemUpdateDate;

    private Long userSearchId;

    private List<ItemImg> itemImgs; // 변경된 부분
    private List<ItemTag> itemTags;
    private int itemChecksSize;
    private int itemLikesSize;

    public ItemDto(Integer integer, Integer integer1, long l, Timestamp timestamp, Timestamp timestamp1, long l1, String s, String s1, String s2, CategoryName categoryName, DealHow dealHow, DealStatus dealStatus, DeliveryStatus deliveryStatus, ItemHidden itemHidden, ItemStatus itemStatus, PriceStatus priceStatus, Integer integer2, long l2) {
        itemDeliveryPrice = integer;
        itemPrice = integer1;
        itemId = l;
        itemResistDate = timestamp;
        itemUpdateDate = timestamp1;
        userId = l;
        itemAddress = s;
        itemContent = s1;
        itemTitle = s2;
        this.categoryName = categoryName;
        this.dealHow = dealHow;
        this.dealStatus = dealStatus;
        this.deliveryStatus = deliveryStatus;
        this.itemHidden = itemHidden;
        this.itemStatus = itemStatus;
        this.priceStatus = priceStatus;
        itemCount = integer2;
        userSearchId = l;

    }

    public ItemDto(int itemPrice, long itemId, Timestamp updateDate, long userId, String itemAddress, String itemTitle, DealStatus dealStatus, DeliveryStatus deliveryStatus, ItemHidden itemHidden) {
        this.itemPrice = itemPrice;
        this.itemId = itemId;
        this.itemUpdateDate = updateDate;
        this.userId = userId;
        this.itemAddress = itemAddress;
        this.itemTitle = itemTitle;
        this.dealStatus = dealStatus;
        this.deliveryStatus = deliveryStatus;
        this.itemHidden = itemHidden;
    }

    public ItemDto(Item item) {
        this.itemPrice = item.getItemPrice();
        this.itemId = item.getItemId();
        this.itemUpdateDate = item.getItemUpdateDate();
        this.userId = item.getUser().getUserId();
        this.itemAddress = item.getItemAddress();
        this.itemTitle = item.getItemTitle();
        this.dealStatus = item.getDealStatus();
        this.deliveryStatus = item.getDeliveryStatus();
        this.itemHidden = item.getItemHidden();
    }

    public ItemDto(Long itemId, String itemTitle, String itemContent, int itemPrice, String itemAddress,
                   ItemStatus itemStatus, DealStatus dealStatus, DealHow dealHow, DeliveryStatus deliveryStatus,
                   int itemDeliveryPrice, PriceStatus priceStatus, CategoryName categoryName, int itemCount,
                   ItemHidden itemHidden, Timestamp itemResistDate, Timestamp itemUpdateDate, Long userSearchId,
                   List<ItemImg> itemImgs, List<ItemTag> itemTags, int itemChecksSize, int itemLikesSize, Long userId) {
        this.itemId = itemId;
        this.itemTitle = itemTitle;
        this.itemContent = itemContent;
        this.itemPrice = itemPrice;
        this.itemAddress = itemAddress;
        this.itemStatus = itemStatus;
        this.dealStatus = dealStatus;
        this.dealHow = dealHow;
        this.deliveryStatus = deliveryStatus;
        this.itemDeliveryPrice = itemDeliveryPrice;
        this.priceStatus = priceStatus;
        this.categoryName = categoryName;
        this.itemCount = itemCount;
        this.itemHidden = itemHidden;
        this.itemResistDate = itemResistDate;
        this.itemUpdateDate = itemUpdateDate;
        this.userSearchId = userSearchId;
        this.itemImgs = itemImgs; // 변경된 부분
        this.itemTags = itemTags;
        this.itemChecksSize = itemChecksSize;
        this.itemLikesSize = itemLikesSize;
        this.userId = userId;
    }

    // 상대 시간을 계산하는 메서드
    public String getRelativeTime() {
        Instant now = Instant.now();
        Instant updateTime = itemUpdateDate.toInstant();
        Duration duration = Duration.between(updateTime, now);

        long seconds = duration.getSeconds();
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();

        if (days > 0) {
            return days + "일 전";
        } else if (hours > 0) {
            return hours + "시간 전";
        } else if (minutes > 0) {
            return minutes + "분 전";
        } else {
            return seconds + "초 전";
        }
    }
}