package kr.co.sist.etmarket.dao;

import kr.co.sist.etmarket.entity.ChatRoom;
import kr.co.sist.etmarket.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageDao extends JpaRepository<Message, Long> {

    // chatRoomId로 메시지 가져오기
    List<Message> findByChatRoom_ChatroomId(Long chatRoomId);

}
