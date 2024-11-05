package kr.co.sist.etmarket.dao;

import kr.co.sist.etmarket.entity.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Deal, Long> {

    @Query("select count(d) from Deal d where d.buyer.userId = :sellerId or d.seller.userId = :sellerId")
    int countBySellerId(@Param("sellerId") Long sellerId);

    @Query("select count(d) from Deal d where d.seller.userId = :sellerId")
    int countSellCountBySellerId(@Param("sellerId") Long sellerId);
}
