package kr.co.sist.etmarket.controller;

import kr.co.sist.etmarket.dto.ItemDto;
import kr.co.sist.etmarket.dto.UserDto;
import kr.co.sist.etmarket.dto.UserSearchDto;
import kr.co.sist.etmarket.service.UserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserSearchRestController {

    @Autowired
    private UserSearchService userSearchService;

    @PostMapping("/search/insert")
    public void insertContent(@RequestBody UserSearchDto userSearchDto) {
        System.out.println("userSearchDto = " + userSearchDto);
        userSearchService.insertContent(userSearchDto);
    }

    @GetMapping("/api/search/items")
    public Page<ItemDto> findItemsByContentAndItemTitle(@RequestParam("content") String content,
                                                        @RequestParam(defaultValue = "0", name = "page") int page,
                                                        @RequestParam(defaultValue = "10", name = "size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userSearchService.getItemTitle(content, page, size);
    }

    @PostMapping("/search/delete")
    public void deleteContent(@RequestBody UserSearchDto userSearchDto) {
        System.out.println("userSearchDto = " + userSearchDto);
        userSearchService.deleteContent(userSearchDto.getContent());
    }

    @PostMapping("/search/deleteall")
    public void deleteAll(@RequestBody UserSearchDto userSearchDto) {
        System.out.println("userSearchDto = " + userSearchDto);
        userSearchService.deleteContent(userSearchDto.getContent());
    }

    @PostMapping("/search/init")
    public List<UserSearchDto> getTop8SearchContent(@RequestParam("userId") Long userId) {
        try {
            UserSearchDto userSearchDto = new UserSearchDto();
            userSearchDto.setUserId(userId);
            return userSearchService.getTop8SearchContent(userSearchDto);
        } catch (Exception e) {
            e.printStackTrace();
            throw e; // 예외를 다시 던져 클라이언트에게 전달
        }
    }

//    // 인기 검색어 8개 출력
//    @GetMapping("/popular")
//    public List<UserSearchDto> getTop8PopularContent() {
//        return userSearchService.getTop8PopularContent();
//    }

    // 최근 검색어를 클릭 시 상단으로 이동시키기 위함
    @PostMapping("/search/update")
    public ResponseEntity<Map<String, String>> getUserSearchUpdate(@RequestBody UserSearchDto userSearchDto) {
        try {
            // 클라이언트로부터 전달받은 UserSearchDto에서 사용자 ID를 가져와 UserDto 객체 생성
            UserDto user = new UserDto();
            user.setUserId(userSearchDto.getUserId());

            // 서비스 메서드를 호출하여 사용자와 콘텐츠를 기반으로 검색 내용을 업데이트
            userSearchService.getUserSearchUpdate(user, userSearchDto.getContent());

            // 응답으로 보낼 데이터를 담을 맵 객체 생성
            Map<String, String> response = new HashMap<>();
            response.put("status", "success"); // 상태를 'success'로 설정
            response.put("message", "검색 내용이 성공적으로 업데이트되었습니다"); // 성공 메시지 설정
            response.put("updatedContent", userSearchDto.getContent()); // 업데이트된 콘텐츠를 응답에 포함

            // 응답 객체를 생성하여 반환 (HTTP 200 OK와 함께 응답 데이터 반환)
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 예외 발생 시 오류 응답 반환
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "업데이트 중 오류가 발생했습니다.");

            // HTTP 500 상태 코드와 함께 오류 응답 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }



}
