package kr.co.sist.etmarket.dao;

import kr.co.sist.etmarket.entity.ReportProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportProductRepository extends JpaRepository<ReportProduct,Long> {
    // 최근 생성된 상품 신고 상위 5개 조회
    List<ReportProduct> findTop5ByOrderByCreateDateDesc();
    // 아이템 ID로 삭제
    void deleteByItem_ItemId(Long itemId);
}
