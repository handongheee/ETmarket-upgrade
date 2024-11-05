package kr.co.sist.etmarket.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import kr.co.sist.etmarket.dto.UserDto;
import kr.co.sist.etmarket.etenum.UserStatus;
import lombok.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder(toBuilder = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String userLoginId;

    @Column
    private String userPassword;

    @Column(unique = true)
    private String userName;

    @Column
    private String userPhone;

    @Column(unique = true)
    private String userEmail;

    @Column
    private String userImg;

    @Column(updatable = false)
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Timestamp userCreateDate;

    @Column
    private String userJoinType;

    @Column
    private String userSocialToken;

    @Column
    private String userIntroduce;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemLike> itemLikes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSearch> userSearches = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Deal> sellers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Deal> buyers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "target", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> target = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoom> senderChatroom = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoom> receiverChatroom = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemUp> itemUps = new ArrayList<>();


    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userLoginId='" + userLoginId + '\'' +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userImg='" + userImg + '\'' +
                ", userCreateDate=" + userCreateDate +
                ", userJoinType='" + userJoinType + '\'' +
                ", userSocialToken='" + userSocialToken + '\'' +
                ", userIntroduce='" + userIntroduce + '\'' +
                ", userStatus=" + userStatus +
//                ", items=" + items +
//                ", itemLikes=" + itemLikes +
//                ", userSearches=" + userSearches +
//                ", sellers=" + sellers +
//                ", buyers=" + buyers +
//                ", target=" + target +
                ", senderChatroom=" + senderChatroom +
                ", receiverChatroom=" + receiverChatroom +
                '}';
    }



    // UserDto를 User 엔티티로 변환하는 메소드
    public static User fromDto(UserDto userDto) {
        return User.builder()
                .userId(userDto.getUserId())
                .userLoginId(userDto.getUserLoginId())
                .userPassword(userDto.getUserPassword())
                .userName(userDto.getUserName())
                .userPhone(userDto.getUserPhone())
                .userEmail(userDto.getUserEmail())
                .userImg(userDto.getUserImg())
                .userCreateDate(userDto.getUserCreateDate())
                .userJoinType(userDto.getUserJoinType())
                .userSocialToken(userDto.getUserSocialToken())
                .userIntroduce(userDto.getUserIntroduce())
                .userStatus(userDto.getUserStatus())
                .build();
    }
}
