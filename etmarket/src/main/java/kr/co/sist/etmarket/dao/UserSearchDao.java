package kr.co.sist.etmarket.dao;


import kr.co.sist.etmarket.entity.Item;
import kr.co.sist.etmarket.entity.User;
import kr.co.sist.etmarket.entity.UserSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserSearchDao extends JpaRepository<UserSearch, Long> {

    // 유저에 대한 최근 검색 날짜 desc 8개 출력
    List<UserSearch> findTop8ByUserOrderByUpdateDateDesc(User user);

    // 검색한 유저 찾기
    List<UserSearch> findByUser(User user);

    // 유저에 대한 content 값이 존재하면 updateDate 수정 => 최근 검색어를 클릭 시 상단으로 이동시키기 위함
    Optional<UserSearch> findByUserAndContent(User user, String content);

    // 인기 검색어 8개 출력
    @Query(value = "SELECT content, COUNT(content) AS WORDCNT " +
            "FROM user_search " +
            "GROUP BY content " +
            "ORDER BY WORDCNT desc " +
            "LIMIT 8", nativeQuery = true)
    List<Object[]> findTop8PopularContent();

    // 검색어 하나 가져오기
    List<UserSearch> findByContent(String content);

    // 검색어에 맞는 상품 리스트 출력
//    @Query(value = "SELECT i.* FROM item i LEFT JOIN user_search us USING(user_search_id) WHERE i.item_title LIKE %:content%", nativeQuery = true)
    //------------------------ 잠시 LAZY 테스트
//    @Query("SELECT DISTINCT i FROM Item i LEFT JOIN FETCH i.itemImgs LEFT JOIN i.userSearch us " +
//            "WHERE i.itemTitle LIKE %:content% AND NOT (i.itemHidden = '숨김' OR i.dealStatus = '거래완료') " +
//            "ORDER BY i.itemUpdateDate DESC") =======> img랑 tag는 조회 시 같이 조회되는 것이 나을 거 같아 없앰
    @Query("SELECT DISTINCT i FROM Item i " +
            "WHERE i.itemTitle LIKE %:content% AND NOT (i.itemHidden = '숨김' OR i.dealStatus = '거래완료') " +
            "ORDER BY i.itemUpdateDate DESC")
    /*@Query("SELECT DISTINCT i FROM Item i " +
            "LEFT JOIN FETCH i.itemTags " +
            "LEFT JOIN FETCH i.itemImgs " +
            "LEFT JOIN FETCH i.itemChecks " +
            "LEFT JOIN FETCH i.itemLikes " +
            "WHERE i.itemTitle LIKE %:content% " +
            "AND NOT (i.itemHidden = '숨김' OR i.dealStatus = '거래완료') " +
            "ORDER BY i.itemUpdateDate DESC")*/
    Page<Item> findItemsByContentAndItemTitle(@Param("content") String content, Pageable pageable);



    Long findByUserSearchId(UserSearch search);

//    UserSearch findByContent(String content);
    @Query("SELECT u.userSearchId FROM UserSearch u WHERE u.content = :content")
    List<Long> findIdsByContent(@Param("content") String content);
}
