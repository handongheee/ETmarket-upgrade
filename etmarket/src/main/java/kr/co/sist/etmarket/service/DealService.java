package kr.co.sist.etmarket.service;

import kr.co.sist.etmarket.dao.DealDao;
import kr.co.sist.etmarket.dao.UserDao;
import kr.co.sist.etmarket.entity.Deal;
import kr.co.sist.etmarket.entity.Item;
import kr.co.sist.etmarket.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DealService {

    @Autowired
    private DealDao dealDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    public Optional<Deal> findDealById(Long dealId) {
        return dealDao.findById(dealId);
    }

    @Autowired
    public DealService(DealDao dealDao, UserDao userDao) {
        this.dealDao = dealDao;
        this.userDao = userDao;
    }

    public List<Deal> getSellHistory(Long userId) {
        User user = userDao.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 ID입니다."));
        return dealDao.findAllBySellerOrderByDealDateDesc(user);
    }

    public List<Deal> getBuyHistory(Long userId) {
        User user = userDao.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 ID입니다."));
        return dealDao.findAllByBuyerOrderByDealDateDesc(user);
    }

    public List<User> getChatParticipantsByItemId(Long itemId) {
        return chatRoomService.getChatParticipantNamesByItemId(itemId);
    }

    @Transactional
    public void completeDeal(Long itemId, Long buyerId) {
        Item item = itemService.findItemById(itemId);
        User buyer = userService.findByUserId(buyerId);
        User seller = userService.findByUserId(chatRoomService.getSellerIdByItemId(itemId));

        // 선택된 구매자 정보를 이용하여 거래내역에 추가
        Deal deal = new Deal();
        deal.setItem(item);
        deal.setItemPrice(item.getItemPrice());
        deal.setBuyer(buyer);
        deal.setSeller(item.getUser());
        deal.setDealDate(Timestamp.valueOf(LocalDateTime.now()));
        deal.setDealMethod("직거래");
        deal.setRatingLeft("N,N"); // 판매자,구매자 평점 남김 여부
        dealDao.save(deal);
    }
    }
