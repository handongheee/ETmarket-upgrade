package kr.co.sist.etmarket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishRequest {
    private Long itemId;
    private Long userId;

}