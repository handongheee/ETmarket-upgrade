package kr.co.sist.etmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter @Setter
public class ItemDetailImgDto {

    private Long itemImgId;
    private String itemImgUrl;
    private Timestamp resistDate;
    private Timestamp updateDate;
}

