package kr.co.sist.etmarket.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.etmarket.dao.ItemDao;
import kr.co.sist.etmarket.dto.*;
import kr.co.sist.etmarket.entity.User;
import kr.co.sist.etmarket.etenum.ItemStatus;
import kr.co.sist.etmarket.service.AdminService;
import kr.co.sist.etmarket.service.ItemService;
import kr.co.sist.etmarket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final ItemService itemService;
    private final ItemDao itemDao;
    private final UserService userService;

    // 로그인 폼 이동
    @GetMapping("/admin/login")
    public String login() {
        return "admin/login/loginForm";
    }

    // 로그인 처리
    @PostMapping("/admin/login")
    public String loginSubmit(@ModelAttribute AdminDto adminDto, HttpSession session, Model model) {
        boolean loginSuccessful = adminService.login(adminDto);
        if (loginSuccessful) {
            session.setMaxInactiveInterval(60*60*8);
            session.setAttribute("myUserLoginId", adminDto.getAdminLoginId());
            session.setAttribute("loginok", "yes");
            return "redirect:/admin/dashboard";
        } else {
            model.addAttribute("loginError", "로그인 정보를 확인해 주세요.");
            return "admin/login/loginForm";
        }
    }

    // 로그아웃 처리
    @GetMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login?logout";
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(HttpSession session, Model model) {

        if (session.getAttribute("loginok") == null) {
            return "redirect:/admin/login";
        }

        // 최근 사용자 및 리포트 데이터를 가져오기
        List<User> recentUsers = adminService.getRecentUsers();
        List<ReportUserDto> recentUserReports = adminService.getRecentUserReports();
        List<ReportProductDto> recentProductReports = adminService.getRecentProductReports();

        // 리포트 카운트 데이터 가져오기
        long totalUserReports = adminService.countTotalUserReports();
        long totalProductReports = adminService.countTotalProductReports();

        // 사용자 통계 데이터 가져오기
        long totalUsers = adminService.getTotalUsers();
        long newUsersToday = adminService.getNewUsersToday();
        long generalUsers = adminService.getGeneralUsers();
        long kakaoUsers = adminService.getKakaoUsers();
        long naverUsers = adminService.getNaverUsers();


        // 월별 사용자 가입 수 데이터 가져오기
        Map<String, Integer> monthlyUserSignups = adminService.getMonthlyUserSignups();



        // 모델에 데이터 추가
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("recentUsers", recentUsers);
        model.addAttribute("newUsersToday", newUsersToday);
        model.addAttribute("generalUsers", generalUsers);
        model.addAttribute("kakaoUsers", kakaoUsers);
        model.addAttribute("naverUsers", naverUsers);
        model.addAttribute("totalUserReports", totalUserReports);
        model.addAttribute("totalProductReports", totalProductReports);
        model.addAttribute("recentUserReports", recentUserReports);
        model.addAttribute("recentProductReports", recentProductReports);
        model.addAttribute("monthlyUserSignups", monthlyUserSignups);

        // 성공 거래 비율 추가
        double successfulTransactionRate = adminService.getSuccessfulTransactionRate();
        model.addAttribute("successfulTransactionRate", (int) successfulTransactionRate); // 정수로 변환

        // 상품 상태별 분포 데이터 가져오기
        Map<ItemStatus, Long> itemStatusDistribution = adminService.getItemStatusDistribution();
        long totalItems = adminService.getTotalItems();

        // 모델에 데이터 추가
        model.addAttribute("itemStatusDistribution", itemStatusDistribution);
        model.addAttribute("totalItems", totalItems);



        return "admin/dashboard/dashboard";
    }

    @GetMapping("/admin/userlist")
    public String getAllUsers(HttpSession session, Model model) {

        if (session.getAttribute("loginok") == null) {
            return "redirect:/admin/login";
        }
        List<UserDto> users = adminService.findAllUsers();
        model.addAttribute("users", users);
        return "admin/user/userlist";
    }

    @GetMapping("/admin/userdetail/{userId}")
    public String getUserById(@PathVariable Long userId, Model model) {
        UserDto user = adminService.findUserById(userId);
        model.addAttribute("user", user);
        return "admin/user/userdetail";
    }

    @PostMapping("/admin/userupdate")
    public String updateUser(@ModelAttribute UserDto userDto) {
        adminService.updateUser(userDto);
        return "redirect:/admin/userdetail/" + userDto.getUserId();
    }




    // 모든 아이템 조회
    @GetMapping("/admin/itemlist")
    public String itemList(Model model) {
        List<ItemDto> itemDtoList = adminService.findAllItems();
        model.addAttribute("itemList", itemDtoList);
        return "admin/item/itemlist";
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


    // 아이템 삭제
    @DeleteMapping("/admin/item/{itemId}")
    @ResponseBody
    public String deleteItem(@PathVariable Long itemId) {
        System.out.println("Deleting item with ID: " + itemId);
        itemService.deleteItem(itemId);
        return "success";
    }

    // 회원 삭제
    @DeleteMapping("/admin/user/{userId}")
    @ResponseBody
    public String deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return "success";
    }

    // 신고

    @GetMapping("/admin/userReport")
    public String getUserReports(Model model) {
        List<ReportUserDto> userReports = adminService.getAllUserReports();
        model.addAttribute("userReports", userReports);
        return "admin/report/userReport";
    }

    @GetMapping("/admin/itemReport")
    public String getProductReports(Model model) {
        List<ReportProductDto> itemReports = adminService.getAllProductReports();
        model.addAttribute("itemReports", itemReports);
        return "admin/report/itemReport";
    }

    // 사용자 신고 삭제
    @DeleteMapping("/admin/userReport/{reportUserId}")
    @ResponseBody
    public String deleteUserReport(@PathVariable Long reportUserId) {
        adminService.deleteUserReport(reportUserId);
        return "success";
    }

    // 상품 신고 삭제
    @DeleteMapping("/admin/productReport/{reportProductId}")
    @ResponseBody
    public String deleteProductReport(@PathVariable Long reportProductId) {
        adminService.deleteProductReport(reportProductId);
        return "success";
    }


}
