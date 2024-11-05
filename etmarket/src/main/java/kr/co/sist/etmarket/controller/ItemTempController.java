package kr.co.sist.etmarket.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.etmarket.KeyValueMapper;
import kr.co.sist.etmarket.dto.ItemDetailDto;
import kr.co.sist.etmarket.dto.SimpleSellerDto;
import kr.co.sist.etmarket.dto.SimilarItemDto;
import kr.co.sist.etmarket.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ItemTempController {

    private final ItemDetailService itemDetailService;
    private final SellerDetailService sellerDetailService;
    private final CommonService commonService;
    private final ItemCheckService itemCheckService;
    private final ItemLikeService itemLikeService;

    @Autowired
    public ItemTempController(ItemDetailService itemDetailService, SellerDetailService sellerDetailService,
                              CommonService commonService, ItemCheckService itemCheckService,
                              ItemLikeService itemLikeService) {

        this.itemDetailService = itemDetailService;
        this.sellerDetailService = sellerDetailService;
        this.commonService = commonService;
        this.itemCheckService = itemCheckService;
        this.itemLikeService = itemLikeService;
    }

    @GetMapping("/items/{itemId}")
    public String itemDetail(@PathVariable("itemId") Long itemId, Model model, HttpSession httpSession) {

        Long uid = (Long) httpSession.getAttribute("myUserId");
        if (uid != null) {
            itemCheckService.addItemCheck(uid,itemId); // 조회수 처리
        }

        ItemDetailDto itemDetailDto = itemDetailService.getItemDetail(itemId);
        String updateTime = commonService.convertTime(itemDetailDto.getItemUpdateDate());
        String categoryName = KeyValueMapper.getValue(itemDetailDto.getCategoryName());
        String itemStatus = KeyValueMapper.getValue(itemDetailDto.getItemStatus());
        List<SimilarItemDto> similarItemDtoList = itemDetailService.getSimilarItems(itemDetailDto.getItemId(),itemDetailDto.getCategoryName());

        SimpleSellerDto simpleSellerDto = sellerDetailService.getSimpleSellerInfo(itemDetailDto.getSellerId(),itemId);
        boolean isWishItem = itemLikeService.isWishItem(uid, itemId);

        model.addAttribute("itemDetailDto", itemDetailDto);
        model.addAttribute("updateTime", updateTime);
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("itemStatus", itemStatus);
        model.addAttribute("similarItemList", similarItemDtoList);
        model.addAttribute("sellerDto", simpleSellerDto);
        model.addAttribute("uid", uid);
        model.addAttribute("isWishItem", isWishItem);
        model.addAttribute("itemId", itemId);

        return "item/item_detail";
    }

}
