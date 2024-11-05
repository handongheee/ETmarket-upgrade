package kr.co.sist.etmarket.dao;

import kr.co.sist.etmarket.entity.ItemLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemLikeRepository extends JpaRepository<ItemLike, Long> {

    @Query("select count(il) from ItemLike il where il.item.itemId = :itemId ")
    int countByItemId(@Param("itemId")Long itemId);

    Optional<ItemLike> findByItem_ItemIdAndUser_UserId(Long itemId, Long userId);

    void deleteByItem_ItemIdAndUser_UserId(Long itemId, Long userId);

    /*마이페이지에서 사용*/
    @Query("SELECT il.item.itemId, COUNT(il.itemLikeId) AS likeCount FROM ItemLike il GROUP BY il.item.itemId")
    List<Object[]> countLikesByItemId();
    /* 찜목록 */
    @Query("SELECT i, img " +
            "FROM ItemLike il " +
            "JOIN il.item i " +
            "JOIN ItemImg img ON i.itemId = img.item.itemId " +
            "WHERE il.user.userId = :userId " +
            "AND img.itemImgId = (SELECT MIN(img2.itemImgId) FROM ItemImg img2 WHERE img2.item.itemId = i.itemId)")
    List<Object[]> findLikedItemsByUserId(@Param("userId") Long userId);

}

