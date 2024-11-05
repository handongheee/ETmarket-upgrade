package kr.co.sist.etmarket.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.etmarket.dto.ItemImgDto;
import kr.co.sist.etmarket.service.ItemImgService;
import kr.co.sist.etmarket.service.ItemLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ItemLikeController {

    private final ItemLikeService itemLikeService;
    private final ItemImgService itemImgService;

    @GetMapping("/item/like")
    public String getLikedItems(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("myUserId");
        if (userId == null) {
            return "redirect:/login"; // 로그인 페이지로 이동
        }
        model.addAttribute("userId", userId);

        // 사용자의 찜 목록을 가져옴
        List<Map<String, Object>> likedItem = itemLikeService.getLikedItems(userId);

        // 찜 목록의 아이템 이미지를 가져옴
        List<ItemImgDto> itemImgList = likedItem.stream()
                .map(item -> (Long) item.get("itemId"))
                .map(itemId -> Optional.ofNullable(itemImgService.getFirstImageByItemId(itemId))
                        .map(itemImg -> new ItemImgDto(itemImg.getItemImgId(), itemImg.getItemImg()))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        model.addAttribute("likedItem", likedItem);
        model.addAttribute("itemImgList", itemImgList);
        return "myPage/itemLike";
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> deleteItem(@PathVariable Long itemId,HttpSession session) {
        Long userId = (Long) session.getAttribute("myUserId");
        itemLikeService.deleteItemLike(itemId,userId);
        return ResponseEntity.ok().body("상품이 삭제되었습니다.");
    }
}
