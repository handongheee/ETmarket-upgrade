package kr.co.sist.etmarket.service;

import kr.co.sist.etmarket.dao.ItemDao;
import kr.co.sist.etmarket.dao.ItemUpDao;
import kr.co.sist.etmarket.dao.UserDao;
import kr.co.sist.etmarket.dto.ItemUpDto;
import kr.co.sist.etmarket.entity.Item;
import kr.co.sist.etmarket.entity.ItemUp;
import kr.co.sist.etmarket.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ItemUpService {

    private final ItemDao itemDao;
    private final ItemUpDao itemUpDao;
    private final UserDao userDao;

    @Autowired
    public ItemUpService(ItemDao itemDao, ItemUpDao itemUpDao, UserDao userDao) {
        this.itemDao = itemDao;
        this.itemUpDao = itemUpDao;
        this.userDao = userDao;
    }


    @Transactional
    public void upItem(ItemUpDto itemUpDto) {
        Item item = itemDao.findById(itemUpDto.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid item ID"));

        User user = userDao.findById(itemUpDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        // 사용자의 남은 끌어올리기 횟수를 감소시키고 반환
        int remainingUpCount = decreaseRemainingUpCount(itemUpDto.getUserId());

        if (remainingUpCount <= 0) {
            throw new IllegalArgumentException("오늘 더 이상 끌어올릴 수 없습니다.");
        }

        // 오늘 이미 이 상품을 끌어올렸는지 확인
        if (itemUpDao.existsByItemAndUserAndDate(itemUpDto.getItemId(), itemUpDto.getUserId(), startOfDay, endOfDay)) {
            throw new IllegalArgumentException("오늘 이미 이 상품을 끌어올렸습니다.");
        }


        // 특정 아이템 ID에 대한 기존 레코드를 찾기
        Optional<ItemUp> existingItemUp = itemUpDao.findByItem_ItemId(itemUpDto.getItemId());

        if (existingItemUp.isPresent()) {
            ItemUp itemUp = existingItemUp.get();
            itemUp.setItemUpCount(itemUp.getItemUpCount() + 1);
            itemUp.setItemUpDate(Timestamp.valueOf(LocalDateTime.now()));
            itemUpDao.save(itemUp);
        } else {
            ItemUp newItemUp = new ItemUp();
            newItemUp.setItem(item);
            newItemUp.setUser(user);
            newItemUp.setItemUpCount(1);
            newItemUp.setItemUpDate(Timestamp.valueOf(LocalDateTime.now()));

            itemUpDao.save(newItemUp);
        }
        item.setItemUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
        itemDao.save(item);
    }

    // 사용자의 남은 끌어올리기 횟수를 감소시키고 반환하는 메서드
    private int decreaseRemainingUpCount(Long userId) {
        int maxDailyUpCount = 10;
        int usedUpCount = itemUpDao.countTodayUpActions(userId, LocalDate.now().atStartOfDay(), LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999));
        int remainingUpCount = maxDailyUpCount - usedUpCount;

        return remainingUpCount;
    }

    // 사용자의 남은 끌어올리기 횟수를 조회하는 메서드
    public int getRemainingUpCount(Long userId) {
        int maxDailyUpCount = 10;
        int usedUpCount = itemUpDao.countTodayUpActions(userId, LocalDate.now().atStartOfDay(), LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999));
        return maxDailyUpCount - usedUpCount;
    }

    public int getItemUpCount(Long itemId) {
        Optional<ItemUp> itemUp = itemUpDao.findByItem_ItemId(itemId);
        return itemUp.map(ItemUp::getItemUpCount).orElse(0);
    }
}
