package kr.co.sist.etmarket.service;

import kr.co.sist.etmarket.dao.ItemDetailRepository;
import kr.co.sist.etmarket.dao.ReviewRepository;
import kr.co.sist.etmarket.dao.SellerDetailRepository;
import kr.co.sist.etmarket.dao.TransactionRepository;
import kr.co.sist.etmarket.dto.*;
import kr.co.sist.etmarket.entity.Item;
import kr.co.sist.etmarket.entity.Rating;
import kr.co.sist.etmarket.entity.User;
import kr.co.sist.etmarket.etenum.DealStatus;
import kr.co.sist.etmarket.etenum.ItemHidden;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SellerDetailService {

    private final SellerDetailRepository sellerDetailRepository;
    private final TransactionRepository transactionRepository;
    private final ItemDetailRepository itemDetailRepository;
    private final ReviewRepository reviewRepository;
    private final CommonService commonService;


    public SimpleSellerDto getSimpleSellerInfo(Long sellerId, Long itemId) {

        User seller = sellerDetailRepository.findById(sellerId).orElseThrow(() -> new NoSuchElementException("id값에 해당하는 유저가 없습니다"));

        SimpleSellerDto simpleSellerDto = new SimpleSellerDto();

        simpleSellerDto.setSellerId(seller.getUserId());
        simpleSellerDto.setSellerName(seller.getUserName());
        simpleSellerDto.setSellerImgUrl(seller.getUserImg());
        simpleSellerDto.setTransactionCount(transactionRepository.countBySellerId(seller.getUserId()));
        simpleSellerDto.setSellItemCount(itemDetailRepository.countByUser_UserIdAndItemHiddenAndDealStatus(seller.getUserId(), ItemHidden.보임, DealStatus.판매중));
        simpleSellerDto.setReviewCount(reviewRepository.countByTarget_UserId(seller.getUserId()));

        Pageable pageableItem = PageRequest.of(0, 6);
        List<Item> sixItemList = itemDetailRepository.findRandomItemsByUserAndStatus(seller.getUserId(), DealStatus.판매중, ItemHidden.보임, itemId, pageableItem);
        simpleSellerDto.setSimpleItemDtoList(sixItemList.stream()
                .map(this::convertToSimpleItem)
                .collect(Collectors.toList()));

        Pageable pageableReview = PageRequest.of(0, 2);
        List<Rating> twoReviewList = reviewRepository.findByTarget_UserIdOrderByRatingDateDesc(seller.getUserId(), pageableReview);
        simpleSellerDto.setRevieweDtoList(twoReviewList.stream()
                .map(this::convertToReviewDto)
                .collect(Collectors.toList()));

        return simpleSellerDto;
    }

    public SellerDetailDto getSellerDetailWithItems(Long sellerId) {

        User seller = sellerDetailRepository.findById(sellerId).orElseThrow(() -> new NoSuchElementException("id값에 해당하는 유저가 없습니다"));

        SellerDetailDto sellerDetailDto = new SellerDetailDto();

        sellerDetailDto.setSellerId(seller.getUserId());
        sellerDetailDto.setSellerName(seller.getUserName());
        sellerDetailDto.setSellerImgUrl(seller.getUserImg());
        sellerDetailDto.setIntroDescription(seller.getUserIntroduce());
        sellerDetailDto.setTransactionCount(transactionRepository.countBySellerId(seller.getUserId()));
        sellerDetailDto.setSellCount(transactionRepository.countSellCountBySellerId(seller.getUserId()));
        sellerDetailDto.setTotalItemCount(itemDetailRepository.countByUser_UserIdAndItemHidden(seller.getUserId(), ItemHidden.보임));
        sellerDetailDto.setReviewCount(reviewRepository.countByTarget_UserId(seller.getUserId()));
        Optional<Double> avgReviewScore = reviewRepository.findByAverageReviewScoreByUserId(seller.getUserId());
        if (avgReviewScore.isPresent()) {
            sellerDetailDto.setAvgReviewScore(avgReviewScore.get());
        } else {
            sellerDetailDto.setAvgReviewScore(0.0);
        }
        sellerDetailDto.setSalesStartDate(commonService.convertTime(seller.getUserCreateDate()));

        List<Item> itemList = itemDetailRepository.findByUser_UserIdAndItemHiddenOrderByItemUpdateDateDesc(seller.getUserId(), ItemHidden.보임);
        sellerDetailDto.setSellerItemDtoList(itemList.stream()
                .map(this::convertSellerItemDto)
                .collect(Collectors.toList()));

        return sellerDetailDto;

    }

    public SellerDetailDto getSellerDetailWithReviews(Long sellerId) {

        User seller = sellerDetailRepository.findById(sellerId).orElseThrow(() -> new NoSuchElementException("id값에 해당하는 유저가 없습니다"));

        SellerDetailDto sellerDetailDto = new SellerDetailDto();

        sellerDetailDto.setSellerId(seller.getUserId());
        sellerDetailDto.setSellerName(seller.getUserName());
        sellerDetailDto.setSellerImgUrl(seller.getUserImg());
        sellerDetailDto.setIntroDescription(seller.getUserIntroduce());
        sellerDetailDto.setTransactionCount(transactionRepository.countBySellerId(seller.getUserId()));
        sellerDetailDto.setSellCount(transactionRepository.countSellCountBySellerId(seller.getUserId()));
        sellerDetailDto.setTotalItemCount(itemDetailRepository.countByUser_UserIdAndItemHidden(seller.getUserId(), ItemHidden.보임));
        sellerDetailDto.setReviewCount(reviewRepository.countByTarget_UserId(seller.getUserId()));
        Optional<Double> avgReviewScore = reviewRepository.findByAverageReviewScoreByUserId(seller.getUserId());
        if (avgReviewScore.isPresent()) {
            sellerDetailDto.setAvgReviewScore(Math.floor(avgReviewScore.get() * 10) / 10.0);
            sellerDetailDto.setReviewScorePercentage(commonService.calPercentScore(avgReviewScore.get()));
        } else {
            sellerDetailDto.setAvgReviewScore(0.0);
            sellerDetailDto.setReviewScorePercentage(0);
        }
        sellerDetailDto.setSalesStartDate(commonService.convertTime(seller.getUserCreateDate()));

        List<Rating> reviewList = reviewRepository.findByTarget_UserIdOrderByRatingDateDesc(seller.getUserId());
        sellerDetailDto.setReviewDtoList(reviewList.stream()
                .map(this::convertSellerReviewDto)
                .collect(Collectors.toList()));

        return sellerDetailDto;

    }

    private SimpleItemDto convertToSimpleItem(Item item) {
        DecimalFormat formatter = new DecimalFormat("#,###");

        return new SimpleItemDto(item.getItemId(), item.getItemImgs().get(0).getItemImg(),formatter.format(item.getItemPrice()));
    }

    private ReviewDto convertToReviewDto(Rating review) {

        return new ReviewDto(review.getReviewer().getUserId(),
                review.getReviewer().getUserName(),
                review.getReviewer().getUserImg(),
                review.getRating(),
                review.getDeal().getItem().getItemId(),
                review.getComment(),
                commonService.convertTime(review.getRatingDate()));
    }

    private SellerItemDto convertSellerItemDto(Item item) {
        DecimalFormat formatter = new DecimalFormat("#,###");

        return new SellerItemDto(item.getItemId(), item.getItemImgs().get(0).getItemImg(),item.getItemTitle(),
                formatter.format(item.getItemPrice()), commonService.trimAddress(item.getItemAddress()),
                commonService.convertTime(item.getItemUpdateDate()),item.getDealStatus(),item.getDeliveryStatus());
    }

    private SellerReviewDto convertSellerReviewDto(Rating review) {

        return new SellerReviewDto(review.getReviewer().getUserId(),
                review.getReviewer().getUserName(),
                review.getReviewer().getUserImg(),
                review.getRating(),
                review.getDeal().getItem().getItemId(),
                review.getDeal().getItem().getItemTitle(),
                review.getComment(),
                commonService.convertTime(review.getRatingDate())
        );

    }

    public String updateMyIntroduction(Long myUid, String description) {

        User user = sellerDetailRepository.findById(myUid).orElseThrow(() -> new NoSuchElementException("id값에 해당하는 유저가 없습니다"));

        user.setUserIntroduce(description);
        sellerDetailRepository.save(user);

        return description;


    }

    public String getMyIntroduction(Long myUid) {
        User user = sellerDetailRepository.findById(myUid).orElseThrow(() -> new NoSuchElementException("id값에 해당하는 유저가 없습니다"));

        return user.getUserIntroduce();
    }
}
