package kr.co.sist.etmarket.dto;
import kr.co.sist.etmarket.entity.User;

import kr.co.sist.etmarket.etenum.UserStatus;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

	private Long userId;

	//    @NotBlank(message = "아이디를 입력해주세요.")
//    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z]).{6,12}", message = "영문, 숫자 사용 6~12자로 입력해주세요.")
	private String userLoginId;

	private String userPassword;

	private String userName;

	private String userPhone;

	private String userEmail;

	private String userImg;

	private Timestamp userCreateDate;

	private String userJoinType;

	private String userSocialToken;

	private UserStatus userStatus;

	private String userIntroduce;

	@Builder.Default
	private List<ItemDto> items = new ArrayList<>();

//    private List<ItemLike> itemLikes = new ArrayList<>();

	@Builder.Default
	private List<UserSearchDto> userSearches = new ArrayList<>();

//    private List<Deal> sellers = new ArrayList<>();
//
//    private List<Deal> buyers = new ArrayList<>();
//
//    private List<Rating> target = new ArrayList<>();

	// User 엔티티를 UserDto로 변환하는 메소드
	public static UserDto fromEntity(User user) {
		return UserDto.builder()
				.userId(user.getUserId())
				.userLoginId(user.getUserLoginId())
				.userPassword(user.getUserPassword())
				.userName(user.getUserName())
				.userPhone(user.getUserPhone())
				.userEmail(user.getUserEmail())
				.userImg(user.getUserImg())
				.userCreateDate(user.getUserCreateDate())
				.userJoinType(user.getUserJoinType())
				.userStatus(user.getUserStatus())
				.userIntroduce(user.getUserIntroduce()).build();
	}

	// UserDto를 User 엔티티로 변환하는 메소드
//	public User toEntity() {
//		return new User(
//				this.userId,
//				this.userLoginId,
//				this.userPassword,
//				this.userName,
//				this.userPhone,
//				this.userEmail,
//				this.userImg,
//				this.userCreateDate,
//				this.userJoinType,
//				this.userSocialToken,
//				this.userIntroduce,
//				this.userStatus,
//				new ArrayList<>(), // items
//				new ArrayList<>(), // itemLikes
//				new ArrayList<>(), // userSearches
//				new ArrayList<>(), // sellers
//				new ArrayList<>(), // buyers
//				new ArrayList<>(), // target
//				new ArrayList<>(), // senderChatroom
//				new ArrayList<>() // receiverChatroom
//		);
//	}

	// 비밀번호 암호화
	public void encryptPassword(String BCryptpassword) {
		this.userPassword=BCryptpassword;
	}

	/*마이페이지에서 사용*/
	public UserDto(Long userId, String userName) {
		this.userId = userId;
		this.userName = userName;
	}
}