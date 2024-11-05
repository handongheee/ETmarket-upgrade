package kr.co.sist.etmarket.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemLikeDto {

    private Long itemId;
    private Long likeCount;

}

