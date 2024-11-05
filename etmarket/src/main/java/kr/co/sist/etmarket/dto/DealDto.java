package kr.co.sist.etmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealDto {
    private Long dealId;
    private Long itemId;
    private Long buyerId;
    private Long sellerId;
    private int itemPrice;
    private Timestamp dealDate;
    private String dealMethod;
    private String ratingLeft;
}
