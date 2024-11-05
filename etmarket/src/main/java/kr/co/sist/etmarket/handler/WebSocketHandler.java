//package kr.co.sist.etmarket.handler;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import kr.co.sist.etmarket.dto.ChatRoomDto;
//import kr.co.sist.etmarket.dto.MessageDto;
//import kr.co.sist.etmarket.service.ChattingService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Set;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class WebSocketHandler extends TextWebSocketHandler {
//
////    HashMap<String, WebSocketSession> sessionMap = new HashMap<>(); //웹소켓 세션을 담아둘 맵
//    List<HashMap<String, Object>> roomListSession = new ArrayList<>(); //웹소켓 세션을 담아둘 리스트
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        // 메세지 발송
//        String msg = message.getPayload();
//        JSONObject obj = jsonToObjectParser(msg); // json 파싱
//
//        String chatroomId = (String) obj.get("chatroomId");
//        HashMap<String, Object> temp = new HashMap<>();
//        if (roomListSession.size() > 0) {
//            for (int i = 0; i < roomListSession.size(); i++) {
//                String roomNumber = (String) roomListSession.get(i).get("chatroomId"); // 세션리스트에 저장된 방번호 가져옴
//                if (roomNumber.equals(chatroomId)) { // 같은값 방이 존재하면
//                    temp = roomListSession.get(i); // 해당 방번호에 세션리스트가 존재하는 모든 object 값을 가져옴
//                    break;
//                }
//            }
//
//            for (String k : temp.keySet()) { // 해당 방 세션들만 찾아서 메시지 전송
//                if (k.equals("chatroomId")) { // 다만 방번호일 경우 건너뜀
//                    continue;
//                }
//
//                WebSocketSession wss = (WebSocketSession) temp.get(k);
//                if (wss != null) {
//                    try {
//                        wss.sendMessage(new TextMessage(obj.toJSONString()));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//        }
//
//    }
//
//    @SuppressWarnings("unchecked") // 컴파일러 경고를 무시
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        // 소켓 연결
//        System.out.println("소켓 연결");
//        super.afterConnectionEstablished(session);
//
//        boolean flag = false;
//        String url = session.getUri().toString();
//        System.out.println("url = " + url);
//
//        String chatroomId = url.split("/chating/")[1];
//        System.out.println("split chatroomId = " + chatroomId);
//
//        int idx = roomListSession.size();
//        if (roomListSession.size() > 0) {
//            for (int i = 0; i < roomListSession.size(); i++) {
//                String roomNumber = (String) roomListSession.get(i).get("chatroomId");
//                if (roomNumber.equals(chatroomId)) {
//                    flag = true;
//                    idx = i;
//                    break;
//                }
//            }
//        }
//
//        if (flag) { // 존재하는 방이라면 세션만 추가
//            HashMap<String, Object> map = roomListSession.get(idx);
//            map.put(session.getId(), session);
//        } else { // 최초 생성하는 방이라면 방번호와 세션 추가
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("chatroomId", chatroomId);
//            map.put(session.getId(), session);
//            roomListSession.add(map);
//        }
//
//        // 세션 등록이 끝나면 발급 받은 세션Id 값의 메시지 발송
//        JSONObject obj = new JSONObject();
//        obj.put("type", "getId");
//        obj.put("sessionId", session.getId());
//        session.sendMessage(new TextMessage(obj.toJSONString()));
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        // 소켓 종료
//        System.out.println("소켓 종료");
//
//        if (roomListSession.size() > 0) { // 소켓이 종료되면 해당 세션값들을 찾아 삭제
//            for (int i = 0; i < roomListSession.size(); i++) {
//                roomListSession.get(i).remove(session.getId());
//            }
//        }
//        super.afterConnectionClosed(session, status);
//    }
//
//    private static JSONObject jsonToObjectParser(String jsonStr) {
//        JSONParser parser = new JSONParser();
//        JSONObject obj = null;
//        try {
//            obj = (JSONObject) parser.parse(jsonStr);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return obj;
//    }
//}
