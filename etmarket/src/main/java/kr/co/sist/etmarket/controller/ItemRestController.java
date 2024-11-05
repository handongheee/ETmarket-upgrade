package kr.co.sist.etmarket.controller;

import kr.co.sist.etmarket.dto.ItemDto;
import kr.co.sist.etmarket.entity.Item;
import kr.co.sist.etmarket.etenum.CategoryName;
import kr.co.sist.etmarket.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ItemRestController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/items")
    public Slice<ItemDto> getAllItems(@RequestParam(defaultValue = "0", name = "page") int page,
                                      @RequestParam(defaultValue = "10", name = "size") int size) {
        Pageable pageable = PageRequest.of(page, size);
//        Slice<ItemDto> itemSlice = itemService.getItemSlice(pageable);
        return itemService.getItemSlice(pageable);
    }

    //    @GetMapping("/items")
//    public ResponseEntity<List<Item>> getItems(@RequestParam(defaultValue = "0") int page,
//                                               @RequestParam(defaultValue = "20") int size) {
//        Slice<Item> itemSlice = itemService.getItemSlice(page, size);
//        return ResponseEntity.ok()
//                .header("X-Has-Next", String.valueOf(itemSlice.hasNext()))
//                .body(itemSlice.getContent());
//    }
    @GetMapping("/api/search/category")
    public Page<ItemDto> findItemsByContentAndItemTitle(@RequestParam String category,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return itemService.getCategoryList(CategoryName.valueOf(category), page, size);
    }

    @GetMapping("api/item/detail")
    public ItemDto getItem(@RequestParam String itemId) {
        ItemDto dataItem = itemService.getDataItem(Long.valueOf(itemId));

        return dataItem;
    }
}
