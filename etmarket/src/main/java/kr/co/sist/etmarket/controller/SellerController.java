package kr.co.sist.etmarket.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.etmarket.dto.MyIntroductionBody;
import kr.co.sist.etmarket.dto.SellerDetailDto;
import kr.co.sist.etmarket.service.ItemDetailService;
import kr.co.sist.etmarket.service.SellerDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SellerController {

    private final SellerDetailService sellerDetailService;


    @GetMapping("/seller/{sellerId}/items")
    public String sellerItems(@PathVariable("sellerId") Long sellerId, Model model, HttpSession httpSession) {

        SellerDetailDto sellerDetailDto = sellerDetailService.getSellerDetailWithItems(sellerId);
        Long uid = (Long) httpSession.getAttribute("myUserId");

        model.addAttribute("sellerDetailDto", sellerDetailDto);
        model.addAttribute("uid", uid);


        return "seller/seller_detail_item";
    }

    @GetMapping("/seller/{sellerId}/reviews")
    public String sellerReviews(@PathVariable("sellerId") Long sellerId, Model model, HttpSession httpSession) {

        SellerDetailDto sellerDetailDto = sellerDetailService.getSellerDetailWithReviews(sellerId);
        Long uid = (Long) httpSession.getAttribute("myUserId");

        model.addAttribute("sellerDetailDto", sellerDetailDto);
        model.addAttribute("uid", uid);

        return "seller/seller_detail_review";
    }

    @PostMapping("/update-introduce")
    @ResponseBody
    public Map<String, Object> updateIntroduce(@RequestBody MyIntroductionBody myIntroductionBody, HttpSession httpSession) {

        Long uid = (Long) httpSession.getAttribute("myUserId");
        String description = myIntroductionBody.getDescription();

        Map<String, Object> response = new HashMap<>();
        String updatedIntroduction = sellerDetailService.updateMyIntroduction(uid, description);

        response.put("introduction", updatedIntroduction);

        return response;
    }

    @GetMapping("/get-introduce")
    @ResponseBody
    public Map<String, Object> getIntroduce(@RequestParam("userId") String userId, HttpSession httpSession) {

        Long uid = (Long) httpSession.getAttribute("myUserId");
        Long parsedUserId = Long.parseLong(userId);

        Map<String, Object> response = new HashMap<>();

        if (!uid.equals(parsedUserId)) {
            response.put("status", false);
        } else {
            String description = sellerDetailService.getMyIntroduction(uid);
            response.put("status", true);
            response.put("description", description);
        }

        return response;

    }
}
