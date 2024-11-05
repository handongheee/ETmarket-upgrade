package kr.co.sist.etmarket.controller;

import kr.co.sist.etmarket.dto.ItemDto;
import kr.co.sist.etmarket.dto.UserDto;
import kr.co.sist.etmarket.entity.Item;
import kr.co.sist.etmarket.entity.User;
import kr.co.sist.etmarket.service.DealService;
import kr.co.sist.etmarket.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deal")
public class DealStatusController {

    private final ItemService itemService;
    private final DealService dealService;

    @PostMapping("/status/update")
    public ResponseEntity<?> updateDealStatus(@RequestBody ItemDto itemDto) {
        // 아이템 상태 업데이트
        itemService.updateDealStatus(itemDto);

        return ResponseEntity.ok().body(Map.of("success", true, "message", "Status updated successfully"));
    }

    @GetMapping("/{itemId}/chatParticipants")
    public ResponseEntity<List<UserDto>> getChatParticipants(@PathVariable Long itemId) {
        Item item = itemService.findItemById(itemId);
        List<User> users = dealService.getChatParticipantsByItemId(item.getItemId());

        List<UserDto> userInfoList = users.stream()
                .map(user -> new UserDto(user.getUserId(), user.getUserName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userInfoList);
    }


    @PostMapping("/{itemId}/complete")
    public ResponseEntity<String> completeDeal(@PathVariable Long itemId, @RequestBody Map<String, Long> requestBody) {
       /* String buyerId = requestBody.get("buyerId");
        Long buyerIdLong = Long.parseLong(buyerId);
        dealService.completeDeal(itemId, buyerIdLong);*/
        Long buyerId = requestBody.get("buyerId");
        dealService.completeDeal(itemId, buyerId);
        return ResponseEntity.ok("Deal completed for item " + itemId + " with buyer " + buyerId);
    }

}
