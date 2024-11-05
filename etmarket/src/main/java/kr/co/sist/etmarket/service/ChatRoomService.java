package kr.co.sist.etmarket.service;

import kr.co.sist.etmarket.dao.ChatRoomDao;
import kr.co.sist.etmarket.dto.ChatRoomCountDto;
import kr.co.sist.etmarket.dto.ChatRoomDto;
import kr.co.sist.etmarket.entity.ChatRoom;
import kr.co.sist.etmarket.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomDao chatRoomDao;

    public void save(ChatRoom chatRoom) {
        chatRoomDao.save(chatRoom);
    }

    public boolean findChatRoomByItemIdAndSenderId(Long itemId, Long senderId) {
        ChatRoom chatRoom = chatRoomDao.findByItem_ItemIdAndSender_UserId(itemId, senderId);

        ChatRoomDto chatRoomDto = convertToDto(chatRoom);
        if (chatRoomDto != null) {
            return true;
        } else {
            return false;
        }

    }

    public ChatRoomDto getChatroom(Long chatroomId) {
//        return convertToDto(chatRoomDao.findByChatroomId(chatroomId));
        ChatRoom chatRoom = chatRoomDao.findById(chatroomId).orElse(null);
        if (chatRoom == null) {
            return null;
        }
        return convertToDto(chatRoom);
    }

    public List<ChatRoomDto> findAllBySender(User sender) {
        List<ChatRoom> chatRooms =  chatRoomDao.findBySender(sender);
        return chatRooms.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    public List<ChatRoomDto> findAllByUser(User user) {
        List<ChatRoom> chatRooms = chatRoomDao.findBySenderOrReceiver(user);
        return chatRooms.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ChatRoomDto> findAllRoomName() {
        List<ChatRoom> chatRooms = chatRoomDao.findAll();

        return chatRooms.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ChatRoomDto convertToDto(ChatRoom chatRoom) {
        if (chatRoom == null) {
            return null;
        }
        return ChatRoomDto.builder()
                .chatroomId(chatRoom.getChatroomId())
                .createDate(chatRoom.getCreateDate())
                .itemId(chatRoom.getItem().getItemId())
                .senderId(chatRoom.getSender().getUserId())
                .receiverId(chatRoom.getReceiver().getUserId())
                .messages(chatRoom.getMessages())
                .chatroomImg(chatRoom.getChatroomImg())
                .build();
    }

    public ChatRoom getEntity(Long chatroomId) {
        return chatRoomDao.findById(chatroomId).orElse(null);
    }
  
  /*마이페이지에서 사용*/
    public List<ChatRoomCountDto> getChatRoomCountByItemId() {
        List<Object[]> results = chatRoomDao.countChatRoomsByItemId();
        return results.stream()
                .map(result -> new ChatRoomCountDto(
                        (Long) result[0],
                        (Long) result[1]
                ))
                .collect(Collectors.toList());
    }

    public List<User> getChatParticipantNamesByItemId(Long itemId) {
        List<User> participantIds = new ArrayList<>();
        List<ChatRoom> chatRooms = chatRoomDao.findByItem_ItemId(itemId); // Item ID로 조회하도록 수정
        for (ChatRoom chatRoom : chatRooms) {
            participantIds.add(chatRoom.getSender()); // 채팅에 참여한 유저 객체 가져오기 (senderId)
        }
        return participantIds;
    }

    public Long getSellerIdByItemId(Long itemId) {
        ChatRoom chatRoom = chatRoomDao.findByItem_ItemId(itemId).stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found for item ID"));
        return chatRoom.getReceiver().getUserId(); // 판매자 ID 반환
    }
}
