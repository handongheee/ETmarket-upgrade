package kr.co.sist.etmarket.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatRoomCountDto {
    private Long itemId;
    private Long chatRoomCount;

    public ChatRoomCountDto(Long itemId, Long chatRoomCount) {
        this.itemId = itemId;
        this.chatRoomCount = chatRoomCount;
    }

}
