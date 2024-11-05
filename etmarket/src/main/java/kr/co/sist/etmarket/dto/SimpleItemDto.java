package kr.co.sist.etmarket.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class SimpleItemDto {

    private Long itemId;
    private String itemMainImg;
    private String itemPrice;
}
