package kr.co.sist.etmarket.controller;

import kr.co.sist.etmarket.dto.ItemUpDto;
import kr.co.sist.etmarket.service.ItemUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/items")
public class ItemUpController {

    private final ItemUpService itemUpService;

    @Autowired
    public ItemUpController(ItemUpService itemUpService) {
        this.itemUpService = itemUpService;
    }

    @PostMapping("/up")
    public ResponseEntity<Map<String, Object>> upItem(@RequestBody ItemUpDto itemUpDto) {
        try {
            itemUpService.upItem(itemUpDto);
            int remainingUpCount = itemUpService.getRemainingUpCount(itemUpDto.getUserId());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Item up successful");
            response.put("remainingUpCount", remainingUpCount);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/up-count/{itemId}")
    public ResponseEntity<Integer> getUpCountForItem(@PathVariable Long itemId) {
        try {
            int upCount = itemUpService.getItemUpCount(itemId);
            return ResponseEntity.ok(upCount);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0);
        }
    }
}
