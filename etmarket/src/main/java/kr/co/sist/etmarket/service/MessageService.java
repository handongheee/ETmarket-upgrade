package kr.co.sist.etmarket.service;

import kr.co.sist.etmarket.dao.MessageDao;
import kr.co.sist.etmarket.dto.MessageDto;
import kr.co.sist.etmarket.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final MessageDao messageDao;

    public void save(Message message) {
        messageDao.save(message);
    }

    public List<MessageDto> getMessagesByChatRoomId(Long chatRoomId) {
        List<Message> messages = messageDao.findByChatRoom_ChatroomId(chatRoomId);

        return messages.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private MessageDto convertToDto(Message message) {
        return MessageDto.builder()
                .messageId(message.getMessageId().intValue())
                .message(message.getMessage())
                .sendDate(message.getSendDate())
                .sender(message.getSender())
                .receiver(message.getReceiver())
                .chatroomId(message.getChatRoom().getChatroomId())
                .img(message.getImg())
                .chatRead(message.getChatRead())
                .build();
    }

}
