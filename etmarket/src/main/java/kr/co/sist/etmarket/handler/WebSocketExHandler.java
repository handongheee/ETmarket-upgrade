package kr.co.sist.etmarket.handler;

import kr.co.sist.etmarket.dto.ChatRoomDto;
import kr.co.sist.etmarket.dto.UserDto;
import kr.co.sist.etmarket.entity.Message;
import kr.co.sist.etmarket.entity.User;
import kr.co.sist.etmarket.service.ChatRoomService;
import kr.co.sist.etmarket.service.ItemService;
import kr.co.sist.etmarket.service.MessageService;
import kr.co.sist.etmarket.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketExHandler extends TextWebSocketHandler {

    HashMap<String, WebSocketSession> sessionMap = new HashMap<>(); //웹소켓 세션을 담아둘 맵
//    List<HashMap<String, Object>> roomListSession = new ArrayList<>(); //웹소켓 세션을 담아둘 리스트

    //로그인 한 인원 전체
    private List<WebSocketSession> sessions = new ArrayList<WebSocketSession>();

    private final MessageService messageService;
    private final ChatRoomService chatRoomService;
    private final ItemService itemService;
    private final UserService userService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 메세지 발송
        String msg = message.getPayload();
        JSONObject obj = jsonToObjectParser(msg);
        System.out.println("handleTextMessage msg = " + obj);

        String userName = obj.get("userName").toString(); // 메세지를 보낸사람 (자기자신)
        Long itemId = Long.parseLong(obj.get("itemId").toString()); // 현재 대화중인 상품
        String itemUserName = itemService.getReceiverUserName(itemId); // item을 올린 사람 (판매자)
        String receiver = ""; // 메세지를 받는 사람
        String getMessage = obj.get("msg").toString();
        Long chatroomId = Long.parseLong(obj.get("chatroomId").toString()); // 현재 대화중인 상품

        System.out.println("if 전 chatroomId = " + chatroomId);

        // 로그인한 사람(자기자신)과 item 올린 사람이(판매자) 같으면
        if(userName.equals(itemUserName)){
            ChatRoomDto chatroom = chatRoomService.getChatroom(chatroomId); // 채팅방 데이터 가져옴
            System.out.println("get chatroomDto = " + chatroom.getChatroomId());

            if (chatroom != null) {
                Long chatroomSenderId = chatroom.getSenderId(); // 처음 메시지 보낸 user 가져옴 (구매자)
                System.out.println("chatroomSenderId = " + chatroomSenderId);
                UserDto sender = userService.getUserDtoById(chatroomSenderId);

                if (sender != null) {
                    receiver = sender.getUserName(); // sender와 item를 파는 사람이 같은 경우에는 상대방이 구매자임
                    System.out.println("itemId 같은 관계로 채팅방 sender 닉네임 = " + receiver);
                } else {
                    System.err.println("Sender not found for chatroomSenderId: " + chatroomSenderId);
                }
            } else {
                System.err.println("Chat room not found for chatroomId: " + chatroomId);
            }
        } else {
            receiver = itemUserName;
        }

        // userName과 sender는 같아야함
        System.out.println("sender = " + userName); // 나
        System.out.println("itemId = " + itemId);
        System.out.println("receiver = " + receiver); // 상대방
        System.out.println("getMessage = " + getMessage);
        System.out.println("chatroomId = " + chatroomId);
        System.out.println("itemUserName = " + itemUserName);

        System.out.println("sessionMap size: " + sessionMap.size());

        Message insertMessage = new Message(chatRoomService.getEntity(chatroomId),
                                            userName,
                                            receiver,
                                            getMessage);
        messageService.save(insertMessage);

        for(String key : sessionMap.keySet()) {
            WebSocketSession wss = sessionMap.get(key);
            try {
                String messageStr = obj.toJSONString();
                System.out.println("Sending message to session: " + key + ", message = " + messageStr);
                wss.sendMessage(new TextMessage(messageStr));
            }catch(Exception e) {
                System.err.println("Error sending message to session " + key);
                e.printStackTrace();
            }
        }

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 소켓 연결
        System.out.println("소켓 연결: " + session.getId());
        super.afterConnectionEstablished(session);

        sessionMap.put(session.getId(), session);
        sessions.add(session);

        JSONObject obj = new JSONObject();
        obj.put("type", "getId");
        obj.put("sessionId", session.getId());
        session.sendMessage(new TextMessage(obj.toJSONString()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 소켓 종료
        System.out.println("소켓 종료: " + session.getId());
        sessionMap.remove(session.getId());
        super.afterConnectionClosed(session, status);
    }

    private static JSONObject jsonToObjectParser(String jsonStr) {
        JSONParser parser = new JSONParser();
        JSONObject obj = null;
        try {
            obj = (JSONObject) parser.parse(jsonStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
