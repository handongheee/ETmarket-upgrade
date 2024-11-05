package kr.co.sist.etmarket.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import kr.co.sist.etmarket.etenum.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder(toBuilder = true)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(updatable = false)
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Timestamp itemResistDate;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Timestamp itemUpdateDate;

    private String itemTitle;

    private String itemContent;

    private int itemPrice;

    private String itemAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_status")
    private ItemStatus itemStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_deal_status")
    private DealStatus dealStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_deal_how")
    private DealHow dealHow;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_delivery_status")
    private DeliveryStatus deliveryStatus;

    private int itemDeliveryPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_price_status")
    private PriceStatus priceStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_category_name")
    private CategoryName categoryName;

    private int itemCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_hidden")
    private ItemHidden itemHidden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_search_id")
    private UserSearch userSearch;

    @Builder.Default
    @JsonIgnoreProperties
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
//    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true) // 조회시 같이 조회되게 LAZY 없앰
    private List<ItemTag> itemTags = new ArrayList<>(); // 상품 태그

    @Builder.Default
    @JsonIgnoreProperties
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
//    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true) // 조회시 같이 조회되게 LAZY 없앰
    private List<ItemImg> itemImgs = new ArrayList<>(); // 상품 이미지

    @Builder.Default
    @JsonIgnoreProperties
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ItemCheck> itemChecks = new ArrayList<>(); // 상품 조회

    @Builder.Default
    @JsonIgnoreProperties
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ItemLike> itemLikes = new ArrayList<>(); // 상품 좋아요

    @OneToOne(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private ItemUp itemUp;

    @Builder.Default
    @JsonIgnoreProperties
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Deal> deals = new ArrayList<>(); // 거래

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", itemTitle='" + itemTitle + '\'' +
                ", itemContent='" + itemContent + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemAddress='" + itemAddress + '\'' +
                ", itemStatus=" + itemStatus +
                ", dealStatus=" + dealStatus +
                ", dealHow=" + dealHow +
                ", deliveryStatus=" + deliveryStatus +
                ", itemDeliveryPrice=" + itemDeliveryPrice +
                ", priceStatus=" + priceStatus +
                ", categoryName=" + categoryName +
                ", itemCount=" + itemCount +
                ", itemHidden=" + itemHidden +
                ", itemResistDate=" + itemResistDate +
                ", itemUpdateDate=" + itemUpdateDate +
                ", userSearchId=" + (userSearch != null ? userSearch.getUserSearchId() : null) +
                ", itemTags=" + itemTags.size() +
                ", itemImgs=" + itemImgs.size() +
                ", itemChecks=" + itemChecks.size() +
                ", itemLikes=" + itemLikes.size() +
                '}';
    }
}