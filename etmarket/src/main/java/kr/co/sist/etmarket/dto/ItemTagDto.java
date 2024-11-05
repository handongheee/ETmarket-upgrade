package kr.co.sist.etmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemTagDto {
    private Long itemTagId;

    private Long itemId;

    private String itemTag;

    private String itemTagText;
}
