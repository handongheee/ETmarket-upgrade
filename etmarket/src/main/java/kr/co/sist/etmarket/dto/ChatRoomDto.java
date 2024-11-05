package kr.co.sist.etmarket.dto;

import kr.co.sist.etmarket.entity.Item;
import kr.co.sist.etmarket.entity.Message;
import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class ChatRoomDto {

    private Long chatroomId;

    private String roomName;

    private Timestamp createDate;

    private Long itemId; // 상품 id

    private Long senderId; // 최초 메세지 보낸 사람 id

    private Long receiverId; // 최초 메제지 받는 사람 id

    @Builder.Default
    private List<Message> messages = new ArrayList<>();

    private String chatroomImg;

    public ChatRoomDto(Long chatroomId, String roomName) {
        this.chatroomId = chatroomId;
        this.roomName = roomName;
    }

}
