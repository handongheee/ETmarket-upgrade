package kr.co.sist.etmarket.dao;

import kr.co.sist.etmarket.entity.Item;
import kr.co.sist.etmarket.etenum.CategoryName;
import kr.co.sist.etmarket.etenum.ItemStatus;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import kr.co.sist.etmarket.etenum.DealStatus;
import kr.co.sist.etmarket.etenum.ItemHidden;

@Repository
public interface ItemDao extends JpaRepository<Item, Long> {

   // 모든 아이템을 최신 업데이트 순으로 정렬하고 페이징 처리
//    @Query("SELECT i FROM Item i ORDER BY i.itemUpdateDate DESC")
//    Page<Item> findAllOrderByItemUpdateDateDesc(Pageable pageable);

    @Query("SELECT i FROM Item i ORDER BY i.itemUpdateDate DESC") // join을 하지 않고 item 엔티티를 직접 조회하기에 연관된 다른 엔티티들도 같이 조회 가능
    Slice<Item> findAllOrderByItemUpdateDateDesc(Pageable pageable);
  
  // itemId값에 따른 getData
    Item findByItemId(Long itemId);

    @Query("SELECT i FROM Item i " +
            "WHERE i.categoryName = :category " +
            "ORDER BY i.itemUpdateDate DESC")
    Page<Item> findItemsByCategoryName(@Param("category") CategoryName category, Pageable pageable);


    // itemId값에 따른 delete
    void deleteByItemId(Long itemId);

    Item findItemByItemId(Long itemId);

    @Query("SELECT i.user.userName FROM Item i WHERE i.itemId = :itemId")
    String findUserNameByItemId(@Param("itemId") Long itemId);
  
    @Query("SELECT COUNT(i) FROM Item i")
    long countAllTransactions();

    @Query("SELECT COUNT(i) FROM Item i WHERE i.dealStatus = '거래완료'")
    long countSuccessfulTransactions();

    // 사용자 ID를 기준으로 아이템 삭제
    //void deleteByUserId(Long userId);

    long countByItemStatus(ItemStatus status);



  /*마이페이지에서 사용*/

   /* 유저가 등록한 전체 상품 출력(숨김 상품 제외) & 숨긴 상품 출력 */
   Page<Item> findByUser_UserIdAndItemHidden(Long userId, ItemHidden hidden, Pageable pageable);

   /* 전체 & 숨김 탭에서 검색 */
   Page<Item> findByUser_UserIdAndItemTitleContainingAndItemHidden(Long userId, String keyword, ItemHidden hidden, Pageable pageable);

   /* 거래상태에 따라 출력(숨김 상품 제외) */
   Page<Item> findByUser_UserIdAndDealStatusAndItemHidden(Long userId, DealStatus dealStatus, ItemHidden hidden, Pageable pageable);

   /* 판매중,예약중,거래완료 탭에서 검색 */
   Page<Item> findByUser_UserIdAndItemTitleContainingAndDealStatusAndItemHidden(Long userId, String keyword, DealStatus dealStatus, ItemHidden hidden, Pageable pageable);

}
