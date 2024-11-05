package kr.co.sist.etmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ItemImgDto {
    private Long itemImgId;

    private Long itemId;

    private String itemImg;

    private String itemDeleteImgIds;

    private int itemImgUploadCount;

    public ItemImgDto(Long itemImgId, String itemImg) {
        this.itemImgId = itemImgId;
        this.itemImg = itemImg;
    }
}