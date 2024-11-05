package kr.co.sist.etmarket.controller;

import kr.co.sist.etmarket.dto.ItemDto;
import kr.co.sist.etmarket.entity.ItemTag;
import kr.co.sist.etmarket.etenum.CategoryName;
import kr.co.sist.etmarket.service.UserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class UserSearchController {

    @Autowired
    private UserSearchService userSearchService;


    @GetMapping("/search")
    public String findItemsByContentAndItemTitle(@RequestParam("content") String content,
                                                 @RequestParam(defaultValue = "0", name = "page") int page,
                                                 @RequestParam(defaultValue = "10", name = "size") int size,
                                                 Model model) {
        Page<ItemDto> itemDtos = userSearchService.getItemTitle(content, page, size);

        int totalPages = itemDtos.getTotalPages(); // 총 페이지 수
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

        // 전체 카테고리와 태그를 저장하기 위한 배열
        List<String> allCategorys = new ArrayList<>();
        List<String> allTags = new ArrayList<>();

        // 전체 페이지에 카테고리와 태그를 호출하여 배열에 저장
        for (int i = 0; i < totalPages; i++) {
            Page<ItemDto> itemDtos2 = userSearchService.getItemTitle(content, i, size);

            for (ItemDto itemDto : itemDtos2) {
                allCategorys.add(itemDto.getCategoryName().toString());

                for (ItemTag itemTag : itemDto.getItemTags()) {
                    allTags.add(itemTag.getItemTag());
                }
            }
        }

        // 각 값의 빈도를 세기 위한 Map
        Map<String, Integer> categoryFrequencyMap = new HashMap<>();
        Map<String, Integer> tagFrequencyMap = new HashMap<>();

        // 카테고리들의 빈도 세기
        for (String category : allCategorys) {
            categoryFrequencyMap.put(category, categoryFrequencyMap.getOrDefault(category, 0) + 1);
        }

        // 태그들의 빈도 세기
        for (String tag : allTags) {
            tagFrequencyMap.put(tag, tagFrequencyMap.getOrDefault(tag, 0) + 1);
        }

        // 빈도를 기준으로 내림차순 정렬한 리스트 생성
        List<Map.Entry<String, Integer>> sortedCategoryList  = new ArrayList<>(categoryFrequencyMap.entrySet());
        sortedCategoryList .sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
        List<Map.Entry<String, Integer>> sortedTagList = new ArrayList<>(tagFrequencyMap.entrySet());
        sortedTagList.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        // 정렬된 카테고리 결과를 새로운 배열로 저장
        List<String> sortedCategories = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedCategoryList) {
            sortedCategories.add(entry.getKey());
        }

        List<String> sortedTags = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedTagList) {
            sortedTags.add(entry.getKey());
        }

        // 모델에 필요한 속성 추가
        model.addAttribute("content", content); // 검색된 내용
        model.addAttribute("items", itemDtos); // 검색된 아이템들
        model.addAttribute("currentPage", currentPage); // 현재 페이지
        model.addAttribute("totalPages", totalPages); // 총 페이지 수
        model.addAttribute("startPage", startPage); // 표시할 시작 페이지
        model.addAttribute("endPage", endPage); // 표시할 끝 페이지
        model.addAttribute("size", size); // 페이지당 아이템 수
        model.addAttribute("categoryNames", sortedCategories); // 카테고리 중복 제거 후 넘김
        model.addAttribute("tagNames", sortedTags); // 태그 중복 제거 후 넘김
        model.addAttribute("tagNamesSize", sortedTags.size()); // 태그 갯수
        

        return "search/search";
    }

}
