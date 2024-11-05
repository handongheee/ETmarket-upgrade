package kr.co.sist.etmarket.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MailService {

	private final JavaMailSender javaMailSender;

	// 이메일 발송 
	public void sendEmail(String userEmail, String title, String code) {
		
		SimpleMailMessage emailForm = createEmailForm(userEmail, title, code);
		//System.out.println("MailService에서 이메일 발송 sendEmail 메서드 실행");
		try {
			javaMailSender.send(emailForm);
			System.out.println(emailForm+" 성공한 이메일 폼");
			log.info("이메일 발송 성공!!");
		} catch (RuntimeException e) {
			System.out.println(emailForm+" 실패한 이메일 폼");
			log.info("MailService.sendEmail exception occur toEmail: {}, " + "title: {}, code: {}", userEmail, title,
					code);
			log.info("이메일 발송 실패,,");
			//throw new BusinessLogicException(ExceptionCode.UNABLE_TO_SEND_EMAIL);
		}
	}

	// 보낼 이메일 데이터 세팅
	public SimpleMailMessage createEmailForm(String userEmail, String title, String code) {
		System.out.println("MailService에서 발신할 이메일 세팅 - createEmailForm 메서드 실행");
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(userEmail);
		message.setSubject(title);
		message.setText(code);

		return message;
	}

}
