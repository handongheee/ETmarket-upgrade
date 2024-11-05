package kr.co.sist.etmarket.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;
import kr.co.sist.etmarket.dto.UserDto;
import kr.co.sist.etmarket.etenum.UserStatus;
import kr.co.sist.etmarket.service.JoinService;

@Controller
public class JoinController {

	@Autowired
	JoinService joinService;
	
	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	// 가입폼으로 이동
	@GetMapping("/member/join")
	public String goJoin() {
		return "join/joinForm";
	}
	
	// 회원가입 save
	@PostMapping("/member/joinProcess")
	public String joinProcess(@ModelAttribute UserDto userDto,
			Errors errors, Model model) {
		
		userDto.setUserStatus(UserStatus.ACTIVE); //회원상태 세팅
		userDto.setUserJoinType("GENERAL"); // 가입유형 세팅
		userDto.setUserImg("NoImage"); // 이미지 세팅
		
		// 0702 비밀번호 암호화 추후 추가
		// userDto.encryptPassword(encoder.encode(userDto.getUserPassword())); // 비밀번호 암호화
		//userDto.setUserPassword(userDto.encryptPassword(userDto.getUserPassword()));
		
//		System.out.println("joinController 입력받은 값: "+userDto+", 인증코드: "+phoneVerifyCode);
		
		joinService.join(userDto);
		
		System.out.println("=============================================== 가입성공");
		
		return "redirect:/login";
	}

	// 중복 검사
	// 아이디
	@PostMapping("/member/join/existLoginId")
	public ResponseEntity<Boolean> existLoginId(@RequestParam("userLoginId") String userLoginId) {
		
		boolean id=joinService.loginIdExistChk(userLoginId);
		
		//System.out.println("joinController에서 아이디 중복 확인: "+id);
		
		if(id==false) {
			// 중복 아니면 false
			return ResponseEntity.ok(false);
		} else {
			// 중복이면 true
			return ResponseEntity.ok(true);
		}		
	}
	
	// 닉네임
	@PostMapping("/member/join/existName")
	public ResponseEntity<Boolean> existName(@RequestParam("userName") String userName) {

		boolean name = joinService.nameExistChk(userName);

		//System.out.println("joinController에서 닉네임 중복 확인: " + name);

		if (name == false) {
			// 중복 아니면 false
			return ResponseEntity.ok(false);
		} else {
			// 중복이면 true
			return ResponseEntity.ok(true);
		}
	}
	
	// 이메일
	@PostMapping("/member/join/existEmail")
	public ResponseEntity<Boolean> existEmail(@RequestParam("userEmail") String userEmail) {

		boolean email = joinService.emailExistChk(userEmail);

		//System.out.println("joinController에서 닉네임 중복 확인: " + email);

		if (email == false) {
			// 중복 아니면 false
			return ResponseEntity.ok(false);
		} else {
			// 중복이면 true
			return ResponseEntity.ok(true);
		}
	}
	
	
}
