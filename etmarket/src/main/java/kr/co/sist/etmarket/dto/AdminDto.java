package kr.co.sist.etmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {
    private Long adminId; // 관리자 번호
    private String adminLoginId; // 관리자 ID
    private String adminPassword; // 관리자 비밀번호
    private String adminName; // 관리자 이름
    private String adminImage; // 관리자 사진
    private String adminCreateDate; // 계정 생성 시간
    private String adminStatus; // 활성상태
}
