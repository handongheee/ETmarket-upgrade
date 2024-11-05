package kr.co.sist.etmarket.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.etmarket.dto.ChatRoomDto;
import kr.co.sist.etmarket.dto.ItemDto;
import kr.co.sist.etmarket.dto.MessageDto;
import kr.co.sist.etmarket.entity.ChatRoom;
import kr.co.sist.etmarket.entity.User;
import kr.co.sist.etmarket.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChattingExController {

    private final ItemService itemService;
    private final ItemImgService itemImgService;
    private List<ChatRoomDto> roomDtoList = new ArrayList<>();
    static int chatroomId = 0;
    private final MessageService messageService;
    private final ChatRoomService chatRoomService;
    private final UserService userService;


    @GetMapping("/et/chat")
    public String chatNotItemId(@RequestParam String userName, Model model) {
        System.out.println("userName = " + userName);
        model.addAttribute("userName", userName);
        return "chating/ex";
    }

//    @GetMapping("/et/chat/{itemId}")
//    public String chatFindItemId(@RequestParam HashMap<Object, Object> params,
//                       @RequestParam String userName,
//                       @PathVariable String itemId,
//                       Model model) {
//        // itemId가 경로 변수로 전달됩니다.
//        model.addAttribute("userName", userName);
//        model.addAttribute("itemId", itemId);
//        return "chating/ex";
//    }
    @GetMapping("/et/chat/choose")
    public String chat(@RequestParam HashMap<Object, Object> params,
                       @RequestParam String userName,
                       @RequestParam String itemId,
                       Model model) {

        System.out.println("/et/chat/choose userName = " + userName);
        System.out.println("/et/chat/choose itemId = " + itemId);

        model.addAttribute("userName", userName);
        model.addAttribute("itemId", itemId);

        return "chating/ex";
    }


    /**
     * 방 정보 가져오기
     */
    @PostMapping("/et/chat/getRooms")
    public @ResponseBody List<ChatRoomDto> getRoom(@RequestParam HashMap<Object, Object> params,
                                                   @RequestParam String userName,
                                                   @RequestParam String itemId) {

        System.out.println("/et/chat/getRooms params = " + params);
        System.out.println("/et/chat/getRooms userName = " + userName);
        System.out.println("/et/chat/getRooms itemId = " + itemId);

        // 해당 닉네임을 가진 회원 찾음
        User sender = userService.getUserName(userName);

        if (itemId != null && isNumeric(itemId)) { // itemId가 null이 아니면

            Long itemIdLong = Long.valueOf(itemId); // 타입 변환
            User receiver = itemService.getItem(itemIdLong).getUser(); // 상품 주인(판매자)

            // 해당 itemId와 senderId에 대한 방이 존재하지 않으면
            // => 여기에 sender 랑 receiver Id 값이 값지 않은 것도 확인해줘야호미 (추가)
            if (!chatRoomService.findChatRoomByItemIdAndSenderId(itemIdLong, sender.getUserId())
                && !sender.getUserId().equals(receiver.getUserId())) {
                // 정보 저장 후 db에 저장
                ChatRoom room = new ChatRoom(itemService.getItem(itemIdLong), // item
                        sender, // sender
                        receiver, // receiver => 해당 상품 등록한 회원
                        itemImgService.getFirstItemImgByItemId(itemIdLong)); // img

                chatRoomService.save(room); // 방 생성
                System.out.println("save room");
            }

        }

//        return chatRoomService.findAllBySender(sender);
        return chatRoomService.findAllByUser(sender);

    }

    private boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @PostMapping("/et/chat/getChat")
    public @ResponseBody ItemDto getImg(@RequestParam String userName,
                          @RequestParam String itemId,
                          @RequestParam String chatroomId) {

        return itemService.findItem(Long.valueOf(itemId));

    }


    /**
     * 채팅 정보
     */
    @PostMapping("/et/chat/getMessage")
    public @ResponseBody List<MessageDto> getMessages(@RequestParam HashMap<Object, Object> params) {

        Long chatRoomId = Long.valueOf(params.get("chatroomId").toString());
        return messageService.getMessagesByChatRoomId(chatRoomId);

    }


}
