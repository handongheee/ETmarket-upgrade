package kr.co.sist.etmarket.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.etmarket.dto.ChatRoomCountDto;
import kr.co.sist.etmarket.dto.ItemImgDto;
import kr.co.sist.etmarket.dto.ItemLikeDto;
import kr.co.sist.etmarket.entity.Item;
import kr.co.sist.etmarket.etenum.DealStatus;
import kr.co.sist.etmarket.etenum.ItemHidden;
import kr.co.sist.etmarket.service.ChatRoomService;
import kr.co.sist.etmarket.service.ItemImgService;
import kr.co.sist.etmarket.service.ItemLikeService;
import kr.co.sist.etmarket.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class DealManageController {

    private final ItemService itemService;
    private final ItemLikeService itemLikeService;
    private final ItemImgService itemImgService;
    private final ChatRoomService chatRoomService;

    @GetMapping("/deal/manage")
    public String list(Model model,
                       @PageableDefault(page = 0, size = 10, sort = "itemId", direction = Sort.Direction.DESC)
                       Pageable pageable,
                       String keyword,
                       @RequestParam(required = false) DealStatus dealStatus,
                       @RequestParam(required = false) ItemHidden hidden,
                       HttpSession session) {

        Long userId = (Long) session.getAttribute("myUserId");
        if (userId == null) {
            return "redirect:/login"; // 로그인 페이지로 이동
        }
        model.addAttribute("userId", userId);

        Page<Item> list;

        if (hidden != null) {
            if (keyword != null) {
                list = itemService.hiddenSearchList(userId, keyword, pageable);
            } else {
                list = itemService.hiddenList(userId, pageable);
            }
        } else if (dealStatus != null) {
            if (keyword != null) {
                list = itemService.itemSearchAndStatusList(userId, keyword, dealStatus, pageable);
            } else {
                list = itemService.itemStatusList(userId, dealStatus, pageable);
            }
        } else {
            if (keyword != null) {
                list = itemService.itemSearchList(userId, keyword, pageable);
            } else {
                list = itemService.findByUserIdAllItem(userId, pageable);
            }
        }




        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        List<ItemImgDto> itemImgList = list.getContent().stream()
                .map(item -> Optional.ofNullable(itemImgService.getFirstImageByItemId(item.getItemId()))
                        .map(itemImg -> new ItemImgDto(itemImg.getItemImgId(), itemImg.getItemImg()))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 상품별 찜 개수
        Map<Long, Long> likesByItemId = itemLikeService.countLikesByItemId().stream()
                .collect(Collectors.toMap(ItemLikeDto::getItemId, ItemLikeDto::getLikeCount, (existing, replacement) -> existing));

        // 상품별 채팅방 개수
        Map<Long, Long> chatRoomsByItemId = chatRoomService.getChatRoomCountByItemId().stream()
                .collect(Collectors.toMap(ChatRoomCountDto::getItemId, ChatRoomCountDto::getChatRoomCount, (existing, replacement) -> existing));



        model.addAttribute("list", list);
        model.addAttribute("itemImgList", itemImgList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("dealStatus", dealStatus);
        model.addAttribute("likesByItemId", likesByItemId);
        model.addAttribute("chatRoomsByItemId", chatRoomsByItemId);
        model.addAttribute("itemHidden", hidden);

        return "myPage/dealManage";
    }

    @PostMapping("/updateHiddenStatus")
    public ResponseEntity<Map<String, String>> updateHiddenStatus(@RequestParam Long itemId,
                                                                  @RequestParam ItemHidden hidden,
                                                                  HttpSession session) {

        Long userId = (Long) session.getAttribute("myUserId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "로그인이 필요합니다."));
        }

        boolean success = itemService.updateHiddenStatus(itemId, hidden);
        if (success) {
            String message = (hidden == ItemHidden.숨김) ? "게시물을 숨겼습니다." : "게시물이 다시 보입니다.";
            return ResponseEntity.ok(Collections.singletonMap("message", message));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "상태 업데이트에 실패했습니다."));
        }
    }

}
