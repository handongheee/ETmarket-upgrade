package kr.co.sist.etmarket.dao;

import kr.co.sist.etmarket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerDetailRepository extends JpaRepository<User,Long> {



}
