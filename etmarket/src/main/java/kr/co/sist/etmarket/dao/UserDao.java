package kr.co.sist.etmarket.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import kr.co.sist.etmarket.dto.UserDto;

import kr.co.sist.etmarket.entity.User;

import java.util.List;

public interface UserDao extends JpaRepository<User, Long> {
	// 로그인
	User findByUserLoginId(String userLoginId);

	// 아이디 찾기
	User findByUserPhoneAndUserEmail(String userPhone, String userEmail);

	// 비밀번호 찾기
	User findByUserLoginIdAndUserEmail(String userLoginId, String userEmail);

	// 비밀번호 재설정
	@Query(value="update user set user_password=:userPassword where user_login_id=:userLoginId AND user_email=:userEmail", nativeQuery = true)
	@Modifying
	@Transactional
	public void updateUserPassword(@Param("userLoginId") String userLoginId, @Param("userEmail") String userEmail, @Param("userPassword") String userPassword);

	// 회원가입
	User save(UserDto userDto);
	
	// 중복체크 true/false
	boolean existsByUserLoginId(String userLoginId);
	boolean existsByUserName(String userName);
	boolean existsByUserEmail(String userEmail);

	User findByUserName(String userName);

	// 총 사용자 수 구하기
	@Query("SELECT COUNT(u) FROM User u")
	long countTotalUsers();

	// 오늘 신규 가입자 수 구하기
	@Query("SELECT COUNT(u) FROM User u WHERE u.userCreateDate >= CURRENT_DATE")
	long countNewUsersToday();

	// 활성 사용자 수 구하기
	@Query("SELECT COUNT(u) FROM User u WHERE u.userStatus = 'ACTIVE'")
	long countActiveUsers();

    // 가입 수단별 사용자 수 구하기

	// 일반 사용자 수 구하기
	@Query("SELECT COUNT(u) FROM User u WHERE u.userJoinType = 'GENERAL'")
	long countGeneralUsers();

	// 카카오 사용자 수 구하기
	@Query("SELECT COUNT(u) FROM User u WHERE u.userJoinType = 'KAKAO'")
	long countKakaoUsers();

	// 네이버 사용자 수 구하기
	@Query("SELECT COUNT(u) FROM User u WHERE u.userJoinType = 'NAVER'")
	long countNaverUsers();

	// 최근 가입자 5명 가져오기
	List<User> findTop5ByOrderByUserCreateDateDesc();

	// 월별 사용자 가입 수 구하기
	@Query("SELECT FUNCTION('MONTH', u.userCreateDate) as month, COUNT(u) as count FROM User u GROUP BY FUNCTION('MONTH', u.userCreateDate)")
	List<Object[]> countMonthlyUserSignups();

	/*마이페이지에서 사용*/
	User findByUserId(Long userId);


// 중복체크 true/false
// boolean existsByUserLoginId(String userLoginId);
// boolean existsByUserName(String userName);
// boolean existsByUserEmail(String userEmail);

// 회원정보 수정
//	@Query(value="update user set user_password=:userPassword where user_id=:userId", nativeQuery = true)
//	@Modifying
//	@Transactional
//	public void updateUser(@Param("userLoginId") String userLoginId, @Param("userEmail") String userEmail, @Param("userPassword") String userPassword);

// 회원 탈퇴 status ACTIVE->DELETE
@Query(value="update user set user_status='DELETE' where user_id=:userId", nativeQuery = true)
@Modifying
@Transactional
public void deleteUser(@Param("userId") Long userId);
	
}