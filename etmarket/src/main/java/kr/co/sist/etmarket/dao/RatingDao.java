package kr.co.sist.etmarket.dao;

import kr.co.sist.etmarket.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingDao extends JpaRepository<Rating, Long> {
}
