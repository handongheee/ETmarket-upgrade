/**
 * 
 */
	

$(function() {

	$(document).ready(function() {
		// 이미지 미리보기
		let photo_path = $('.profileImg').attr('src');
		let my_photo;

		$('#userImgUpload').change(function() {
			my_photo = this.files[0];
			if (!my_photo) {
				$('.profileImg').attr('src', photo_path);
				return;
			}
			if (my_photo.size > 1024 * 1024) {
				alert(Math.round(my_photo.size / 1024 / 1024) + 'MB(1MB까지만 업로드 가능)');
				$('.profileImg').attr('src', photo_path);
				$(this).val('');
				return;
			}

			// 이미지 미리보기 처리
			let reader = new FileReader();
			reader.readAsDataURL(my_photo);

			reader.onload = function() {
				$('.profileImg').attr('src', reader.result);
			};
		});
	
	});
	
	// 이미지 삭제 시
	$("#delImg").click(function() {
		$("#profileImg").attr("src", "../image/basic_profile_image.png");
		$("#isDeleted").val("imgDeleted");
	});
	
	
	// 유효성 체크 
	// 닉네임 유효성 체크
	$("#userName").blur(function() {
		var userName=$(this).val();
		
		// 원래대로
		$("#nameOverError").hide();
		$("#nameBlankError").hide();
		
		// 닉네임 공백 체크
	    if (userName == "") {
	        $("#nameBlankError").show();
	        $("#btnEdit").prop('disabled', true);
	        $("#btnEdit").css("background-color", "#aaa");
	        return; // 공백이면 더 이상 진행하지 않음
	    }
		
		// 닉네임 중복 체크
		$.ajax({
			type: "post",
			url: "/member/join/existName",
			dataType: "json",
			data: { "userName": userName},
			success: function(res) {
				// 중복된 값이면 에러 출력
				if(res==true){
					$("#nameOverError").show();
				}
			}
		});
		
	});
	
	// 비밀번호 유효성 체크
	$("#userPassword").blur(function() {
		var userPassword=$(this).val();
		
		// 원래대로
		$("#passwordRuleError").hide();
		$("#passwordBlankError").hide();
		$("#btnEdit").prop('disabled',false);
		$("#btnEdit").css("background-color","#4eb006");
		
		// 비밀번호 공백 체크
	    if (userPassword == "") {
	        $("#passwordBlankError").show();
	        $("#btnEdit").prop('disabled', true);
	        $("#btnEdit").css("background-color", "#aaa");
	        return; // 공백이면 더 이상 진행하지 않음
	    }
		
		// 비밀번호 양식
		let passwordRule = /^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*+=-_]).{8,16}$/;
		
		if(!passwordRule.test(userPassword)){
			$("#passwordRuleError").show();
			
			// 회원가입 차단
			$("#btnEdit").prop('disabled',true);
			$("#btnEdit").css("background-color","#aaa");
		}
	});
	
	// 비밀번호 확인 유효성 체크
	$("#userPasswordChk").blur(function() {
		var userPassword=$("#userPassword").val();
		var userPasswordChk=$("#userPasswordChk").val();

		// 원래대로
		$("#passwordChkError").hide();
		$("#passwordChkBlankError").hide();
		
		// 비밀번호와 비밀번호 확인이 일치하지 않을 경우 문구 출력
		if(userPassword!=userPasswordChk){
			$("#passwordChkError").show();
		}
		
		// 비밀번호 공백 체크
	    if (userPassword == "") {
	        $("#passwordChkBlankError").show();
	        $("#btnEdit").prop('disabled', true);
	        $("#btnEdit").css("background-color", "#aaa");
	        return; // 공백이면 더 이상 진행하지 않음
	    }
	    
	});
	
	// 휴대폰번호 유효성 체크, 인증번호 전송
	$("#userPhone").blur(function() {
		//alert("블러");
		var userPhone=$(this).val();
		
		// 원래대로
		$("#phoneRuleError").hide();
		$("#phoneBlankError").hide();
		$("#btnEdit").prop('disabled',false);
		$("#btnEdit").css("background-color","#4eb006");
		
		// 전화번호 공백 체크
	    if (userPhone == "") {
	        $("#phoneBlankError").show();
	        $("#btnEdit").prop('disabled', true);
	        $("#btnEdit").css("background-color", "#aaa");
	        return; // 공백이면 더 이상 진행하지 않음
	    }
	    
		// 전화번호 양식
		let phoneRule = /^01(0|1|[6-9])[0-9]{3,4}[0-9]{4}$/;
		
		if(!phoneRule.test(userPhone)){
			$("#phoneRuleError").show();
			
			// 회원가입 차단
			$("#btnEdit").prop('disabled',true);
			$("#btnEdit").css("background-color","#aaa");
		}
		
		// 인증번호 전송
		
		
	});
	
	// 인증번호 체크
	$("#phoneVerifyCode").blur(function() {
		//alert("블러");
	});
	
	// 이메일 유효성 체크
	$("#userEmail").blur(function() {
		var userEmail=$(this).val();
		//alert(userEmail);
		
		// 원래대로
		$("#emailRuleError").hide();
		$("#emailOverError").hide();
		$("#emailBlankError").hide();
		$("#btnEdit").prop('disabled',false);
		$("#btnEdit").css("background-color","#4eb006");
		
		// 이메일 공백 체크
	    if (userEmail == "") {
	        $("#emailBlankError").show();
	        $("#btnEdit").prop('disabled', true);
	        $("#btnEdit").css("background-color", "#aaa");
	        return; // 공백이면 더 이상 진행하지 않음
	    }
		
		// 이메일 양식 체크
		let emailRule = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
		
		if(!emailRule.test(userEmail)){
			$("#emailRuleError").show();
			
			// 회원가입 차단
			$("#btnEdit").prop('disabled',true);
			$("#btnEdit").css("background-color","#aaa");
		}
		
		// 이메일 중복 체크
		$.ajax({
			type: "post",
			url: "/member/join/existEmail",
			dataType: "json",
			data: { "userEmail": userEmail},
			success: function(res) {
				// 중복된 값이면 에러 출력
				if(res==true){
					$("#emailOverError").show();
					$("#btnEdit").prop('disabled',true);
					$("#btnEdit").css("background-color","#aaa");
				}
			}
		});	
		
	});
	
});
