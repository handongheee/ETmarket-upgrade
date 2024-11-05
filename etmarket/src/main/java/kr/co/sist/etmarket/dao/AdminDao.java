package kr.co.sist.etmarket.dao;

import kr.co.sist.etmarket.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminDao extends JpaRepository<Admin, Long> {
    Admin findByAdminLoginId(String adminLoginId);
}
