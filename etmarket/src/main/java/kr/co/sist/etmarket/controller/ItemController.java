package kr.co.sist.etmarket.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.etmarket.dto.ItemDto;
import kr.co.sist.etmarket.dto.ItemImgDto;
import kr.co.sist.etmarket.dto.ItemTagDto;
import kr.co.sist.etmarket.entity.Item;
import kr.co.sist.etmarket.etenum.CategoryName;
import kr.co.sist.etmarket.service.ItemImgService;
import kr.co.sist.etmarket.service.ItemService;
import kr.co.sist.etmarket.service.ItemTagService;
import kr.co.sist.etmarket.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ItemTagService itemTagService;
    private final ItemImgService itemImgService;

    // insertForm 이동
    @GetMapping("/item/insertForm")
    public String insertForm(HttpSession session, Model model){
        Long userId = (Long) session.getAttribute("myUserId");

        if (userId != null) {
            model.addAttribute("userId", userId);

            return "item/itemInsertForm";
        } else {
            model.addAttribute("approach","insert");

            return "item/wrongApproach";
        }
    }

    // Item, ItemTag, ItemImg DB Insert, S3 Image Upload
    @PostMapping("/item/insert")
    public String insert(@ModelAttribute ItemDto itemDto,
                         @ModelAttribute ItemTagDto itemTagDto,
                         @RequestParam ArrayList<MultipartFile> itemImgUpload) {

        Item item = itemService.insertItem(itemDto);

        if (!itemTagDto.getItemTagText().isBlank()) {
            itemTagService.insertItemTag(itemTagDto, item);
        }

        itemImgService.insertItemImg(itemImgUpload, item);

        return "redirect:/";
    }

    // updateForm 이동
    @GetMapping("/item/updateForm")
    public String updateForm(@RequestParam("itemId") Long itemId, HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("myUserId");
        String userName = (String) session.getAttribute("myUserName");

        ItemDto itemDto = itemService.getDataItem(itemId);

        if ((userId != null && userId.equals(itemDto.getUserId())) || (userName != null && userName.equals("admin"))) {
            List<ItemImgDto> itemImgDtos = itemImgService.getItemImgDataByItemId(itemId);
            String itemTags = itemTagService.getItemTagsByItemId(itemId);

            model.addAttribute("itemDto", itemDto);
            model.addAttribute("itemImgDtos", itemImgDtos);
            model.addAttribute("itemImgCount", itemImgDtos.size());
            model.addAttribute("itemTags", itemTags);

            return "item/itemUpdateForm";
        } else {
            model.addAttribute("approach","update");

            return "item/wrongApproach";
        }
    }

    // Item, ItemTag, ItemImg DB Update, S3 Image Delete or Upload
    @PostMapping("/item/update")
    public String update(@ModelAttribute ItemDto itemDto,
                         @ModelAttribute ItemTagDto itemTagDto,
                         @ModelAttribute ItemImgDto itemImgDto,
                         @RequestParam ArrayList<MultipartFile> itemImgUpload) {
        Item item = itemService.updateItem(itemDto);

        if (!itemTagDto.getItemTagText().isBlank()) {
            itemTagService.deleteItemTag(itemDto.getItemId());

            itemTagService.insertItemTag(itemTagDto, item);
        } else {
            itemTagService.deleteItemTag(itemDto.getItemId());
        }

        int itemImgCount = itemImgService.getItemImgDataByItemId(itemDto.getItemId()).size();
        itemImgService.updateItemImg(itemImgUpload, itemImgDto, itemImgCount, item);

        return "redirect:/";
    }

    @GetMapping("/search/category")
    public String getCategoryList(@RequestParam("category") String category,
                                  @RequestParam(defaultValue = "0", name = "page") int page,
                                  @RequestParam(defaultValue = "10", name = "size") int size,
                                  Model model) {

        Page<ItemDto> categorys = itemService.getCategoryList(CategoryName.valueOf(category), page, size);

        int totalPages = categorys.getTotalPages(); // 총 페이지 수
        int currentPage = page; // 현재 페이지 (0부터 시작)
        int maxDisplayedPages = 5; // 한 번에 표시할 최대 페이지 번호 수

        // 시작 페이지 계산: 현재 페이지에서 표시할 페이지 수의 절반을 뺀 값, 최소 0
        int startPage = Math.max(0, currentPage - maxDisplayedPages / 2);
        // 끝 페이지 계산: 시작 페이지 + 표시할 최대 페이지 수 - 1, 최대 총 페이지 - 1
        int endPage = Math.min(totalPages - 1, startPage + maxDisplayedPages - 1);

        // 끝 페이지와 시작 페이지 사이의 간격이 최대 표시 페이지 수보다 작으면
        if (endPage - startPage < maxDisplayedPages - 1) {
            // 시작 페이지 재조정: 끝 페이지 - 최대 표시 페이지 수 + 1, 최소 0
            startPage = Math.max(0, endPage - maxDisplayedPages + 1);
        }

        // 모델에 필요한 속성 추가
        model.addAttribute("category", category); // 검색된 내용
        model.addAttribute("categorys", categorys); // 검색된 아이템들
        model.addAttribute("currentPage", currentPage); // 현재 페이지
        model.addAttribute("totalPages", totalPages); // 총 페이지 수
        model.addAttribute("startPage", startPage); // 표시할 시작 페이지
        model.addAttribute("endPage", endPage); // 표시할 끝 페이지
        model.addAttribute("size", size); // 페이지당 아이템 수

        return "search/category";
    }

    // Item DB Delete, S3 Image Delete
    @GetMapping("/item/delete")
    public String delete(@RequestParam("itemId") Long itemId, HttpSession session, Model model){
        Long userId = (Long) session.getAttribute("myUserId");
        String userName = (String) session.getAttribute("myUserName");

        ItemDto itemDto = itemService.getDataItem(itemId);

        if ((userId != null && userId.equals(itemDto.getUserId())) || (userName != null && userName.equals("admin"))) {
            itemImgService.deleteItemImgS3(itemId);

            itemService.deleteItem(itemId);

            return "redirect:/";
        } else {
            model.addAttribute("approach","delete");

            return "item/wrongApproach";
        }
    }

}
