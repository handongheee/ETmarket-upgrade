package kr.co.sist.etmarket.controller;

import kr.co.sist.etmarket.dto.RatingDto;
import kr.co.sist.etmarket.entity.Deal;
import kr.co.sist.etmarket.entity.Rating;
import kr.co.sist.etmarket.service.DealService;
import kr.co.sist.etmarket.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/rating")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private DealService dealService;

    @PostMapping("/leave")
    public ResponseEntity<RatingDto> leaveRating(@RequestBody RatingDto ratingDto) {
        try {
            Rating rating = ratingService.leaveRating(
                    ratingDto.getDealId(),
                    ratingDto.getReviewerId(),
                    ratingDto.getTargetId(),
                    ratingDto.getRating(),
                    ratingDto.getComment()
            );

            // Rating 엔티티를 RatingDto로 변환하여 클라이언트에 반환
            RatingDto responseDto = new RatingDto();
            responseDto.setDealId(rating.getDeal().getDealId());
            responseDto.setReviewerId(rating.getReviewer().getUserId());
            responseDto.setTargetId(rating.getTarget().getUserId());
            responseDto.setRating(rating.getRating());
            responseDto.setComment(rating.getComment());

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/{dealId}/getTargetId")
    public ResponseEntity<Map<String, Long>> getTargetId(@PathVariable Long dealId, @RequestParam Long reviewerId) {

        Optional<Deal> dealOptional = dealService.findDealById(dealId);
        if (dealOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Deal deal = dealOptional.get();
        Long targetId;

        if (deal.getBuyer().getUserId().equals(reviewerId)) {
            targetId = deal.getSeller().getUserId();
        } else if (deal.getSeller().getUserId().equals(reviewerId)) {
            targetId = deal.getBuyer().getUserId();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Map<String, Long> response = new HashMap<>();
        response.put("targetId", targetId);
        return ResponseEntity.ok(response);
    }
}