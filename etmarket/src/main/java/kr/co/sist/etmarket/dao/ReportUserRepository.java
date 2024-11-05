package kr.co.sist.etmarket.dao;

import kr.co.sist.etmarket.entity.ReportUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportUserRepository extends JpaRepository<ReportUser, Long> {
    // 최근 생성된 사용자 신고 상위 5개 조회
    List<ReportUser> findTop5ByOrderByCreateDateDesc();

    // 신고된 사용자를 참조하는 모든 레코드 삭제
    //void deleteByReported_UserId(Long reportedUserId);

    // 신고한 사용자를 참조하는 모든 레코드 삭제
    //void deleteByReporter_UserId(Long reporterUserId);


}
