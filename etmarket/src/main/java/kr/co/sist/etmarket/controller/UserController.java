package kr.co.sist.etmarket.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import kr.co.sist.etmarket.dto.UserDto;
import kr.co.sist.etmarket.entity.User;
import kr.co.sist.etmarket.etenum.UserStatus;
import kr.co.sist.etmarket.service.S3Uploader;
import kr.co.sist.etmarket.service.UserService;

@Controller
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	S3Uploader s3Uploader;
	
	// 회원정보 수정 페이지로 이동
	@GetMapping("/seller/mypage")
	public ModelAndView goMypage(@RequestParam("userId") Long userId) {
		ModelAndView mview=new ModelAndView();
		
		User userInfo=userService.getUserById(userId);
		mview.addObject("userInfo", userInfo);
		
		mview.setViewName("myPage/mypageEdit");
		return mview;
	}
	
	// 회원정보 수정
	@PostMapping("/member/update")
	public String updateUser(@ModelAttribute UserDto userDto, @RequestParam("isDeleted") String isDeleted,
			@RequestParam(name="userImgUpload", required = false) MultipartFile userImgUpload) throws IOException {
		
//		System.out.println("수정 전: "+userDto);
//		System.out.println("이미지 업로드 값: "+userImgUpload);
//		System.out.println("삭제 유무확인 : "+isDeleted);
		
		if(("imgDeleted").equals(isDeleted)){
			// 이미지 삭제
			//System.out.println("db에 저장된 이름 확인 "+userDto.getUserImg());
	
			String img=userService.getPath(userDto.getUserImg()); // s3 주소 제외한 뒷부분 링크 얻어오기
			
			// s3 이미지 삭제
			s3Uploader.deleteFile(img);
			
			// 데이터베이스 값 'NoImage'로 변경
			userDto.setUserImg("NoImage");
			
		} else if(!userImgUpload.isEmpty()) {
			// 이미지 업로드 (수정)
			// s3 이미지 업로드
			String upload=s3Uploader.upload(userImgUpload, "userImg");
			// db 업로드
			userDto.setUserImg(upload);
		}
		
		// db 반영
		userService.update(userDto);

		// 이전 페이지로(내상점)
		return "redirect:/seller/"+userDto.getUserId()+"/items";
	}
	
	// 회원탈퇴
	@GetMapping("/member/delete")
	public String deleteUser(@RequestParam("userId") Long userId, UserDto userDto) {	
		userService.delete(userId);
		
		return "redirect:/";
	}
}
