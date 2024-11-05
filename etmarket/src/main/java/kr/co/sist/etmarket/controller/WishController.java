package kr.co.sist.etmarket.controller;

import kr.co.sist.etmarket.dto.WishRequest;
import kr.co.sist.etmarket.service.ItemLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class WishController {

    private final ItemLikeService itemLikeService;

    @PostMapping("/toggle-wish")
    public ResponseEntity<Map<String, Object>> toggleWish(@RequestBody WishRequest wishRequest) {
        Long itemId = wishRequest.getItemId();
        Long userId = wishRequest.getUserId();

        Map<String, Object> response = itemLikeService.toggleWish(itemId, userId);

        return ResponseEntity.ok(response);
    }

}



