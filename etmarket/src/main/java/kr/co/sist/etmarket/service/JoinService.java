package kr.co.sist.etmarket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.etmarket.dao.UserDao;
import kr.co.sist.etmarket.dto.UserDto;
import kr.co.sist.etmarket.entity.User;

@Service
public class JoinService {
	
	@Autowired
	UserDao userDao;
	
	// 회원가입
    public void join(UserDto userDto) {
        //System.out.println("joinService에서 userDto 출력: " + userDto);

        // UserDto를 User 엔티티로 변환
        User user = User.fromDto(userDto);


        // User 엔티티를 저장
        userDao.save(user);
    }
    
    // 아이디 중복 체크
    public boolean loginIdExistChk(String userLoginId) {
    	boolean chk = userDao.existsByUserLoginId(userLoginId);
    	//System.out.println("joinService에서 아이디 중복 체크값: "+chk);
    	return chk;
    }
    
	// 닉네임 중복 체크
    public boolean nameExistChk(String userName) {
    	boolean chk = userDao.existsByUserName(userName);
    	//System.out.println("joinService에서 닉네임 중복 체크값: "+chk);
    	return chk;
    }
    
    // 이메일 중복 체크
    public boolean emailExistChk(String userEmail) {
    	boolean chk = userDao.existsByUserEmail(userEmail);
    	//System.out.println("joinService에서 이메일 중복 체크값: "+chk);
    	return chk;
    }

}
