package kr.co.sist.etmarket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import kr.co.sist.etmarket.dto.ChatRoomDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChattingService {

    private final ObjectMapper objectMapper;
    private Map<String, ChatRoomDto> chatRooms;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>(); // 정렬됨
    }

    public List<ChatRoomDto> findAllRoom(){
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoomDto findRoomById(String chatroomId){
        return chatRooms.get(chatroomId);
    }

//    public ChatRoomDto createRoom(String roomName) {
//        String chatroomId = UUID.randomUUID().toString();
//        ChatRoomDto chatRoomDto = new ChatRoomDto(chatroomId, roomName);
//        chatRooms.put(chatroomId, chatRoomDto);
//
//        return chatRoomDto;
//    }

}
