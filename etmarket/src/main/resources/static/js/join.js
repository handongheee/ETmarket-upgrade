/**
 * 
 */

$(function() {
	// 아이디 유효성 체크
	$("#userLoginId").blur(function() {
		var userLoginId=$(this).val();
		//alert(userLoginId);
		
		// 원래대로
		$("#idBlankError").hide();
		$("#idOverError").hide();
		$("#idRuleError").hide();
		$("#btnJoin").prop('disabled',false);
		$("#btnJoin").css("background-color","#4eb006");
		
		// 아이디 공백 체크
	    if (userLoginId == "") {
	        $("#idBlankError").show();
	        $("#btnJoin").prop('disabled', true);
	        $("#btnJoin").css("background-color", "#aaa");
	        return; // 공백이면 더 이상 진행하지 않음
	    }
		
		// 아이디 중복 체크
		$.ajax({
			type: "post",
			url: "/member/join/existLoginId",
			dataType: "json",
			data: { "userLoginId": userLoginId},
			success: function(res) {
				// 중복된 값이면 에러 출력
				if(res==true){
					$("#idOverError").show();
					
					// 회원가입 차단
					$("#btnJoin").prop('disabled',true);
					$("#btnJoin").css("background-color","#aaa");
				}
			}
		});
		
		// 아이디 양식
		let loginIdRule = /^(?=.*[0-9])(?=.*[a-zA-Z]).{6,12}$/;
		
		if(!loginIdRule.test(userLoginId)){
			$("#idRuleError").show();
			
			// 회원가입 차단
			$("#btnJoin").prop('disabled',true);
			$("#btnJoin").css("background-color","#aaa");
		} 
			
	});
	
	// 닉네임 유효성 체크
	$("#userName").blur(function() {
		var userName=$(this).val();
		
		// 원래대로
		$("#nameOverError").hide();
		$("#nameBlankError").hide();
		
		// 닉네임 공백 체크
	    if (userName == "") {
	        $("#nameBlankError").show();
	        $("#btnJoin").prop('disabled', true);
	        $("#btnJoin").css("background-color", "#aaa");
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
		$("#btnJoin").prop('disabled',false);
		$("#btnJoin").css("background-color","#4eb006");
		
		// 비밀번호 공백 체크
	    if (userPassword == "") {
	        $("#passwordBlankError").show();
	        $("#btnJoin").prop('disabled', true);
	        $("#btnJoin").css("background-color", "#aaa");
	        return; // 공백이면 더 이상 진행하지 않음
	    }
		
		// 비밀번호 양식
		let passwordRule = /^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*+=-_]).{8,16}$/;
		
		if(!passwordRule.test(userPassword)){
			$("#passwordRuleError").show();
			
			// 회원가입 차단
			$("#btnJoin").prop('disabled',true);
			$("#btnJoin").css("background-color","#aaa");
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
	        $("#btnJoin").prop('disabled', true);
	        $("#btnJoin").css("background-color", "#aaa");
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
		$("#btnJoin").prop('disabled',false);
		$("#btnJoin").css("background-color","#4eb006");
		
		// 전화번호 공백 체크
	    if (userPhone == "") {
	        $("#phoneBlankError").show();
	        $("#btnJoin").prop('disabled', true);
	        $("#btnJoin").css("background-color", "#aaa");
	        return; // 공백이면 더 이상 진행하지 않음
	    }
	    
		// 전화번호 양식
		let phoneRule = /^01(0|1|[6-9])[0-9]{3,4}[0-9]{4}$/;
		
		if(!phoneRule.test(userPhone)){
			$("#phoneRuleError").show();
			
			// 회원가입 차단
			$("#btnJoin").prop('disabled',true);
			$("#btnJoin").css("background-color","#aaa");
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
		$("#btnJoin").prop('disabled',false);
		$("#btnJoin").css("background-color","#4eb006");
		
		// 이메일 공백 체크
	    if (userEmail == "") {
	        $("#emailBlankError").show();
	        $("#btnJoin").prop('disabled', true);
	        $("#btnJoin").css("background-color", "#aaa");
	        return; // 공백이면 더 이상 진행하지 않음
	    }
		
		// 이메일 양식 체크
		let emailRule = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
		
		if(!emailRule.test(userEmail)){
			$("#emailRuleError").show();
			
			// 회원가입 차단
			$("#btnJoin").prop('disabled',true);
			$("#btnJoin").css("background-color","#aaa");
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
					$("#btnJoin").prop('disabled',true);
					$("#btnJoin").css("background-color", "#aaa");
				}
			}
		});	
		
	});
	
});
