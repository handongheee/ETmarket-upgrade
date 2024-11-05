package kr.co.sist.etmarket.dao;

import kr.co.sist.etmarket.entity.Deal;
import kr.co.sist.etmarket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealDao extends JpaRepository<Deal, Long> {
    /* 판매자 ID로 판매 내역 조회 (deal_date 기준 최신순) */
    List<Deal> findAllBySellerOrderByDealDateDesc(User seller);
    /* 구매자 ID로 구매 내역 조회 (deal_date 기준 최신순) */
    List<Deal> findAllByBuyerOrderByDealDateDesc(User buyer);
}

