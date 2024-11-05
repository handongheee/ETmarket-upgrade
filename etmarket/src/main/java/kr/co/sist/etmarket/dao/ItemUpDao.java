package kr.co.sist.etmarket.dao;

import kr.co.sist.etmarket.entity.ItemUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ItemUpDao extends JpaRepository<ItemUp, Long> {
    // 오늘의 끌어올리기 액션 수를 카운트하는 메서드
    @Query("SELECT COUNT(i) FROM ItemUp i WHERE i.user.userId = :userId AND i.itemUpDate >= :startOfDay AND i.itemUpDate <= :endOfDay")
    int countTodayUpActions(@Param("userId") Long userId, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    // 오늘 사용자가 특정 상품을 이미 끌어올렸는지 확인하는 메서드
    @Query("SELECT COUNT(i) > 0 FROM ItemUp i WHERE i.item.itemId = :itemId AND i.user.userId = :userId AND i.itemUpDate >= :startOfDay AND i.itemUpDate <= :endOfDay")
    boolean existsByItemAndUserAndDate(@Param("itemId") Long itemId, @Param("userId") Long userId, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    // 특정 아이템 ID에 대한 레코드를 찾는 메서드
    @Query("SELECT u FROM ItemUp u WHERE u.item.itemId = :itemId")
    Optional<ItemUp> findByItem_ItemId(@Param("itemId") Long itemId);
}
