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
public class ItemUpDto {
    private Long itemId;
    private Long userId;
    private int itemUpCount;
    private Timestamp itemUpDate;
}
