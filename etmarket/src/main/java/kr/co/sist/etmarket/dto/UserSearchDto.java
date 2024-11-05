package kr.co.sist.etmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchDto {

    private Long userSearchId;

    private Long userId;

    private String content;

    private Timestamp resistDate;

    private Timestamp updateDate;

    public UserSearchDto(Long userId, String content) {
        this.userId = userId;
        this.content = content;
    }

    public UserSearchDto(Long userId, String content, Timestamp updateDate) {
        this.userId = userId;
        this.content = content;
        this.updateDate = updateDate;
    }

    public UserSearchDto(String content) {
        this.content = content;
    }
}
