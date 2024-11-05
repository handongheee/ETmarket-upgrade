package kr.co.sist.etmarket.service;

import kr.co.sist.etmarket.dao.DealDao;
import kr.co.sist.etmarket.dao.RatingDao;
import kr.co.sist.etmarket.dao.UserDao;
import kr.co.sist.etmarket.entity.Deal;
import kr.co.sist.etmarket.entity.Rating;
import kr.co.sist.etmarket.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RatingService {

    @Autowired
    private RatingDao ratingDao;

    @Autowired
    private DealDao dealDao;

    @Autowired
    private DealService dealService;

    @Autowired
    private UserDao userDao;

    @Transactional
    public Rating leaveRating(Long dealId, Long reviewerId, Long targetId, Double rating, String comment) {
        Deal deal = dealService.findDealById(dealId).orElseThrow(() -> new RuntimeException("Deal not found"));
        User reviewer = userDao.findById(reviewerId).orElseThrow(() -> new RuntimeException("Reviewer not found"));
        User target = userDao.findById(targetId).orElseThrow(() -> new RuntimeException("Target not found"));

        // 새로운 리뷰 객체 생성
        Rating newRating = new Rating();
        newRating.setDeal(deal);
        newRating.setReviewer(reviewer);
        newRating.setTarget(target);
        newRating.setRating(rating);
        newRating.setComment(comment);

        // Rating 저장
        ratingDao.save(newRating);

        // 리뷰 남김 여부 업데이트
        if (deal.getSeller().getUserId().equals(reviewerId)) {
            deal.setRatingLeft("Y," + deal.getRatingLeft().charAt(2));
        } else if (deal.getBuyer().getUserId().equals(reviewerId)) {
            deal.setRatingLeft(deal.getRatingLeft().charAt(0) + ",Y");
        }

        // Deal 업데이트
        dealDao.save(deal);

        return newRating;
    }
}
