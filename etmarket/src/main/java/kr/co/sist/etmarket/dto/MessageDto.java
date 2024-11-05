package kr.co.sist.etmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MessageDto {

    private int messageId;

    private String message;

    private Timestamp sendDate;

    private String sender;

    private String receiver;

    private Long chatroomId;

    private String img;

    private String chatRead; // 읽은 여부

}
