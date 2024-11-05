package kr.co.sist.etmarket.service;


import kr.co.sist.etmarket.dao.ItemCheckRepository;
import kr.co.sist.etmarket.dao.ItemDetailRepository;
import kr.co.sist.etmarket.dao.ItemLikeRepository;
import kr.co.sist.etmarket.dto.DetailTagDto;
import kr.co.sist.etmarket.dto.ItemDetailDto;
import kr.co.sist.etmarket.dto.ItemDetailImgDto;
import kr.co.sist.etmarket.dto.SimilarItemDto;
import kr.co.sist.etmarket.entity.Item;
import kr.co.sist.etmarket.entity.ItemImg;
import kr.co.sist.etmarket.entity.ItemTag;
import kr.co.sist.etmarket.etenum.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemDetailService {

    private final ItemDetailRepository itemDetailRepository;
    private final ItemLikeRepository itemLikeRepository;
    private final ItemCheckRepository itemCheckRepository;

    @Autowired
    public ItemDetailService(ItemDetailRepository itemDetailRepository, ItemLikeRepository itemLikeRepository,
                             ItemCheckRepository itemCheckRepository) {
        this.itemDetailRepository = itemDetailRepository;
        this.itemLikeRepository = itemLikeRepository;
        this.itemCheckRepository = itemCheckRepository;
    }

    // 상품 상세정보
    public ItemDetailDto getItemDetail(Long itemId) {

        Item item = itemDetailRepository.findById(itemId).orElseThrow(() -> new NoSuchElementException("id값에 해당하는 상품이 없습니다"));

        ItemDetailDto itemDetailDto = new ItemDetailDto();
        DecimalFormat formatter = new DecimalFormat("#,###");

        itemDetailDto.setItemId(item.getItemId());
        itemDetailDto.setSellerId(item.getUser().getUserId());
        itemDetailDto.setCategoryName(item.getCategoryName());
        itemDetailDto.setItemTitle(item.getItemTitle());
        itemDetailDto.setItemContent(item.getItemContent());
        itemDetailDto.setItemPrice(formatter.format(item.getItemPrice()));
        itemDetailDto.setWishCount(itemLikeRepository.countByItemId(item.getItemId()));
        itemDetailDto.setViewCount(itemCheckRepository.countByItemId(item.getItemId()));
        itemDetailDto.setItemStatus(item.getItemStatus());
        itemDetailDto.setDealStatus(item.getDealStatus());
        itemDetailDto.setItemHidden(item.getItemHidden());
        itemDetailDto.setDealHow(item.getDealHow());
        itemDetailDto.setItemAddress(item.getItemAddress());
        itemDetailDto.setDeliveryStatus(item.getDeliveryStatus());
        itemDetailDto.setItemDeliveryPrice(item.getItemDeliveryPrice());
        itemDetailDto.setItemResistDate(item.getItemResistDate());
        itemDetailDto.setItemUpdateDate(item.getItemUpdateDate());
        itemDetailDto.setDetailTagDtoList(item.getItemTags().stream()
                .map(this::convertTagToDto)
                .collect(Collectors.toList()));
        itemDetailDto.setItemImgDtoList(item.getItemImgs().stream()
                .map(this::convertImageToDto)
                .collect(Collectors.toList()));

        return itemDetailDto;

    }

    // 비슷한 상품 2개
    public List<SimilarItemDto> getSimilarItems(Long itemId, CategoryName categoryName) {

        List<SimilarItemDto> similarItemDtoList = new ArrayList<>();
        DecimalFormat formatter = new DecimalFormat("#,###");


        Pageable pageable = PageRequest.of(0, 2);
        List<Item> itemsByCategory = itemDetailRepository.findRandomItemsByCategory(categoryName, itemId,pageable);

        // 상품이 2개 미만인 경우 추가로 조회하여 채움
        if (itemsByCategory.size() < 2) {
            List<Item> additionalItems = itemDetailRepository.findRandomItems(itemId, PageRequest.of(0, 2 - itemsByCategory.size()));
            itemsByCategory.addAll(additionalItems);
        }
        for (Item item : itemsByCategory) {
            SimilarItemDto similarItemDto = new SimilarItemDto();

            similarItemDto.setItemId(item.getItemId());
            similarItemDto.setItemTitle(item.getItemTitle());
            similarItemDto.setItemPrice(formatter.format(item.getItemPrice()));
            similarItemDto.setImgUrl(item.getItemImgs().get(0).getItemImg());
            similarItemDto.setSellerId(item.getUser().getUserId());
            similarItemDto.setSellerName(item.getUser().getUserName());

            similarItemDtoList.add(similarItemDto);
        }

        return similarItemDtoList;
    }

    private DetailTagDto convertTagToDto(ItemTag itemTag) {

        return new DetailTagDto(itemTag.getItemTag());
    }

    private ItemDetailImgDto convertImageToDto(ItemImg itemImg) {

        return new ItemDetailImgDto(itemImg.getItemImgId(), itemImg.getItemImg(), itemImg.getResistDate(), itemImg.getUpdateDate());
    }


}
