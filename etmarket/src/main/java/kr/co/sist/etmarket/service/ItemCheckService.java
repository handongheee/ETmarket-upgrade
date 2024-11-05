package kr.co.sist.etmarket.service;


import kr.co.sist.etmarket.dao.ItemCheckRepository;
import kr.co.sist.etmarket.dao.ItemDetailRepository;
import kr.co.sist.etmarket.dao.SellerDetailRepository;
import kr.co.sist.etmarket.entity.Item;
import kr.co.sist.etmarket.entity.ItemCheck;
import kr.co.sist.etmarket.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemCheckService {

    private final SellerDetailRepository sellerDetailRepository;
    private final ItemDetailRepository itemDetailRepository;
    private final ItemCheckRepository itemCheckRepository;


    public void addItemCheck(Long userId, Long itemId) {

        Optional<ItemCheck> originalItemCheck = itemCheckRepository.findByItem_ItemIdAndUser_UserId(itemId, userId);

        if (!originalItemCheck.isPresent()) {

            User user = sellerDetailRepository.findById(userId).orElseThrow(()-> new NoSuchElementException("id값에 해당하는 유저가 없습니다"));
            Item item = itemDetailRepository.findById(itemId).orElseThrow(() -> new NoSuchElementException("id값에 해당하는 상품이 없습니다"));

            ItemCheck itemCheck = new ItemCheck();

            itemCheck.setItem(item);
            itemCheck.setUser(user);

            itemCheckRepository.save(itemCheck);
        }
    }



}
