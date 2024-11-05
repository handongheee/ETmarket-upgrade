package kr.co.sist.etmarket.dao;

import kr.co.sist.etmarket.entity.Item;
import kr.co.sist.etmarket.etenum.CategoryName;
import kr.co.sist.etmarket.etenum.DealStatus;
import kr.co.sist.etmarket.etenum.ItemHidden;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemDetailRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i where i.categoryName = :categoryName and i.itemId <> :itemId order by rand()")
    List<Item> findRandomItemsByCategory(@Param("categoryName") CategoryName categoryName, @Param("itemId") Long itemId, Pageable pageable);

    @Query("select i from Item i where i.itemId <> :itemId order by rand()")
    List<Item> findRandomItems(@Param("itemId") Long itemId, Pageable pageable);

    int countByUser_UserIdAndItemHiddenAndDealStatus(Long userId, ItemHidden itemHidden, DealStatus dealStatus);

    @Query("select i from Item i where i.user.userId = :userId and i.dealStatus = :dealStatus " +
            "and i.itemHidden = :itemHidden and i.itemId <> :itemId order by rand()")
    List<Item> findRandomItemsByUserAndStatus(@Param("userId") Long userId, @Param("dealStatus") DealStatus dealStatus, @Param("itemHidden") ItemHidden itemHidden,
                                              @Param("itemId") Long itemId, Pageable pageable);

    int countByUser_UserIdAndItemHidden(Long userId, ItemHidden itemHidden);

    List<Item> findByUser_UserIdAndItemHiddenOrderByItemUpdateDateDesc(Long userId, ItemHidden itemHidden);

}
