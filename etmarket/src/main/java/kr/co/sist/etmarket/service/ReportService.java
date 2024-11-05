package kr.co.sist.etmarket.service;


import kr.co.sist.etmarket.dao.ItemDetailRepository;
import kr.co.sist.etmarket.dao.ReportProductRepository;
import kr.co.sist.etmarket.dao.ReportUserRepository;
import kr.co.sist.etmarket.dao.SellerDetailRepository;
import kr.co.sist.etmarket.dto.BasicItemDto;
import kr.co.sist.etmarket.dto.BasicSellerDto;
import kr.co.sist.etmarket.entity.Item;
import kr.co.sist.etmarket.entity.ReportProduct;
import kr.co.sist.etmarket.entity.ReportUser;
import kr.co.sist.etmarket.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {

    private final SellerDetailRepository sellerDetailRepository;
    private final ItemDetailRepository itemDetailRepository;
    private final ReportUserRepository reportUserRepository;
    private final ReportProductRepository reportProductRepository;

    public BasicSellerDto getUserIdName(Long userId) {

        User user = sellerDetailRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("id값에 해당하는 유저가 없습니다"));

        return new BasicSellerDto(user.getUserId(), user.getUserName());
    }


    public void submitReportUser(Long userId, Long sellerId, String reportContent) {

        ReportUser reportUser = new ReportUser();

        User reporter = sellerDetailRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("id값에 해당하는 유저가 없습니다"));
        User reported = sellerDetailRepository.findById(sellerId).orElseThrow(() -> new NoSuchElementException("id값에 해당하는 유저가 없습니다"));



        reportUser.setReporter(reporter);
        reportUser.setReported(reported);
        reportUser.setContent(reportContent);

        reportUserRepository.save(reportUser);
    }

    public BasicItemDto getItemIdName(Long id) {

        Item item = itemDetailRepository.findById(id).orElseThrow(() -> new NoSuchElementException("id값에 해당하는 상품이 없습니다"));

        return new BasicItemDto(item.getItemId(), item.getItemTitle());
    }

    public void submitReportItem(Long myUid, Long itemId,String reportReason, String reportContent) {

        ReportProduct reportProduct = new ReportProduct();

        User reporter = sellerDetailRepository.findById(myUid).orElseThrow(() -> new NoSuchElementException("id값에 해당하는 유저가 없습니다"));
        Item item = itemDetailRepository.findById(itemId).orElseThrow(() -> new NoSuchElementException("id값에 해당하는 상품이 없습니다"));

        reportProduct.setUser(reporter);
        reportProduct.setItem(item);
        reportProduct.setTitle(reportReason);
        reportProduct.setContent(reportContent);

        reportProductRepository.save(reportProduct);
    }
}
