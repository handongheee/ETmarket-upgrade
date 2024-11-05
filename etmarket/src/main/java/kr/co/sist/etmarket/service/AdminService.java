package kr.co.sist.etmarket.service;

import kr.co.sist.etmarket.dao.*;
import kr.co.sist.etmarket.dto.*;
import kr.co.sist.etmarket.entity.Admin;
import kr.co.sist.etmarket.entity.Item;
import kr.co.sist.etmarket.entity.User;
import kr.co.sist.etmarket.etenum.ItemStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminDao adminDao;
    private final UserDao userDao;
    private final ReportUserRepository reportUserRepository;
    private final ReportProductRepository reportProductRepository;
    private final ItemDao itemDao;
    private final TransactionRepository transactionRepository;

    // 관리자 로그인
    public boolean login(AdminDto adminDto) {

        // 로그인 처리 로직 구현
        Admin admin = adminDao.findByAdminLoginId(adminDto.getAdminLoginId());
        if (admin != null && admin.getAdminPassword().equals(adminDto.getAdminPassword())) {
            return true;
        }
        return false;
    }


    // 총 사용자 수 조회
    public long getTotalUsers() {
        return userDao.countTotalUsers();
    }

    // 오늘 신규 가입자 수 조회
    public long getNewUsersToday() {
        return userDao.countNewUsersToday();
    }

    // 활성 사용자 수 조회 메서드
    public long getActiveUsers() {
        return userDao.countActiveUsers();
    }

    // 일반 사용자 수 조회 메서드
    public long getGeneralUsers() {
        return userDao.countGeneralUsers();
    }

    // 카카오 사용자 수 조회 메서드
    public long getKakaoUsers() {
        return userDao.countKakaoUsers();
    }

    // 네이버 사용자 수 조회 메서드
    public long getNaverUsers() {
        return userDao.countNaverUsers();
    }

    // 최근 가입자 5명 가져오기
    public List<User> getRecentUsers() {
        return userDao.findTop5ByOrderByUserCreateDateDesc();
    }



    // 월별 사용자 가입 수 데이터 가져오기
    public Map<String, Integer> getMonthlyUserSignups() {
        // 월별 사용자 가입 수를 저장할 맵 초기화
        Map<String, Integer> monthlyUserSignups = new LinkedHashMap<>();

        // 데이터베이스에서 월별 사용자 가입 수 조회
        List<Object[]> results = userDao.countMonthlyUserSignups();

        // 모든 월을 0으로 초기화
        for (int i = 1; i <= 12; i++) {
            monthlyUserSignups.put(getMonthName(i), 0);
        }

        // 실제 데이터로 맵 업데이트
        for (Object[] result : results) {
            int month = (int) result[0];
            int count = ((Number) result[1]).intValue();
            monthlyUserSignups.put(getMonthName(month), count);
        }

        return monthlyUserSignups;
    }

    // 월 번호를 월 이름으로 변환하는 헬퍼 메서드
    private String getMonthName(int month) {
        switch (month) {
            case 1: return "Jan";
            case 2: return "Feb";
            case 3: return "Mar";
            case 4: return "Apr";
            case 5: return "May";
            case 6: return "Jun";
            case 7: return "Jul";
            case 8: return "Aug";
            case 9: return "Sep";
            case 10: return "Oct";
            case 11: return "Nov";
            case 12: return "Dec";
            default: return "";
        }
    }

    // 모든 사용자 조회
    public List<UserDto> findAllUsers() {
        List<User> users = userDao.findAll();
        return users.stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 사용자 ID로 사용자 정보 조회
    public UserDto findUserById(Long userId) {
        Optional<User> userOptional = userDao.findById(userId);
        return userOptional.map(UserDto::fromEntity).orElse(null);
    }

    // 사용자 정보 업데이트
    public void updateUser(UserDto userDto) {
        User user = userDao.findById(userDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        user.setUserLoginId(userDto.getUserLoginId());
        user.setUserPassword(userDto.getUserPassword());
        user.setUserName(userDto.getUserName());
        user.setUserEmail(userDto.getUserEmail());
        user.setUserPhone(userDto.getUserPhone());
        user.setUserStatus(userDto.getUserStatus());
        userDao.save(user);
    }

    public void deleteUser(Long userId) {
        // 신고 목록에서 신고당한 사용자가 reported인 경우 삭제
        //reportUserRepository.deleteByReported_UserId(userId);
        // 신고 목록에서 신고한 사용자가 reported인 경우 삭제
        //reportUserRepository.deleteByReporter_UserId(userId);
        // 사용자와 관련된 아이템 삭제
        //itemDao.deleteByUserId(userId);

        userDao.deleteById(userId);
    }










    // 모든 아이템 조회
    public List<ItemDto> findAllItems() {
        List<Item> items = itemDao.findAll();
        return items.stream().map(item -> {
            ItemDto dto = new ItemDto();
            dto.setItemId(item.getItemId());
            dto.setUserId(item.getUser().getUserId());
            dto.setItemTitle(item.getItemTitle());
            dto.setItemPrice(item.getItemPrice());
            dto.setItemStatus(item.getItemStatus());
            return dto;
        }).collect(Collectors.toList());
    }

    // 성공 거래 비율 계산 메서드
    public double getSuccessfulTransactionRate() {
        long totalTransactions = itemDao.countAllTransactions();
        long successfulTransactions = itemDao.countSuccessfulTransactions();
        if (totalTransactions == 0) {
            return 0;
        }
        return (double) successfulTransactions / totalTransactions * 100;
    }

    // 상품 상태별 분포 데이터 가져오기
    public Map<ItemStatus, Long> getItemStatusDistribution() {
        Map<ItemStatus, Long> statusDistribution = new LinkedHashMap<>();
        for (ItemStatus status : ItemStatus.values()) {
            long count = itemDao.countByItemStatus(status);
            statusDistribution.put(status, count);
        }
        return statusDistribution;
    }

    public long getTotalItems() {
        return itemDao.count();
    }








    // 신고 관리

    // 모든 사용자 신고 조회
    public List<ReportUserDto> getAllUserReports() {
        return reportUserRepository.findAll().stream()
                .map(report -> new ReportUserDto(
                        report.getReportUserId(),
                        report.getReporter().getUserId(),
                        report.getReporter().getUserName(),
                        report.getReported().getUserId(),
                        report.getReported().getUserName(),
                        report.getContent(),
                        report.getCreateDate()))
                .collect(Collectors.toList());
    }

    // 모든 상품 신고 조회
    public List<ReportProductDto> getAllProductReports() {
        return reportProductRepository.findAll().stream()
                .map(report -> new ReportProductDto(
                        report.getReportProductId(),
                        report.getUser().getUserId(),
                        report.getUser().getUserName(),
                        report.getItem().getItemId(),
                        report.getItem().getItemTitle(),
                        report.getTitle(),
                        report.getContent(),
                        report.getCreateDate()))
                .collect(Collectors.toList());
    }

    // 최근 사용자 신고 5개 조회
    public List<ReportUserDto> getRecentUserReports() {
        return reportUserRepository.findTop5ByOrderByCreateDateDesc().stream()
                .map(report -> new ReportUserDto(
                        report.getReportUserId(),
                        report.getReporter().getUserId(),
                        report.getReporter().getUserName(),
                        report.getReported().getUserId(),
                        report.getReported().getUserName(),
                        report.getContent(),
                        report.getCreateDate()))
                .collect(Collectors.toList());
    }

    // 최근 상품 신고 5개 조회
    public List<ReportProductDto> getRecentProductReports() {
        return reportProductRepository.findTop5ByOrderByCreateDateDesc().stream()
                .map(report -> new ReportProductDto(
                        report.getReportProductId(),
                        report.getUser().getUserId(),
                        report.getUser().getUserName(),
                        report.getItem().getItemId(),
                        report.getItem().getItemTitle(),
                        report.getTitle(),
                        report.getContent(),
                        report.getCreateDate()))
                .collect(Collectors.toList());
    }

    // 사용자 신고 총 수
    public long countTotalUserReports() {
        return reportUserRepository.count();
    }

    // 상품 신고 총 수
    public long countTotalProductReports() {
        return reportProductRepository.count();
    }

    // 사용자 신고 삭제
    public void deleteUserReport(Long reportUserId) {
        reportUserRepository.deleteById(reportUserId);
    }

    // 상품 신고 삭제
    public void deleteProductReport(Long reportProductId) {
        reportProductRepository.deleteById(reportProductId);
    }


}
