package kr.co.sist.etmarket.service;

import kr.co.sist.etmarket.dto.ItemDto;
import kr.co.sist.etmarket.dao.UserDao;
import kr.co.sist.etmarket.dao.UserSearchDao;
import kr.co.sist.etmarket.dto.UserDto;
import kr.co.sist.etmarket.dto.UserSearchDto;
import kr.co.sist.etmarket.entity.*;
import kr.co.sist.etmarket.etenum.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserSearchService {

    @Autowired
    private UserSearchDao userSearchDao;
    @Autowired
    private UserDao userDao;

    // UserSearch insert
    public void insertContent(UserSearchDto userSearchDto) {
        Optional<User> optionalUser = userDao.findById(userSearchDto.getUserId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            List<UserSearch> userSearchList = userSearchDao.findByContent(userSearchDto.getContent());
            UserSearch userSearch;
            if (userSearchList.isEmpty()) {
                userSearch = new UserSearch();
                userSearch.setUser(user);
                userSearch.setContent(userSearchDto.getContent());
            }else {
                userSearch = userSearchList.get(0);
            }
            userSearchDao.save(userSearch);
        }else {
            System.out.println("insert 실패");
        }

    }

    // 인기 검색어 8개 출력
    public List<UserSearchDto> getTop8PopularContent() {
        List<Object[]> result = userSearchDao.findTop8PopularContent();
        return result.stream()
                .map(userSearch -> new UserSearchDto((String) userSearch[0]))
                .collect(Collectors.toList());
    }

    // 유저에 대한 최근 검색 날짜 desc 8개 출력
    public List<UserSearchDto> getTop8SearchContent(UserSearchDto userSearchDto) {
        User user = userDao.findById(userSearchDto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found")); // orElseThrow : 존재하지 않으면 예외처리
        List<UserSearch> result = userSearchDao.findTop8ByUserOrderByUpdateDateDesc(user);
        return result.stream()
                .map(userSearch -> new UserSearchDto(userSearchDto.getUserId(), userSearch.getContent(),userSearch.getUpdateDate()))
                .collect(Collectors.toList());
    }

    // 최근 검색어를 클릭 시 상단으로 이동시키기 위함
    public void getUserSearchUpdate(UserDto user, String content) {
        User findUser = userDao.findById(user.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        System.out.println("findUser = " + findUser);
        Optional<UserSearch> existing = userSearchDao.findByUserAndContent(findUser, content);
        if (existing.isPresent()) { // 기존의 UserSearch 객체가 존재하는 경우
            UserSearch userSearch = existing.get(); // 기존 객체를 가져옴
            // resistDate를 현재 시간으로 업데이트
            userSearch.setUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
            // 업데이트된 객체를 데이터베이스에 저장
            userSearchDao.save(userSearch);
        }
    }
    
    // 검색어에 대한 상품 리스트
//    List<Object[]> findItemsByContentAndItemTitle(@Param("content") String content);
    public Page<ItemDto> getItemTitle(String content, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> items = userSearchDao.findItemsByContentAndItemTitle(content, pageable);
//        Page<ItemDto> itemDtos = items.map(this::createItemDto);

        /*for (Item item : items) {
            List<ItemTag> itemTags = item.getItemTags();
            List<ItemImg> itemImgs = item.getItemImgs();
            List<ItemCheck> itemChecks = item.getItemChecks();
            List<ItemLike> itemLikes = item.getItemLikes();

            System.out.println("태그갯수: " + itemTags.size());
            System.out.println("이미지갯수: " + itemImgs.size());
            System.out.println("체크갯수: " + itemChecks.size());
            System.out.println("좋아요갯수: " + itemLikes.size());
        }*/

        return items.map(this::createItemDto);
    }

    private ItemDto createItemDto(Item item) {
        return new ItemDto(
                item.getItemId(),
                item.getItemTitle(),
                item.getItemContent(),
                item.getItemPrice(),
                item.getItemAddress(),
                item.getItemStatus(),
                item.getDealStatus(),
                item.getDealHow(),
                item.getDeliveryStatus(),
                item.getItemDeliveryPrice(),
                item.getPriceStatus(),
                item.getCategoryName(),
                item.getItemCount(),
                item.getItemHidden(),
                item.getItemResistDate(),
                item.getItemUpdateDate(),
                item.getUserSearch() != null ? item.getUserSearch().getUserSearchId() : null,
                item.getItemImgs(), // Include the item images list
                item.getItemTags(),
                item.getItemChecks().size(),
                item.getItemLikes().size(),
                item.getUser().getUserId()
        );
    }

    public List<UserSearch> getContent(String content) {
        return userSearchDao.findByContent(content);
    }

    public void deleteContent(String content) {
        List<UserSearch> results = userSearchDao.findByContent(content);
        for (UserSearch entity : results) {
            userSearchDao.delete(entity);
        }

    }

    public void deleteAll(UserSearchDto userSearchDto){
        if(userSearchDao.existsById(userSearchDto.getUserId())){
            userSearchDao.deleteAll();
        } else {
            System.out.println("존재하지 않은 id임");
        }
    }


}
